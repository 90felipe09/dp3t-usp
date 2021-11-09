package com.example.dp3t_usp.APIService;

import static android.provider.Settings.System.getString;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dp3t_usp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RemoteAPIService implements APIService{
    private static final String TAG = "REMOTE_API";
    private String urlString;
    private Context context;
    private RequestQueue queue;

    public RemoteAPIService(String urlString, Context context){
        Log.e(" start api", " startin");
        this.context = context;
        this.urlString = urlString;
        this.queue = Volley.newRequestQueue(context);
    }

    public void sendHashes(ArrayList<String> hashes, final onPostHashesSuccessCallback successCallback, final onPostHashesFailureCallback failureCallback){
        try {
            URL url = new URL(this.urlString + "/collect");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            connection.setChunkedStreamingMode(0);

            Map<String, Object> data = new HashMap<>();
            data.put("hashes", hashes);

            JSONObject requestBody = new JSONObject(data);

            PrintStream printStream = new PrintStream(connection.getOutputStream());
            printStream.println(requestBody);

            Log.i("[SEND HASHES]", requestBody.toString());

            connection.connect();

            connection.disconnect();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getInfectedHashes(final onGetHashesSuccessCallback successCallback ){
        Log.i("getInfectedHashes()", "Is performing getInfectedHashes from remote");
        String checkUri = this.urlString + "check";
        final ArrayList hashes = new ArrayList();
        StringRequest request = new StringRequest(Request.Method.GET, checkUri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Response", response);
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray infectedHashes = responseObject.getJSONArray("infected_hashes");
                    if (infectedHashes != null) {

                        //Iterating JSON array
                        for (int i=0;i<infectedHashes.length();i++){

                            //Adding each element of JSON array into ArrayList
                            hashes.add(infectedHashes.get(i));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("hashes being callbacked", hashes.toString());
                successCallback.callback(hashes);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.toString());
            }
        });

        queue.add(request);
    }
}
