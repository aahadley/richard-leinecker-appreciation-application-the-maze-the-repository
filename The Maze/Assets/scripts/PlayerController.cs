using System.Collections;
using System.Collections.Generic;
using UnityEngine.SceneManagement;
using UnityEngine;
using TMPro;

public class PlayerController : MonoBehaviour
{
    public float speed;
    public AudioClip CoinSound = null;
    private Rigidbody rb;
    private AudioSource mAudioSource = null;
    private Queue<string> Commands = new Queue<string>();
    public TextMeshProUGUI timeText;

    private double timer; 

    void Start()
    {   
        rb = GetComponent<Rigidbody>();
        timer = 0;
    }

    void FixedUpdate()
    {
        controllerMovement();
        timer += Time.deltaTime;
        timeText.text = $"{(int)timer}";
        

    }

    public void addCommands(string[] input)
    {

        foreach (string command in input)
        {

            Commands.Enqueue(command);

        }

    }

    void controllerMovement()
    {
        float moveHorizontal = Input.GetAxis("Horizontal");
        float moveVertical = Input.GetAxis("Vertical");

        Vector3 movement = new Vector3(moveHorizontal, 0.0f, moveVertical);

        MoveInDirection(movement);

        if( Commands.Count > 0)
        {

            processCommand();

        }

    }

    private void restartGame()
    {

        gameObject.GetComponent<APIAccess>().SendHighScore((int)timer,Consts.Seed);
        //SceneManager.LoadScene("Maze");
       
    }

    void OnTriggerEnter(Collider other)
    {

        if (other.gameObject.tag.Equals("Coin"))
        {
            if (mAudioSource != null && CoinSound != null)
            {
                mAudioSource.PlayOneShot(CoinSound);
            }
            restartGame();
            Destroy(other.gameObject);
           
        }

    }

    void MoveInDirection(Vector3 direc)
    {

        rb.AddForce(direc * speed);

    }

    void processCommand()
    {

        var command = Commands.Dequeue();
        var direc = new Vector3();
        switch (command)
        {

            case "Left":
                direc = new Vector3(-1, 0, 0);
                break;
            case "Right":
                direc = new Vector3(1, 0, 0);
                break;
            case "Up":
                direc = new Vector3(0, 0, 1);
                break;
            case "Down":
                direc = new Vector3(0, 0, -1);
                break;

        }

        MoveInDirection(direc);
    }




}
