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
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.bson.Document;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.emailRegText) EditText emailRegText;
    @BindView(R.id.passRegText) EditText passRegText;
    @BindView(R.id.confRegText) EditText confRegText;
    @BindView(R.id.registerBtn) Button registerBtn;
    @BindView(R.id.regProgressBar) ProgressBar regProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
        Realm.init(this);

        SharedPreferences loginPrefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor Ed = loginPrefs.edit();
        App app = new App(new AppConfiguration.Builder(getString(R.string.mongoAppId)).build());

        registerBtn.setOnClickListener(view -> {
            String email = emailRegText.getText().toString().trim();
            String pass = passRegText.getText().toString();
            String confirm = confRegText.getText().toString();

            if(email.isEmpty() || pass.isEmpty() || confirm.isEmpty())
                Snackbar.make(view, "Please fill all fields", Snackbar.LENGTH_LONG).show();
            else if (!pass.equals(confirm))
                Snackbar.make(view, "Passwords do not match", Snackbar.LENGTH_LONG).show();
            else{
                regProgressBar.setVisibility(View.VISIBLE);
                app.getEmailPassword().registerUserAsync(email, pass, it -> {
                    if(it.isSuccess()){
                        Ed.putString("Email", email);
                        Ed.putString("Password", pass);
                        Ed.commit();
                        startActivity(new Intent(RegisterActivity.this, DetailsActivity.class));
                        finish();
                    } else {
                        regProgressBar.setVisibility(View.INVISIBLE);
                        Log.e("Error", String.valueOf(it.getError()));
                        Snackbar.make(view, "Error in registering. Please try again", Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}