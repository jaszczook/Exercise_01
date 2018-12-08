package com.example.kuba.exercise_01;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddShopActivity extends AppCompatActivity implements View.OnClickListener {

    LocationManager locationManager;
    LocationListener locationListener;
    private EditText nameEditText, descriptionEditText, radiusEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop);

        nameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        radiusEditText = findViewById(R.id.radiusEditText);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

        Toast.makeText(this, displayGpsStatus().toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addShopButton:
                addShop();
                startActivity(new Intent(this, ShopListActivity.class));
                break;
            case R.id.cancelButton:
                startActivity(new Intent(this, ShopListActivity.class));
                break;
        }
    }

    private void addShop() {
        String name = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        Integer radius = Integer.parseInt(radiusEditText.getText().toString());

        Shop shop = new Shop(name, description, radius, new Double(0), new Double(0));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("shops");

        String id = databaseReference.push().getKey();

        databaseReference.child(id).setValue(shop);

        Toast.makeText(this, "Shop inserted", Toast.LENGTH_SHORT).show();
    }

    /*----Method to Check GPS is enable or disable ----- */
    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext().getContentResolver();
        return Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
    }
}
