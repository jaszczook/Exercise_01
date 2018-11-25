package com.example.kuba.exercise_01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class ItemListActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Item> itemList = new ArrayList<>();
    private HashMap<String, Item> itemHashMap = new HashMap<>();
    private TextView shoppingListTitleTextView;
    private RecyclerView recyclerView;
    private ItemListAdapter itemListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        shoppingListTitleTextView = findViewById(R.id.shoppingListTitleTextView);

        SharedPreferences prefs = getSharedPreferences("Exercise_01_prefs", MODE_PRIVATE);

        String restoredText = prefs.getString("username", null);
        if (restoredText != null) {
            shoppingListTitleTextView.setText(restoredText.concat("'s shopping list"));
        }

        boolean restoredBoolean = prefs.getBoolean("tip", true);
        if (!restoredBoolean) {
            findViewById(R.id.tipButton).setVisibility(View.INVISIBLE);
        }

        recyclerView = findViewById(R.id.listRecycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // add item separator
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        itemListAdapter = new ItemListAdapter(itemList);
        recyclerView.setAdapter(itemListAdapter);

        recyclerView.addOnItemTouchListener(new ItemListClickListener(getApplicationContext(), recyclerView, new ItemListClickListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                Intent intent = new Intent(view.getContext(), EditItemActivity.class);

                for (Map.Entry<String, Item> entry : itemHashMap.entrySet()) {
                    if (entry.getValue() == itemList.get(position)) {
                        intent.putExtra("id", entry.getKey());
                        break;
                    }
                }

                startActivity(intent);
            }
        }));

        prepareItemsData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addItemButton:
                startActivity(new Intent(this, AddItemActivity.class));
                break;
            case R.id.mainMenuButton:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.saveStatusButton:
                saveStatus();
                break;
            case R.id.tipButton:
                Toast toast = Toast.makeText(this, "Tap on an item and hold for a while to edit :)", Toast.LENGTH_LONG);
                LinearLayout layout = (LinearLayout) toast.getView();
                if (layout.getChildCount() > 0) {
                    TextView tv = (TextView) layout.getChildAt(0);
                    tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                }
                toast.show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void prepareItemsData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("items");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemHashMap.clear();
                itemList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String id = postSnapshot.getKey();
                    Item item = postSnapshot.getValue(Item.class);
                    itemHashMap.put(id, item);
                }

                itemList.addAll(itemHashMap.values());

                itemListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Getting items failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveStatus() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("items");

        for (Map.Entry<String, Item> entry : itemHashMap.entrySet()) {
            databaseReference.child(entry.getKey()).setValue(entry.getValue());
        }

        Toast.makeText(this, "Status saved", Toast.LENGTH_SHORT).show();
    }
}
