package com.example.cpen321m1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.droidsonroids.gif.GifImageView;

public class SurpriseActivity extends AppCompatActivity {

    private Button postDBButton;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] permissionstorage = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private String state = "The Wizard is Reading Spells!";
    private Button readingButton;
    private GifImageView readingImageView;
    private Button attack1Button;
    private GifImageView attack1ImageView;

    private Button attack2Button;
    private GifImageView attack2ImageView;
    private Button runningButton;
    private GifImageView runningImageView;
    private Button goofyButton;
    private GifImageView goofyImageView;
    private Button hurtButton;
    private GifImageView hurtImageView;
    private Button deathButton;
    private GifImageView deathImageView;
    Bitmap screenshotBitmap;

    private Button getDBButton;
    List<Bitmap> bitmaps = new ArrayList<>();

    // Source: https://youtu.be/Psg7sdm6J6c?si=8xtF11tQGuIlBUj8
    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surprise);

        postDBButton = findViewById(R.id.postDB_button);
        verifyStoragePermissions(this);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.screenshot);
        postDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screenshotBitmap = screenshot(getWindow().getDecorView().getRootView());
                mediaPlayer.start();
                sendPostRequest();
                Toast.makeText(SurpriseActivity.this, "Took a screenshot and sending it to DB", Toast.LENGTH_SHORT).show();
            }
        });

        readingButton = findViewById(R.id.reading_button);
        readingImageView = findViewById(R.id.gifImageView);
        readingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = "The Wizard is Reading Spells!";
                Toast.makeText(SurpriseActivity.this, state, Toast.LENGTH_SHORT).show();
                readingImageView.setImageResource(R.drawable.reading);
            }
        });

        attack1Button = findViewById(R.id.attack1_button);
        attack1ImageView = findViewById(R.id.gifImageView);
        attack1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = "The Wizard is Power Stomping!";
                Toast.makeText(SurpriseActivity.this, state, Toast.LENGTH_SHORT).show();
                attack1ImageView.setImageResource(R.drawable.attack1);
            }
        });

        attack2Button = findViewById(R.id.attack2_button);
        attack2ImageView = findViewById(R.id.gifImageView);
        attack2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = "The Wizard is Energy Blasting!";
                Toast.makeText(SurpriseActivity.this, state, Toast.LENGTH_SHORT).show();
                attack2ImageView.setImageResource(R.drawable.attack2);
            }
        });

        runningButton = findViewById(R.id.running_button);
        runningImageView = findViewById(R.id.gifImageView);
        runningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = "The Wizard is Running with Haste!";
                Toast.makeText(SurpriseActivity.this, state, Toast.LENGTH_SHORT).show();
                runningImageView.setImageResource(R.drawable.running);
            }
        });

        goofyButton = findViewById(R.id.goofy_button);
        goofyImageView = findViewById(R.id.gifImageView);
        goofyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = "The Wizard is Acting Goofy!";
                Toast.makeText(SurpriseActivity.this, state, Toast.LENGTH_SHORT).show();
                goofyImageView.setImageResource(R.drawable.goofy);
            }
        });

        hurtButton = findViewById(R.id.hurt_button);
        hurtImageView = findViewById(R.id.gifImageView);
        hurtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = "The Wizard is Getting Hurt!";
                Toast.makeText(SurpriseActivity.this, state, Toast.LENGTH_SHORT).show();
                hurtImageView.setImageResource(R.drawable.hurt);
            }
        });

        deathButton = findViewById(R.id.death_button);
        deathImageView = findViewById(R.id.gifImageView);
        deathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = "The Wizard is Dead and the Planet has EXPLODED!";
                Toast.makeText(SurpriseActivity.this, state, Toast.LENGTH_SHORT).show();
                deathImageView.setImageResource(R.drawable.death);
            }
        });

        getDBButton = findViewById(R.id.getDB_button);
        getDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SurpriseActivity.this, "Getting images from DB and sending to email", Toast.LENGTH_SHORT).show();
                getImages();
            }
        });
    }

    // Source: https://www.geeksforgeeks.org/how-to-take-screenshot-programmatically-in-android/

    protected static Bitmap screenshot(View view) {
        try {
            // Get a bitmap from the view using the new method
            Bitmap bitmap = getBitmapFromView(view);
            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Source: https://stackoverflow.com/questions/52642055/view-getdrawingcache-is-deprecated-in-android-api-28
    static Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(
                view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    // ChatGPT for Bitmap stuff

    private void sendPostRequest() {
        OkHttpClient client = new OkHttpClient();

        // Define the URL where you want to send the POST request
        String postUrl = "http://ec2-3-21-114-89.us-east-2.compute.amazonaws.com:8081/imagelist";

        try {
            // Convert the screenshot Bitmap to a base64-encoded string
            int maxWidth = 800; // Set your desired maximum width
            int maxHeight = 600; // Set your desired maximum height
            int quality = 50; // Set your desired image quality

            // Resize the screenshot
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(screenshotBitmap, maxWidth, maxHeight, false);

            // Compress and encode the resized image
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

            // Create a JSON object with the screenshot data
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("image", encodedImage);

            // Set the request body and content type
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

            // Create the POST request
            Request request = new Request.Builder()
                    .url(postUrl)
                    .post(requestBody)
                    .build();

            // Enqueue the request asynchronously
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                    Log.e("SurpriseActivity", "POST request failed: " + e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        String responseBody = response.body().string();
                        Log.d("SurpriseActivity", responseBody);
                    } else {
                        Log.e("SurpriseActivity", "POST request failed with code: " + response.code());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void verifyStoragePermissions(Activity activity) {

        int permissions = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissions != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissionstorage, REQUEST_EXTERNAL_STORAGE);
        }
    }

    private void getImages() {
        OkHttpClient client = new OkHttpClient();
        String getUrl = "http://ec2-3-21-114-89.us-east-2.compute.amazonaws.com:8081/get-images";

        Request request = new Request.Builder()
                .url(getUrl)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.e("SurpriseActivity", "GET request failed: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONArray imageStrings = jsonResponse.getJSONArray("images");

                        // convert image strings to Bitmaps and store them in a list
                        for (int i = 0; i < imageStrings.length(); i++) {
                            String imageString = imageStrings.getString(i);
                            byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            bitmaps.add(bitmap);
                        }

                        Log.d("SurpriseActivity", "GET request succeeded with code: " + response.code());
                        Log.d("SurpriseActivity", "There are " + String.valueOf(bitmaps.size()) + " screenshots on the DB");

                        // SENDING EMAIL HERE
                        Intent emailIntent = new Intent(SurpriseActivity.this, EmailActivity.class);
                        String message = state + " There are " + String.valueOf(bitmaps.size()) + " screenshots on the DB." +
                                "This email was sent to you from Aman's first ever app! How cool is that?! \uD83E\uDD73";
                        emailIntent.putExtra("message", message);
                        startActivity(emailIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("SurpriseActivity", "GET request failed with code: " + response.code());
                }
            }
        });
    }
}