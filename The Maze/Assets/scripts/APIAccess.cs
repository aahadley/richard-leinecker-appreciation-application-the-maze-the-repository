using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.SceneManagement;

public class APIAccess : MonoBehaviour
{

    private PlayerController pc;
    public int NumMovesToGrab = 10;
    private string key = "76wAj5TCRfy2s2XA7dAeuBJtDzDafFBR"; //76wAj5TCRfy2s2XA7dAeuBJtDzDafFBR
    private string url = "http://iburch.pythonanywhere.com";

    private IEnumerator GetMovesCoroutine;
    private IEnumerator HighScoreCoroutine;

    private bool GetMovesFlag = false;

    // Start is called before the first frame update
    void Start()
    {
        pc = gameObject.GetComponent<PlayerController>();
    }

    // Update is called once per frame
    void Update()
    {
        if (GetMovesFlag == false)
        {
            GetMovesFlag = true;
            GetMovesCoroutine = GetMoves();
            StartCoroutine(GetMovesCoroutine);
        }
    }

    private class MovesClass
    {
        public int num_moves;
        public string key;
    }

    IEnumerator GetMoves()
    {
        //Debug.Log("Entering GetMoves()");

        MovesClass mc = new MovesClass();
        mc.num_moves = NumMovesToGrab;
        mc.key = key;
        string json = JsonUtility.ToJson(mc);

        UnityWebRequest uwr = new UnityWebRequest(url + "/getmoves", "POST");
        byte[] jsonToSend = new System.Text.UTF8Encoding().GetBytes(json);
        uwr.uploadHandler = (UploadHandler)new UploadHandlerRaw(jsonToSend);
        uwr.downloadHandler = (DownloadHandler)new DownloadHandlerBuffer();
        uwr.SetRequestHeader("Content-Type", "application/json");

        yield return uwr.SendWebRequest();

        if (uwr.isNetworkError)
        {
            Debug.Log("Error While Sending: " + uwr.error);
        }
        else
        {
            //Debug.Log("Received: " + uwr.downloadHandler.text);
            string input = uwr.downloadHandler.text;
            string[] moveList = input.Split(',');

            if (moveList[0].Length == 13)
            {
                GetMovesFlag = false;
                yield break;
            }

            moveList = ProcessMoveList(moveList);
            pc.addCommands(moveList);
        }

        GetMovesFlag = false;
    }

    public string[] ProcessMoveList(string[] moveList)
    {
        string str;
        List<string> newMoveList = new List<string>();

        foreach (string s in moveList)
        {
            int len = s.Length;

            if (s[0] == '{' && s[len - 2] != '}')
                str = s.Substring(11);
            else if (s[0] != '{' && s[len - 2] == '}')
                str = s.Substring(1);
            else if (s[0] == '{' && s[len - 2] == '}')
                str = s.Substring(11);
            else
                str = s.Substring(1);

            str = ProcessString(str);

            //Debug.Log("attempting to add " + str);
            if (str.Equals("Up") || str.Equals("Down") || str.Equals("Left") || str.Equals("Right"))
                newMoveList.Add(str);
        }
        return newMoveList.ToArray();
    }

    private string ProcessString(string s)
    {
        if (s[0] == 'U')
            return s.Substring(0, 2);
        else if (s[0] == 'D')
            return s.Substring(0, 4);
        else if (s[0] == 'L')
            return s.Substring(0, 4);
        else
            return s.Substring(0, 5);
    }



    //============== HIGHSCORE FUNCTIONS ==============

    public class CoroutineWithData
    {
        public Coroutine coroutine { get; private set; }
        public object result;
        private IEnumerator target;
        public CoroutineWithData(MonoBehaviour owner, IEnumerator target)
        {
            this.target = target;
            this.coroutine = owner.StartCoroutine(Run());
        }

        private IEnumerator Run()
        {
            while (target.MoveNext())
            {
                result = target.Current;
                yield return result;
            }
        }
    }

    private class GetHighScoreClass
    {
        public string seed;
    }

    IEnumerator GetHighScore(int seed)
    {
        string input = "failure";
        //Debug.Log("Entering GetHighScore\tseed = " + seed);

        GetHighScoreClass hsc = new GetHighScoreClass();
        hsc.seed = seed.ToString();
        string json = JsonUtility.ToJson(hsc);
        //Debug.Log("json: " + json);

        UnityWebRequest uwr = new UnityWebRequest(url + "/gethighscore", "POST");
        byte[] jsonToSend = new System.Text.UTF8Encoding().GetBytes(json);
        uwr.uploadHandler = (UploadHandler)new UploadHandlerRaw(jsonToSend);
        uwr.downloadHandler = (DownloadHandler)new DownloadHandlerBuffer();
        uwr.SetRequestHeader("Content-Type", "application/json");

        //Debug.Log("pre yield return");
        yield return uwr.SendWebRequest();
        //Debug.Log("post yield return");

        if (uwr.isNetworkError)
        {
           // Debug.Log("Error While Sending: " + uwr.error);
        }
        else
        {
            //Debug.Log("Received: " + uwr.downloadHandler.text);
            input = uwr.downloadHandler.text;
            yield return input;
        }

        if (input.Equals("failure"))
            yield return "failure";

    }

    public void SendHighScore(int time, int seed)
    {
        //Debug.Log("Entering SendHighScore\tseed = " + seed + "\ttime = " + time);

        HighScoreCoroutine = CheckHighScore(time, seed);
        StartCoroutine(HighScoreCoroutine);
    }

    private class NewHighScoreClass
    {
        public string seed;
        public double score;
    }

    IEnumerator CheckHighScore(int time, int seed)
    {
        CoroutineWithData cd = new CoroutineWithData(this, GetHighScore(seed));
        yield return cd.coroutine;
       // Debug.Log("result is " + cd.result);  //  input or 'fail'

        string result = cd.result.ToString();
        string toDouble = result.Slice(13, result.Length-2);
        int oldTime = int.Parse(toDouble);
        //Debug.Log("oldTime: " + oldTime);

        if(oldTime > time)
        {
            //Debug.Log("New record! Sending data...");
            NewHighScoreClass nhs = new NewHighScoreClass();
            nhs.seed = seed.ToString();
            nhs.score = time;
            string json = JsonUtility.ToJson(nhs);
            //Debug.Log("json: " + json);

            UnityWebRequest uwr = new UnityWebRequest(url + "/newhighscore", "POST");
            byte[] jsonToSend = new System.Text.UTF8Encoding().GetBytes(json);
            uwr.uploadHandler = (UploadHandler)new UploadHandlerRaw(jsonToSend);
            uwr.downloadHandler = (DownloadHandler)new DownloadHandlerBuffer();
            uwr.SetRequestHeader("Content-Type", "application/json");

            //Debug.Log("pre yield return");
            yield return uwr.SendWebRequest();
            //Debug.Log("post yield return");

            if (uwr.isNetworkError)
            {
                Debug.Log("Error While Sending: " + uwr.error);
            }
            else
            {
                //Debug.Log("Received: " + uwr.downloadHandler.text);
                yield return "success";
            }

            yield return "failure";
        }

        SceneManager.LoadScene("Maze");
    }

}
public static class Extensions
{
    public static string Slice(this string source, int start, int end)
    {
        if (end < 0) // Keep this for negative end support
        {
            end = source.Length + end;
        }
        int len = end - start;               // Calculate length
        return source.Substring(start, len); // Return Substring of length
    }

}
