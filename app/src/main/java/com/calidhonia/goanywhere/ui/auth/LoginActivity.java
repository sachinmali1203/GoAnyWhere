package com.calidhonia.goanywhere.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.calidhonia.goanywhere.R;
import com.calidhonia.goanywhere.ui.homepage.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin, buttonSignUp;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize SharedPreferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Initialize UI elements
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        // AuthStateListener to handle automatic login
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

            if (user != null && isLoggedIn) {
                // User is signed in, redirect to HomeActivity
                redirectToHome();
            }
        };

        // Set OnClickListener for Login Button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input from EditTexts
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Basic validation check
                if (TextUtils.isEmpty(username)) {
                    editTextUsername.setError("Please enter your email");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Please enter your password");
                    return;
                }

                // Perform Firebase Authentication
                mAuth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Save user state in SharedPreferences
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean("isLoggedIn", true);
                                    editor.apply();

                                    // Show success message
                                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                    // Redirect to HomeActivity
                                    redirectToHome();
                                } else {
                                    // If sign-in fails, display a message to the user
                                    Toast.makeText(LoginActivity.this, "Authentication Failed: " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Set OnClickListener for Sign Up Button
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to a Sign-Up Activity (if you have one)
                Toast.makeText(LoginActivity.this, "Sign Up clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Add AuthStateListener to check if user is logged in
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Remove AuthStateListener when activity is stopped
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    // Method to redirect to HomeActivity
    private void redirectToHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the back stack
        startActivity(intent);
        finish();
    }
}
