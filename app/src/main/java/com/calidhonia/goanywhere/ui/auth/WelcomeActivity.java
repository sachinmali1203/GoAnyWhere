package com.calidhonia.goanywhere.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.calidhonia.goanywhere.R;
import com.calidhonia.goanywhere.ui.homepage.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if it's the first time the app is launched
        SharedPreferences preferences = getSharedPreferences("GoAnywherePrefs", MODE_PRIVATE);
        boolean isFirstLaunch = preferences.getBoolean("firstLaunch", true);

        // If not the first launch, skip welcome and check authentication
        if (!isFirstLaunch) {
            checkAuthentication();
        } else {
            setContentView(R.layout.activity_welcome);

            // Update preference so that welcome screen is not shown again
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstLaunch", false);
            editor.apply();

            // Move to Login or Home after user clicks 'Get Started'
            findViewById(R.id.button_get_started).setOnClickListener(v -> checkAuthentication());
        }
    }

    // Check user session via Firebase Authentication
    private void checkAuthentication() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // User is logged in, go to Home Activity
            startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
        } else {
            // User is not logged in, go to Login Activity
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        }
        finish();
    }
}
