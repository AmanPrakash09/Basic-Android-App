package com.example.cpen321m1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ServerActivity extends AppCompatActivity {

    final static String TAG = "ServerActivity";
    String serverIP;
    String serverTime;
    String fullName;
    TextView serverIPTextView;
    TextView serverTimeTextView;
    TextView myNameTextView;
    TextView clientIPTextView;
    TextView clientTimeTextView;
    TextView loggedInTextView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        String clientIP = getIntent().getStringExtra("clientIP");
        String clientTime = getIntent().getStringExtra("clientTime");
        String clientName = getIntent().getStringExtra("clientName");

        serverIPTextView = findViewById(R.id.server_ip_textView);
        serverTimeTextView = findViewById(R.id.server_time_textView);
        myNameTextView = findViewById(R.id.my_name_textView);

        clientIPTextView = findViewById(R.id.client_ip_textView);
        clientIPTextView.setText("Client IP Address: " + clientIP);

        clientTimeTextView = findViewById(R.id.client_time_textView);
        clientTimeTextView.setText("Client Local Time: " + clientTime);

        loggedInTextView = findViewById(R.id.logged_in_textView);
        loggedInTextView.setText("Logged In: " + clientName);

        startServerActivity(clientName);
    }

    // Source: https://youtu.be/oGWJ8xD2W6k?si=plXluHkyTvQ2cVet

    @SuppressLint("SetTextI18n")
    private void startServerActivity(String clientName) {
        OkHttpClient client = new OkHttpClient();

        /* _____________________________________________________________________________________________*/

        String ipURL = "http://ec2-3-21-114-89.us-east-2.compute.amazonaws.com:8081/server-ip";

        Request ipRequest = new Request.Builder()
                .url(ipURL)
                .build();

        client.newCall(ipRequest).enqueue((new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String myResponse = response.body().string();
                    try {
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(myResponse, JsonObject.class);

                        if (jsonObject.has("IPAddress")) {
                            serverIP = jsonObject.get("IPAddress").getAsString();
                            serverIPTextView.setText("Server IP Address: " + serverIP);
                            Log.d(TAG, "Server IP Address: " + serverIP);
                        } else {
                            Log.d(TAG, "Server IP Address not found in JSON response");
                        }
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }));

        /* _____________________________________________________________________________________________*/

        String serverURL = "http://ec2-3-21-114-89.us-east-2.compute.amazonaws.com:8081/server-time";

        Request serverRequest = new Request.Builder()
                .url(serverURL)
                .build();

        client.newCall(serverRequest).enqueue((new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String myResponse = response.body().string();
                    try {
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(myResponse, JsonObject.class);

                        if (jsonObject.has("serverTime")) {
                            serverTime = jsonObject.get("serverTime").getAsString();
                            serverTimeTextView.setText("Server Local Time: " + serverTime);
                            Log.d(TAG, "Server Time: " + serverTime);
                        } else {
                            Log.d(TAG, "Server Time not found in JSON response");
                        }
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }));

        /* _____________________________________________________________________________________________*/

        String nameURL = "http://ec2-3-21-114-89.us-east-2.compute.amazonaws.com:8081/my-name";

        Request nameRequest = new Request.Builder()
                .url(nameURL)
                .build();

        client.newCall(nameRequest).enqueue((new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String myResponse = response.body().string();
                    try {
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(myResponse, JsonObject.class);

                        if (jsonObject.has("fullName")) {
                            fullName = jsonObject.get("fullName").getAsString();
                            myNameTextView.setText("My Name: " + fullName);
                            Log.d(TAG, "Full Name: " + fullName);
                        } else {
                            Log.d(TAG, "Full Name not found in JSON response");
                        }
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }));

        /* _____________________________________________________________________________________________*/

    }
}
