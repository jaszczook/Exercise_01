package com.example.kuba.exercise_01;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditItemActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEditText, priceEditText, quantityEditText;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        nameEditText = findViewById(R.id.nameEditText);
        priceEditText = findViewById(R.id.priceEditText);
        quantityEditText = findViewById(R.id.quantityEditText);

        int id = (int) getIntent().getSerializableExtra("id");
        prepareItemData(id);

        nameEditText.setText(item.getName());
        priceEditText.setText(String.valueOf(item.getPrice()));
        quantityEditText.setText(String.valueOf(item.getQuantity()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.applyButton:
                updateItemData(item.getId());
                startActivity(new Intent(this, ItemListActivity.class));
                break;
            case R.id.deleteButton:
                deleteItemData(item.getId());
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

    private void prepareItemData(int id) {
        ItemDbHelper itemDbHelper = new ItemDbHelper(this);
        SQLiteDatabase sqLiteDatabase = itemDbHelper.getWritableDatabase();

        Cursor cursor = itemDbHelper.readItem(id, sqLiteDatabase);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.NAME));
            Integer price = cursor.getInt(cursor.getColumnIndex(ItemContract.ItemEntry.PRICE));
            Integer quantity = cursor.getInt(cursor.getColumnIndex(ItemContract.ItemEntry.QUANTITY));
            boolean isBought = cursor.getInt(cursor.getColumnIndex(ItemContract.ItemEntry.IS_BOUGHT)) == 1;

            item = new Item(id, name, price, quantity, isBought);
        }

        itemDbHelper.close();
    }

    private void updateItemData(int id) {
        String name = nameEditText.getText().toString();
        Integer price = Integer.parseInt(priceEditText.getText().toString());
        Integer quantity = Integer.parseInt(quantityEditText.getText().toString());
        boolean isBought = item.isBought();

        ItemDbHelper itemDbHelper = new ItemDbHelper(this);
        SQLiteDatabase sqLiteDatabase = itemDbHelper.getWritableDatabase();
        itemDbHelper.updateItem(id, name, price, quantity, isBought, sqLiteDatabase);
        itemDbHelper.close();
        Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
    }

    private void deleteItemData(int id) {
        ItemDbHelper itemDbHelper = new ItemDbHelper(this);
        SQLiteDatabase sqLiteDatabase = itemDbHelper.getWritableDatabase();
        itemDbHelper.deleteItem(id, sqLiteDatabase);
        itemDbHelper.close();
        Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
    }
}
