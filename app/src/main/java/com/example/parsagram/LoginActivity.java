package com.example.parsagram;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private Button loginBtn;
    private Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        usernameInput = findViewById(R.id.tiUsername);
        passwordInput = findViewById(R.id.tiPassword);
        loginBtn = findViewById(R.id.btnLogin);
        signupBtn = findViewById(R.id.btnSignup);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.instagram_word_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Log.d("PersistActivity", "persist successful");
            final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameInput.getText().toString();
                Log.println(Log.DEBUG, "entered username: ", username);
                final String password = passwordInput.getText().toString();
                Log.println(Log.DEBUG, "entered password: ",password);

                login(username, password);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = new ParseUser();

                user.setUsername(usernameInput.getText().toString());
                user.setPassword(passwordInput.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("SignupActivity", "signup successful");
                            final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.e("SignupActivity", "signup failure");
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Log.d("LoginActivity", "login successful");
                    final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Check your username or password", Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", "login failure");
                    e.printStackTrace();
                }
            }
        });
    }
}
