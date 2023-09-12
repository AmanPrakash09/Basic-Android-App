package com.example.cpen321m1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";
    private Button mapsButton;
    private Button detailsButton;
    private String cityGeo;
    private boolean locationUpdateReceived = false;
    private Handler handler = new Handler();
    private Runnable checkLocationUpdateRunnable;
    private GoogleSignInClient mGoogleSignInClient;
    private Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapsButton = findViewById(R.id.maps_button);
        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Trying to open Google maps");

                Intent mapsIntent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(mapsIntent);
            }
        });

        detailsButton = findViewById(R.id.details_button);
//        detailsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                checkLocationPermissions();
//                Log.d(TAG, "Trying to request location permissions");
////                Toast.makeText(MainActivity.this, "Trying to request location permissions", Toast.LENGTH_LONG).show();
////                if (checkLocationPermissions()) {
////                    getLocationInfo();
////                    String city = cityGeo; // Get the city information
////                    String manufacturer = android.os.Build.MANUFACTURER;
////                    String model = android.os.Build.MODEL;
////
////                    // Create an Intent to launch DeviceInfoActivity
////                    Intent phoneDetailsIntent = new Intent(MainActivity.this, DetailsActivity.class);
////
////                    // Pass the data as extras to DeviceInfoActivity
////                    phoneDetailsIntent.putExtra("city", city);
////                    phoneDetailsIntent.putExtra("manufacturer", manufacturer);
////                    phoneDetailsIntent.putExtra("model", model);
////
////                    // Start DeviceInfoActivity
////                    startActivity(phoneDetailsIntent);
////                }
//                if (checkLocationPermissions()) {
//                    getLocationInfo();
//                    if (locationUpdateReceived) {
//                        String city = cityGeo; // Get the city information
//                        String manufacturer = android.os.Build.MANUFACTURER;
//                        String model = android.os.Build.MODEL;
//
//                        // Create an Intent to launch DeviceInfoActivity
//                        Intent phoneDetailsIntent = new Intent(MainActivity.this, DetailsActivity.class);
//
//                        // Pass the data as extras to DeviceInfoActivity
//                        phoneDetailsIntent.putExtra("city", city);
//                        phoneDetailsIntent.putExtra("manufacturer", manufacturer);
//                        phoneDetailsIntent.putExtra("model", model);
//
//                        // Start DeviceInfoActivity
//                        startActivity(phoneDetailsIntent);
//                    } else {
//                        // If a valid update has not been received yet, inform the user
//                        Toast.makeText(MainActivity.this, "Waiting for location update. Please try again.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkLocationPermissions()) {
                    getLocationInfo();

                    // Check location update status periodically
                    checkLocationUpdateRunnable = new Runnable() {
                        @Override
                        public void run() {
                            if (locationUpdateReceived) {
                                handler.removeCallbacks(this); // Stop checking
                                String city = cityGeo; // Get the city information
                                String manufacturer = android.os.Build.MANUFACTURER;
                                String model = android.os.Build.MODEL;

                                // Create an Intent to launch DeviceInfoActivity
                                Intent phoneDetailsIntent = new Intent(MainActivity.this, DetailsActivity.class);

                                // Pass the data as extras to DeviceInfoActivity
                                phoneDetailsIntent.putExtra("city", city);
                                phoneDetailsIntent.putExtra("manufacturer", manufacturer);
                                phoneDetailsIntent.putExtra("model", model);

                                // Start DeviceInfoActivity
                                startActivity(phoneDetailsIntent);
                            } else {
                                // Continue checking until location update is received
                                Toast.makeText(MainActivity.this, "Loading location ...", Toast.LENGTH_SHORT).show();
                                handler.postDelayed(this, 1000); // Check again after 1 second (adjust the delay as needed)
                            }
                        }
                    };

                    // Start checking location update
                    handler.post(checkLocationUpdateRunnable);
                }
            }
    });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        signOutButton = findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    private ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    handleSignInResult(task);
                }
            }
    );

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        signInLauncher.launch(signInIntent);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "Log out successful!");
                        Toast.makeText(MainActivity.this, "You have been signed out", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account == null) {
            Log.d(TAG, "There is no user signed in!");
        }
        else {
            Log.d(TAG, "Pref Name: " + account.getDisplayName());
            Log.d(TAG, "Email: " + account.getEmail());
            Log.d(TAG, "Given Name: " + account.getGivenName());
            Log.d(TAG, "Family Name: " + account.getFamilyName());
            Log.d(TAG, "Display URI: " + account.getPhotoUrl());

            // Send token to your back-end
            // Move to another activity
        }
    }

    private boolean checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Need Location Permissions")
                        .setMessage("We need the location permissions to mark your location on a map")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        return false;
    }

    private void getLocationInfo() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationChanged(Location location) {
                // Get city information from the location
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                // You can use Geocoder to get city information from latitude and longitude
                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                try {
                    // Get the city name from the latitude and longitude
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    assert addresses != null;
                    if (!addresses.isEmpty()) {
                        cityGeo = addresses.get(0).getLocality();
                        // Display the city name in the TextView
                        locationUpdateReceived = true;
                    } else {
                        // Handle the case where no address information is available
                        cityGeo = "Not Found";
                    }
                } catch (IOException e) {
                    // Handle Geocoder exceptions, such as network errors
                    e.printStackTrace();
                    cityGeo = "Error";
                }
            }

            // Implement other LocationListener methods if needed
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
}