package com.example.kuba.exercise_01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ACTION_ITEM_ADDED = "com.example.kuba.intent.action.ITEM_ADDED";
    private EditText nameEditText, priceEditText, quantityEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        nameEditText = findViewById(R.id.nameEditText);
        priceEditText = findViewById(R.id.priceEditText);
        quantityEditText = findViewById(R.id.quantityEditText);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addItemButton:
                Item item = addItem();
                broadcastItemAddition(view, item);
                startActivity(new Intent(this, ItemListActivity.class));
                break;
            case R.id.cancelButton:
                startActivity(new Intent(this, ItemListActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ItemListActivity.class));
    }

    private Item addItem() {
        String name = nameEditText.getText().toString();
        Integer price = Integer.parseInt(priceEditText.getText().toString());
        Integer quantity = Integer.parseInt(quantityEditText.getText().toString());

        Item item = new Item(name, price, quantity, false);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("items");

        String id = databaseReference.push().getKey();

        databaseReference.child(id).setValue(item);

        Toast.makeText(this, "Item inserted", Toast.LENGTH_SHORT).show();
        return new Item(name, price, quantity, false);
    }

    private void broadcastItemAddition(View view, Item item) {
        SharedPreferences prefs = getSharedPreferences("Exercise_01_prefs", MODE_PRIVATE);
        String restoredText = prefs.getString("username", null);

        Intent intent = new Intent(ACTION_ITEM_ADDED);
        intent.putExtra("name", item.getName());
        intent.putExtra("username", restoredText);
        sendBroadcast(intent, Manifest.permission.NOTIFY_ITEM_ADDED);
    }
}
