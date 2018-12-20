package com.example.kuba.exercise_01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditItemActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEditText, priceEditText, quantityEditText;
    private String id;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        nameEditText = findViewById(R.id.nameEditText);
        priceEditText = findViewById(R.id.priceEditText);
        quantityEditText = findViewById(R.id.quantityEditText);

        id = getIntent().getStringExtra("id");

        prepareItemData(id);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.applyButton:
                updateItemData();
                startActivity(new Intent(this, ItemListActivity.class));
                break;
            case R.id.deleteButton:
                deleteItemData();
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

    private void prepareItemData(String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("items").child(id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                item = dataSnapshot.getValue(Item.class);
                if (item != null) {
                    updateControls();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Getting item failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateItemData() {
        updateItem();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("items");

        databaseReference.child(id).setValue(item);

        Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
    }

    private void deleteItemData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("items");

        databaseReference.child(id).removeValue();

        Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
    }

    private void updateControls() {
        nameEditText.setText(item.getName());
        priceEditText.setText(String.valueOf(item.getPrice()));
        quantityEditText.setText(String.valueOf(item.getQuantity()));
    }

    private void updateItem() {
        item.setName(nameEditText.getText().toString());
        item.setPrice(Integer.parseInt(priceEditText.getText().toString()));
        item.setQuantity(Integer.parseInt(quantityEditText.getText().toString()));
    }
}
