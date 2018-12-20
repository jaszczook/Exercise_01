package com.example.kuba.exercise_01;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private List<Shop> shopList = new ArrayList<>();
    private HashMap<String, Shop> shopHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        prepareShopsData();
    }

    private void prepareShopsData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("shops");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shopHashMap.clear();
                shopList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String id = postSnapshot.getKey();
                    Shop shop = postSnapshot.getValue(Shop.class);
                    shopHashMap.put(id, shop);
                }

                shopList.addAll(shopHashMap.values());

                updateGoogleMap();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Getting shops failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateGoogleMap() {
        googleMap.clear();

        for (Shop shop : shopList) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(shop.getLatitude(), shop.getLongitude()))
                    .title(shop.getName())
                    .snippet(shop.getDescription()));
        }

        if (!shopList.isEmpty()) {
            Shop firstShop = shopList.get(0);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(firstShop.getLatitude(), firstShop.getLongitude())));
        }
    }
}
