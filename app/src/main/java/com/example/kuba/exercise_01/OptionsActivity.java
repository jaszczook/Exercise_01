package com.example.kuba.exercise_01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class OptionsActivity extends AppCompatActivity implements View.OnClickListener {

    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        username = findViewById(R.id.usernameEditText);

        SharedPreferences prefs = getSharedPreferences("Exercise_01_prefs", MODE_PRIVATE);
        String restoredText = prefs.getString("username", null);
        if (restoredText == null) {
            Toast.makeText(this, "No username defined", Toast.LENGTH_SHORT).show();
        }
        username.setText(restoredText);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.applyButton:
                if (applyOptions()) {
                    startActivity(new Intent(this, MainActivity.class));
                }
                break;
            case R.id.cancelButton:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private boolean applyOptions() {
        SharedPreferences.Editor editor = getSharedPreferences("Exercise_01_prefs", MODE_PRIVATE).edit();
        String restoredText = username.getText().toString();
        if (restoredText.isEmpty()) {
            Toast.makeText(this, "Enter valid username", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            editor.putString("username", restoredText);
            editor.apply();
            return true;
        }
    }
}
