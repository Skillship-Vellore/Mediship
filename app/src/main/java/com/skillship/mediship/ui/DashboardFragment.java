package com.skillship.mediship.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.skillship.mediship.LoginActivity;
import com.skillship.mediship.MainActivity;
import com.skillship.mediship.R;
import com.skillship.mediship.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardFragment extends Fragment {

    @BindView(R.id.logoutBtn) Button logoutBtn;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        ButterKnife.bind(this, root);
        SharedPreferences loginPrefs = getContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor Ed = loginPrefs.edit();

        logoutBtn.setOnClickListener(view -> {
            Ed.putString("Email", null);
            Ed.putString("Password", null);
            Ed.commit();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });
        return root;
    }
}