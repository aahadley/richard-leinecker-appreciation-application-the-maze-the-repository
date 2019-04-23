package my.app.therichardleineckerappreciationappthemaze;

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
        // params comes from the execute() call: params[0] is the url.
        try {
            try {
                return HttpPost();
            } catch (JSONException e) {
                e.printStackTrace();
                return "Error!";
            }
        } catch (IOException e) {
            return constants.gameServerFailure;
        }
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result)
    { }

    private String HttpPost() throws IOException, JSONException {
        String result = "";

        URL url = new URL(constants.APIURL);

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
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("move", MainActivity.command);

        return jsonObject;
    }
    private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException {
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        Log.i(MainActivity.class.toString(), jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }
}