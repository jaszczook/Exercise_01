package com.example.kuba.exercise_01;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.listButton:
                startActivity(new Intent(this, ItemListActivity.class));
                break;
            case R.id.shopsButton:
                startActivity(new Intent(this, ShopListActivity.class));
                break;
            case R.id.optionsButton:
                startActivity(new Intent(this, OptionsActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    void shopHelper() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("shops");

        String id = databaseReference.push().getKey();

        Shop shop = new Shop("Grocery store", "Fresh vegetables!", 11, 52.229676, 21.012229);

        databaseReference.child(id).setValue(shop);

        id = databaseReference.push().getKey();

        shop = new Shop("Toy store", "Best fun!", 111, 52.239123, 20.913154);

        databaseReference.child(id).setValue(shop);

        id = databaseReference.push().getKey();

        shop = new Shop("Nothing store", "Nothing to do in Sosnowiec!", 111, 50.278048, 19.134348);

        databaseReference.child(id).setValue(shop);
    }
}
