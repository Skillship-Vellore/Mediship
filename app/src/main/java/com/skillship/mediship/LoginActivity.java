package com.skillship.mediship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.emailEditText) EditText emailEditText;
    @BindView(R.id.passwordEditText) EditText passwordEditText;
    @BindView(R.id.loginButton) Button loginButton;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    String appId = "mediship-vhxze";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        Realm.init(this);

        SharedPreferences loginPrefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor Ed = loginPrefs.edit();
        App app = new App(new AppConfiguration.Builder(appId).build());

        loginButton.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            String email = emailEditText.getText().toString().trim();
            String pass = passwordEditText.getText().toString();

            if(email.isEmpty() || pass.isEmpty())
                Snackbar.make(view, "Please fill all fields", Snackbar.LENGTH_LONG).show();
            else{
                progressBar.setVisibility(View.VISIBLE);
                Credentials credentials = Credentials.emailPassword(email,pass);
                app.loginAsync(credentials, result -> {
                    if(result.isSuccess()) {
                        Ed.putString("Email", email);
                        Ed.putString("Password", pass);
                        Ed.commit();
                        Log.e("Info", "User logged in successfully");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }else {
                        Log.e("Info", "Error in logging in");
                        progressBar.setVisibility(View.INVISIBLE);
                        Snackbar.make(view, "Error in logging in. Please try again", Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}