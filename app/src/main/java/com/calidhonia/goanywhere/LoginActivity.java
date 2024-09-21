package com.calidhonia.goanywhere;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    // Declare UI elements
    private EditText emailEditText, passwordEditText;
    private Button loginButton, registerButton;

    // Declare Firebase Auth instance
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Set up click listeners with logging
        loginButton.setOnClickListener(view -> {
            Log.d("LoginActivity", "Login button clicked");
            loginUser();
        });

        registerButton.setOnClickListener(view -> {
            Log.d("LoginActivity", "Register button clicked");
            registerUser();
        });
    }

    private void loginUser() {
        Log.d("LoginActivity", "Login user method called");

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required.");
            Log.d("LoginActivity", "Email is empty");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required.");
            Log.d("LoginActivity", "Password is empty");
            return;
        }

        // Authenticate user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-in success
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(LoginActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
                        Log.d("LoginActivity", "Login successful");

                        // Navigate to SuccessActivity
                        Intent intent = new Intent(LoginActivity.this, SuccessActivity.class);
                        intent.putExtra("message", "Login successful");
                        startActivity(intent);
                        finish();
                    } else {
                        // If sign-in fails
                        Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.d("LoginActivity", "Authentication failed: " + task.getException().getMessage());

                        // Navigate to ErrorActivity
                        Intent intent = new Intent(LoginActivity.this, ErrorActivity.class);
                        intent.putExtra("message", "Authentication failed: " + task.getException().getMessage());
                        startActivity(intent);
                    }
                });
    }

    private void registerUser() {
        Log.d("LoginActivity", "Register user method called");

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required.");
            Log.d("LoginActivity", "Email is empty");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required.");
            Log.d("LoginActivity", "Password is empty");
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters.");
            Log.d("LoginActivity", "Password is too short");
            return;
        }

        // Create user with Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("LoginActivity", "Registration successful");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(LoginActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();

                        // Navigate to SuccessActivity after registration
                        Intent intent = new Intent(LoginActivity.this, SuccessActivity.class);
                        intent.putExtra("message", "Registration successful");
                        startActivity(intent);
                        finish();
                    } else {
                        Log.d("LoginActivity", "Registration failed: " + task.getException().getMessage());
                        Toast.makeText(LoginActivity.this, "Registration failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();

                        // Navigate to ErrorActivity with error message
                        Intent intent = new Intent(LoginActivity.this, ErrorActivity.class);
                        intent.putExtra("message", "Registration failed: " + task.getException().getMessage());
                        startActivity(intent);
                    }
                });
    }
}
