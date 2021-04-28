package com.skillship.mediship.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skillship.mediship.R;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

import static com.skillship.mediship.MainActivity.app;

public class HomeFragment extends Fragment {

    @BindView(R.id.medicinesRecycler)
    RecyclerView medicinesRecycler;
    private MongoDatabase mongoDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, root);

        medicinesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        medicinesRecycler.setHasFixedSize(true);

        User user = app.currentUser();
        MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("mediship");
        MongoCollection<Document> collection = mongoDatabase.getCollection("patients");

        getUserPrescriptions(user.getId(), collection);

        return root;
    }

    private void getUserPrescriptions(String id, MongoCollection<Document> collection) {
        Document queryFilter = new Document().append("userId", id);
        collection.findOne(queryFilter).getAsync(result -> {
            if (result.isSuccess()) {
                String res = result.get().toJson();
                try {
                    JSONObject json = new JSONObject(res);
                    JSONArray medArray = json.getJSONArray("Medicines");
                    getAllMedicines(medArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else
                Log.e("HomeFragment", result.getError().toString());
        });
    }

    private void getAllMedicines(JSONArray medArray) {

        MongoCollection<Document> medCollections = mongoDatabase.getCollection("Medicine");
        Document query = null;
//        try {
            query = new Document().append("medID", "6077b1eccf9e4216e82ce179");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        medCollections.findOne(query).getAsync(result -> {
            if (result.isSuccess()) {
//                String res = result.get().toJson();
//                try {
                Log.e("Info", String.valueOf(result.get()));
//                    JSONObject json = new JSONObject(res);
//                    Log.e("MedName", json.getString("Name"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            } else
                Log.e("HomeFragment", result.getError().toString());
        });

    }
}