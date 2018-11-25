package com.example.kuba.exercise_01;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

// login: abc@abc.abc password: abcABCabc
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText loginEditText, passwordEditText;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEditText = findViewById(R.id.loginEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.loginButton:
                login();
                break;
            case R.id.registerButton:
                register();
                break;
        }
    }

    private void login() {
        String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (!login.isEmpty() && !password.isEmpty()) {
            // firebaseAuth.signInWithEmailAndPassword("abc@abc.abc", "abcABCabc")
            firebaseAuth.signInWithEmailAndPassword(login, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Insert credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private void register() {
        String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (!login.isEmpty() && !password.isEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(login, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Insert credentials", Toast.LENGTH_SHORT).show();
        }
    }
}
