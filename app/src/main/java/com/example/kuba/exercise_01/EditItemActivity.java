package com.example.kuba.exercise_01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditItemActivity extends AppCompatActivity {

    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        this.item = (Item) getIntent().getSerializableExtra("item");
    }
}
