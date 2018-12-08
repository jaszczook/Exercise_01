package com.example.kuba.exercise_01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopListActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Shop> shopList = new ArrayList<>();
    private HashMap<String, Shop> shopHashMap = new HashMap<>();
    private RecyclerView recyclerView;
    private ShopListAdapter shopListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        recyclerView = findViewById(R.id.shopListRecycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // add item separator
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        shopListAdapter = new ShopListAdapter(shopList);
        recyclerView.setAdapter(shopListAdapter);

        recyclerView.addOnItemTouchListener(new ListClickListener(getApplicationContext(), recyclerView, new ListClickListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                String id = null;

                for (Map.Entry<String, Shop> entry : shopHashMap.entrySet()) {
                    if (entry.getValue() == shopList.get(position)) {
                        id = entry.getKey();
                        break;
                    }
                }

                deleteShopData(id);
            }
        }));

        prepareShopsData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addShopButton:
                startActivity(new Intent(this, AddShopActivity.class));
                break;
            case R.id.mainMenuButton:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.mapsButton:
                startActivity(new Intent(this, MapsActivity.class));
                break;
        }
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

                shopListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Getting shops failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteShopData(String id) {
        if (!id.isEmpty()) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("shops");

            databaseReference.child(id).removeValue();

            Toast.makeText(this, "Shop deleted", Toast.LENGTH_SHORT).show();
        }
    }
}
