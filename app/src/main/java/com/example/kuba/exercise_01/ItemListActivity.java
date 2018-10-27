package com.example.kuba.exercise_01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private RecyclerView recyclerView;
    private ItemListAdapter itemListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
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
                Item item = itemList.get(position);
                Toast.makeText(getApplicationContext(), item.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Item item = itemList.get(position);
                Intent intent = new Intent(view.getContext(), EditItemActivity.class);
                intent.putExtra("item", item);
                startActivity(intent);
            }
        }));

        prepareItemData();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.addItemButton:
                startActivity(new Intent(this, AddItemActivity.class));
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

    private void prepareItemData() {
       itemList.add(new Item("Name 1", 10, true));
       itemList.add(new Item("Name 2", 20, false));
       itemList.add(new Item("Name 3", 100, false));
       itemList.add(new Item("Name 4", 100, false));
       itemList.add(new Item("Name 5", 1050, false));
       itemList.add(new Item("Name 6", 200, false));
       itemList.add(new Item("Name 7", 30, false));

       itemListAdapter.notifyDataSetChanged();
    }
}
