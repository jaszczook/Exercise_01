package com.example.kuba.exercise_01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.ArrayList;
import java.util.List;

public class ItemListActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Item> itemList = new ArrayList<>();
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

        recyclerView = (RecyclerView) findViewById(R.id.listRecycler);

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
                Item item = itemList.get(position);
                Intent intent = new Intent(view.getContext(), EditItemActivity.class);
                intent.putExtra("id", item.getId());
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
        ItemDbHelper itemDbHelper = new ItemDbHelper(this);
        SQLiteDatabase sqLiteDatabase = itemDbHelper.getWritableDatabase();

        Cursor cursor = itemDbHelper.readItems(sqLiteDatabase);

        while (cursor.moveToNext()) {
            Integer id = cursor.getInt(cursor.getColumnIndex(ItemContract.ItemEntry._ID));
            String name = cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.NAME));
            Integer price = cursor.getInt(cursor.getColumnIndex(ItemContract.ItemEntry.PRICE));
            Integer quantity = cursor.getInt(cursor.getColumnIndex(ItemContract.ItemEntry.QUANTITY));
            boolean isBought = cursor.getInt(cursor.getColumnIndex(ItemContract.ItemEntry.IS_BOUGHT)) == 1;

            itemList.add(new Item(id, name, price, quantity, isBought));
        }

        itemListAdapter.notifyDataSetChanged();

        itemDbHelper.close();
    }

    private void saveStatus() {
        ItemDbHelper itemDbHelper = new ItemDbHelper(this);
        SQLiteDatabase sqLiteDatabase = itemDbHelper.getWritableDatabase();

        for (Item item : itemList) {
            Integer id = item.getId();
            String name = item.getName();
            Integer price = item.getPrice();
            Integer quantity = item.getQuantity();
            boolean isBought = item.isBought();

            itemDbHelper.updateItem(id, name, price, quantity, isBought, sqLiteDatabase);
        }

        itemDbHelper.close();
        Toast.makeText(this, "Status saved", Toast.LENGTH_SHORT).show();
    }
}
