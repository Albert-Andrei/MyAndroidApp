package com.example.myandroidapplication.ui.notifications;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.myandroidapplication.R;
import com.example.myandroidapplication.SignInActivity;
import com.example.myandroidapplication.ViewModel.HomeViewModel;
import com.example.myandroidapplication.ViewModel.NotificationsViewModel;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private MaterialButtonToggleGroup themeButtons;
    private HomeViewModel homeViewModel;
    private NotificationsViewModel viewModel;
    private int item = 0;
    private ImageView langImage;
    private TextView languageTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.main));
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel = new NotificationsViewModel();

        langImage = root.findViewById(R.id.langIcon);
        languageTextView = root.findViewById(R.id.languageTextField);
        loadLocal();

        themeButtons = root.findViewById(R.id.buttonToggleGroup);
        themeButtons.check(R.id.autoTheme);

        themeButtons.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.lightTheme) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else if (checkedId == R.id.darkTheme) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                }
            }
        });

        homeViewModel.getCurrentUser().observe(getActivity(), user -> {
            if (user != null) {
                TextView user_name = root.findViewById(R.id.user_name);
                String hy = getResources().getString(R.string.welcome);
                user_name.setText(hy + " " + user.getDisplayName());

                ImageView profileImage = root.findViewById(R.id.profile_image);
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(profileImage);
            }
        });

        LinearLayout linearLayoutLang = root.findViewById(R.id.linearLayoutLang);
        linearLayoutLang.setOnClickListener(v -> showChangeLanguageDialog());

        Button signOutBtn = root.findViewById(R.id.sign_out);
        signOutBtn.setOnClickListener(v -> {
            homeViewModel.signOut();
            startActivity(new Intent(getActivity(), SignInActivity.class));
            getActivity().finish();
        });
        return root;
    }

    public void showChangeLanguageDialog() {
        String[] languages = {"English", "Română", "Dansk", "Русский "};

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        mBuilder.setTitle("Choose language");
        mBuilder.setSingleChoiceItems(languages, item, (dialog, which) -> {
            switch (which) {
                case 0:
                    setLocal("en");
                    item = 0;
                    languageTextView.setText(languages[item]);
                    langImage.setImageResource(R.drawable.usa_flag);
                    reset();
                    break;
                case 1:
                    setLocal("ro");
                    item = 1;
                    languageTextView.setText(languages[item]);
                    langImage.setImageResource(R.drawable.romania_flag);
                    reset();
                    break;
                case 2:
                    setLocal("da");
                    item = 2;
                    languageTextView.setText(languages[item]);
                    langImage.setImageResource(R.drawable.denmark_flag);
                    reset();
                    break;
                case 3:
                    setLocal("ba");
                    item = 3;
                    languageTextView.setText(languages[item]);
                    langImage.setImageResource(R.drawable.russia_flag);
                    reset();
                    break;
            }

            dialog.dismiss();
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocal(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration conf = new Configuration();
        conf.locale = locale;
        getContext().getResources().updateConfiguration(conf, getContext().getResources().getDisplayMetrics());
        //Save changes to shared preferences
        SharedPreferences prefs = getContext().getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    public void loadLocal() {
        SharedPreferences prefs = getContext().getSharedPreferences("Settings", MODE_PRIVATE);
        String lang = prefs.getString("My_Lang", "");
        setLocal(lang);
        String[] languages = {"English", "Română", "Dansk", "Русский "};

        switch (lang) {
            case "en":
                item = 0;
                languageTextView.setText(languages[item]);
                langImage.setImageResource(R.drawable.usa_flag);
                break;
            case "ro":
                item = 1;
                languageTextView.setText(languages[item]);
                langImage.setImageResource(R.drawable.romania_flag);
                break;
            case "da":
                item = 2;
                languageTextView.setText(languages[item]);
                langImage.setImageResource(R.drawable.denmark_flag);
                break;
            case "ba":
                item = 3;
                languageTextView.setText(languages[item]);
                langImage.setImageResource(R.drawable.russia_flag);
                break;
            default:
                item = 0;
                languageTextView.setText("");
                break;
        }
    }

    public void reset() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }
}