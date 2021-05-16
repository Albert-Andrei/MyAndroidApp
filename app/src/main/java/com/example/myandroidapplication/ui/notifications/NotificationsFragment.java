package com.example.myandroidapplication.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myandroidapplication.R;
import com.example.myandroidapplication.SignInActivity;
import com.example.myandroidapplication.ViewModel.HomeViewModel;
import com.example.myandroidapplication.ViewModel.NotificationsViewModel;
import com.google.android.material.button.MaterialButtonToggleGroup;

import static androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private MaterialButtonToggleGroup themeButtons;
    private HomeViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.main));
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        themeButtons = root.findViewById(R.id.buttonToggleGroup);
        themeButtons.check(R.id.autoTheme);

        themeButtons.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

                if (isChecked) {
                    if (checkedId == R.id.lightTheme) {
                        setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    } else if (checkedId == R.id.darkTheme) {
                        ((AppCompatActivity)getActivity()).getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        ((AppCompatActivity)getActivity()).getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    }
                }
            }
        });

        Button signOutBtn = root.findViewById(R.id.sign_out);
        signOutBtn.setOnClickListener(v -> {
            viewModel.signOut();
            startActivity(new Intent(getActivity(), SignInActivity.class));
            getActivity().finish();
        });
        return root;
    }
}