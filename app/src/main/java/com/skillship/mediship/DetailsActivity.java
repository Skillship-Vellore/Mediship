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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.bson.Document;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.nameEditText) EditText nameEditText;
    @BindView(R.id.ageEditText) EditText ageEditText;
    @BindView(R.id.phoneEditText) EditText phoneEditText;
    @BindView(R.id.genderRadio) RadioGroup genderRadio;
    @BindView(R.id.submitBtn) Button submitBtn;
    @BindView(R.id.detailsProgress) ProgressBar detailsProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        submitBtn.setOnClickListener(view -> {

            detailsProgress.setVisibility(View.VISIBLE);
            SharedPreferences loginPrefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            String email = loginPrefs.getString("Email", null);
            String pass = loginPrefs.getString("Password", null);

            App app = new App(new AppConfiguration.Builder(getString(R.string.mongoAppId)).build());
            Credentials credentials = Credentials.emailPassword(email, pass);
            app.loginAsync(credentials, result -> {
                if (result.isSuccess()) {

                    User user = app.currentUser();
                    MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
                    MongoDatabase mongoDatabase = mongoClient.getDatabase("mediship");
                    MongoCollection<Document> collection = mongoDatabase.getCollection("userdata");

                    RadioButton selectedGender = findViewById(genderRadio.getCheckedRadioButtonId());

                    collection.insertOne(new Document("userId", user.getId())
                            .append("Name", nameEditText.getText().toString().trim())
                            .append("Phone", Long.parseLong(phoneEditText.getText().toString()))
                            .append("Gender", selectedGender.getText())
                            .append("Age", Integer.parseInt(ageEditText.getText().toString()))
                            .append("Email", email)
                    ).getAsync(insertResult -> {
                        if (insertResult.isSuccess()) {
                            Log.i("SignUp", "Signup successful and added to database");
                            startActivity(new Intent(DetailsActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Log.e("Error", String.valueOf(insertResult.getError()));
                            detailsProgress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Error in database, please try again", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Error in database, please try again", Toast.LENGTH_LONG).show();
                    Log.e("Error", String.valueOf(result.getError()));
                    detailsProgress.setVisibility(View.INVISIBLE);
                }
            });
        });
    }
}