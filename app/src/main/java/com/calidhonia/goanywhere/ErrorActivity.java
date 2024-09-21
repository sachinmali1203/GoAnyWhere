package com.calidhonia.goanywhere;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        // Get the message passed from LoginActivity
        String message = getIntent().getStringExtra("message");

        // Set the message in a TextView
        TextView errorMessage = findViewById(R.id.errorMessage);
        errorMessage.setText(message);
    }
}
