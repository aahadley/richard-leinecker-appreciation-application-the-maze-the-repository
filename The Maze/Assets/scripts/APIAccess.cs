using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;

public class APIAccess : MonoBehaviour
{

    private PlayerController pc;
    public int NumMovesToGrab = 10;
    private string key = "76wAj5TCRfy2s2XA7dAeuBJtDzDafFBR"; //76wAj5TCRfy2s2XA7dAeuBJtDzDafFBR
    private string url = "http://iburch.pythonanywhere.com";

    private IEnumerator GetMovesCoroutine;

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

        //WWWForm form = new WWWForm();
        //form.AddField("num_moves", NumMovesToGrab);
        //form.AddField("auth-key", auth);

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

            if (s[0] == '{' && s[len-2] != '}')
                str = s.Substring(11);
            else if (s[0] != '{' && s[len-2] == '}')
                str = s.Substring(1);
            else if (s[0] == '{' && s[len-2] == '}')
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

    private string ProcessString (string s)
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
}
