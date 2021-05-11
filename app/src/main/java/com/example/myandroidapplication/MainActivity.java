package com.example.myandroidapplication;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myandroidapplication.ViewModel.HomeViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HomeViewModel viewModel;
    private TextView welcomeMessage;
    private Handler handler;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        handler = new Handler();

        setContentView(R.layout.splash_screen);
        getWindow().setStatusBarColor(getColor(R.color.white));
        welcomeMessage = findViewById(R.id.welcomeMessage);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        checkIfSignedIn();
    }

    /**
     * If user is not authenticated, send him to SignIn first.
     * Else send him to Home
     */
    private void checkIfSignedIn() {
        viewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                String message = "Welcome " + user.getDisplayName();
                Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                welcomeMessage.setText(message);
                welcomeMessage.setAnimation(fadeIn);

                startHomeFragment();
            } else {
                startLoginActivity();
            }
        });
    }

    /**
     * If user is authenticated the main activity content view is changed to main
     */
    private void startHomeFragment() {
        handler.postDelayed((Runnable) () -> {
            startActivity(new Intent(this, HomeActivity.class));
            viewModel.init();
            finish();
        }, 2000);

    }

    private void startLoginActivity() {
        handler.postDelayed((Runnable) () -> {

            handler.postDelayed((Runnable) () -> {
                startActivity(new Intent(this, SignInActivity.class));
                finish();
            }, 1000);

//            setContentView(R.layout.sing_in_fragment);
//
//            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                    .requestIdToken(getString(R.string.default_web_client_id))
//                    .requestEmail()
//                    .build();
//
//            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//
//            Button button = findViewById(R.id.sign_in_btn);
//
//            button.setOnClickListener(v -> {
//                signIn();
//            });
//
//            getWindow().setStatusBarColor(getColor(R.color.main));
        }, 1000);
    }

    private void signIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ic_google)
                .build();

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInRequest(resultCode);
        }
    }

    private void handleSignInRequest(int resultCode) {
        if (resultCode == RESULT_OK) {
            startHomeFragment();
        } else
            Toast.makeText(this, "SIGN IN CANCELLED", Toast.LENGTH_SHORT).show();
    }

}