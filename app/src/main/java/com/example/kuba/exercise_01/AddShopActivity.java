package com.example.kuba.exercise_01;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.kuba.exercise_01.LoginActivity.GEOFENCE_EXPIRATION_IN_MILLISECONDS;

public class AddShopActivity extends AppCompatActivity implements View.OnClickListener {

    LocationManager locationManager;
    LocationListener locationListener;
    private EditText nameEditText, descriptionEditText, radiusEditText;
    private GeofencingClient geofencingClient;
    private Geofence geofence;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop);

        nameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        radiusEditText = findViewById(R.id.radiusEditText);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        /*locationListener = new MyLocationListener();*/

        geofencingClient = LocationServices.getGeofencingClient(this);
        geofence = null;
        pendingIntent = null;

/*        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addShopButton:
                if (addShop()) {
                    startActivity(new Intent(this, ShopListActivity.class));
                }
                break;
            case R.id.cancelButton:
                startActivity(new Intent(this, ShopListActivity.class));
                break;
        }
    }

    private boolean addShop() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Enabling GPS might help", Toast.LENGTH_SHORT).show();
            return false;
        }

        String name = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        Integer radius = Integer.parseInt(radiusEditText.getText().toString());
        Location location = getLastBestLocation();

        if (location == null) {
            Toast.makeText(this, "Unable to get exact location", Toast.LENGTH_SHORT).show();
            return false;
        }

        Shop shop = new Shop(name, description, radius, location.getLatitude(), location.getLongitude());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("shops");

        String id = databaseReference.push().getKey();

        databaseReference.child(id).setValue(shop);

        Toast.makeText(this, "Shop inserted", Toast.LENGTH_SHORT).show();

        initializeGeofence(id, shop);
        addGeofence();

        return true;
    }

    /**
     * @return the last know best location
     */
    private Location getLastBestLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long GPSLocationTime = 0;
        if (null != locationGPS) {
            GPSLocationTime = locationGPS.getTime();
        }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if (0 < GPSLocationTime - NetLocationTime) {
            return locationGPS;
        } else {
            return locationNet;
        }
    }

    private void initializeGeofence(String id, Shop shop) {
        geofence = new Geofence.Builder()
                .setRequestId(id)
                .setCircularRegion(
                        shop.getLatitude(),
                        shop.getLongitude(),
                        shop.getRadius()
                )
                .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }

    private void addGeofence() {
        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Geofence added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to add geofence", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.addGeofence(geofence);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (pendingIntent != null) {
            return pendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }
}
