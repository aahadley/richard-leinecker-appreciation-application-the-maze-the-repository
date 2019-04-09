package com.example.therichardleineckerappreciationappthemaze;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPAsyncTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {
        Log.v("tag", "in doInBackground function");
        // params comes from the execute() call: params[0] is the url.
        try {
            try {
                return HttpPost(urls[0]);
            } catch (JSONException e) {
                e.printStackTrace();
                return "Error!";
            }
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {

        //  tvResult.setText(result);
    }
    private String HttpPost(String myUrl) throws IOException, JSONException {
        Log.v("tag", "in HttpPost function");
        String result = "";

        URL url = new URL(myUrl);

        // 1. create HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        // 2. build JSON object
        JSONObject jsonObject = buidJsonObject();

        // 3. add JSON content to POST request body
        setPostRequestContent(conn, jsonObject);

        // 4. make POST request to the given URL
        conn.connect();

        // 5. return response message
        return conn.getResponseMessage()+"";

    }
    private JSONObject buidJsonObject() throws JSONException {
        Log.v("tag", "in JSONObject function");

        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("name", "My name");
        jsonObject.accumulate("country",  "my country");
        jsonObject.accumulate("twitter",  "my twitter");

        return jsonObject;
    }
    private void setPostRequestContent(HttpURLConnection conn,
                                       JSONObject jsonObject) throws IOException {
        Log.v("tag", "in setPostRequest function");
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        Log.i(MainActivity.class.toString(), jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }
}