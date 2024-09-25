package com.calidhonia.goanywhere;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class SearchRidesActivity extends AppCompatActivity {

    private AutoCompleteTextView fromLocation, toLocation;
    private Button selectDate, searchRidesButton;
    private EditText numPassengers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_rides);

        // Initialize Places API
        Places.initialize(getApplicationContext(), "AIzaSyCZ333rAnp_GUksFja4yGyiUsl0RYmLF9Q");

        fromLocation = findViewById(R.id.from_location);
        toLocation = findViewById(R.id.to_location);
        selectDate = findViewById(R.id.select_date);
        numPassengers = findViewById(R.id.num_passengers);
        searchRidesButton = findViewById(R.id.search_rides_button);

        // Handle 'From' Location Autocomplete
        fromLocation.setOnClickListener(v -> launchAutocomplete(1));

        // Handle 'To' Location Autocomplete
        toLocation.setOnClickListener(v -> launchAutocomplete(2));
    }

    // Method to launch the Places Autocomplete for From and To fields
    private void launchAutocomplete(int requestCode) {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(this);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            if (requestCode == 1) {
                fromLocation.setText(place.getName());  // Set the selected place for "From"
            } else if (requestCode == 2) {
                toLocation.setText(place.getName());    // Set the selected place for "To"
            }
        }
    }
}
