package com.example.kuba.exercise_01;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {

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
                addItem();
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

    private void addItem() {
        String name = nameEditText.getText().toString();
        Integer price = Integer.parseInt(priceEditText.getText().toString());
        Integer quantity = Integer.parseInt(quantityEditText.getText().toString());

        ItemDbHelper itemDbHelper = new ItemDbHelper(this);
        SQLiteDatabase sqLiteDatabase = itemDbHelper.getWritableDatabase();
        itemDbHelper.addItem(name, price, quantity, false, sqLiteDatabase);
        itemDbHelper.close();
        Toast.makeText(this, "Item inserted", Toast.LENGTH_SHORT).show();
    }
}
