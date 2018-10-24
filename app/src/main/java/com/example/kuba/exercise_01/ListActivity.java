package com.example.kuba.exercise_01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private List<Item> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ItemListAdapter itemListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        recyclerView = (RecyclerView) findViewById(R.id.listRecycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        itemListAdapter = new ItemListAdapter(itemList);
        recyclerView.setAdapter(itemListAdapter);

        prepareItemData();
    }

    private void prepareItemData() {
       itemList.add(new Item("Name 1", 10));
       itemList.add(new Item("Name 2", 20));
       itemList.add(new Item("Name 3", 100));
       itemList.add(new Item("Name 4", 100));
       itemList.add(new Item("Name 5", 1050));
       itemList.add(new Item("Name 6", 200));
       itemList.add(new Item("Name 7", 30));

       itemListAdapter.notifyDataSetChanged();
    }
}
