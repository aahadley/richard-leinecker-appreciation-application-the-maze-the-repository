using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TenureBehavior : MonoBehaviour
{
    private float timer = 0.0001f;   

    public float ScaleSpeed = 2;
    public float minScale = .9f;
    public float maxScale = 1.2f;

    private Vector3 min;
    private Vector3 max;


    private bool increaseing = true;
    // Start is called before the first frame update
    void Start()
    {
        gameObject.transform.rotation = Quaternion.Euler(90, 0, 0);
        min = new Vector3(minScale, 1, minScale);
        max = new Vector3(maxScale, 1, maxScale);
    }

    // Update is called once per frame
    void Update()
    {

    
        if(increaseing)
        {

            if( timer/ScaleSpeed > 1)
            {
                increaseing = false;
            }
            else
            {
                timer += Time.deltaTime;

            }

        }
        else
        {

            if( timer /ScaleSpeed <= 0)
            {
                increaseing = true;
            }
            else
            {
                timer -= Time.deltaTime;
            }

        }

        gameObject.transform.localScale = Vector3.Lerp(min, max, timer / ScaleSpeed);


    }
}
