package com.skillship.mediship;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;

public class MainActivity extends AppCompatActivity {

    String appId = "mediship-vhxze";
    public static App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        app = new App(new AppConfiguration.Builder(appId).build());
        SharedPreferences loginPrefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        String email = loginPrefs.getString("Email", null);
        String pass = loginPrefs.getString("Password", null);

        if(email == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }else{
            Credentials credentials = Credentials.emailPassword(email,pass);
            app.loginAsync(credentials, result -> {
                if(result.isSuccess())
                    Log.e("Info","User logged in successfully");
                else
                    Log.e("MainActivity/Error", result.getError().toString());
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }
}