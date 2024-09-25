package com.calidhonia.goanywhere;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int AUTOCOMPLETE_REQUEST_CODE_FROM = 1;
    private static final int AUTOCOMPLETE_REQUEST_CODE_TO = 2;
    private static final String TAG = "MainActivity";

    private GoogleMap mMap;
    private TextInputEditText fromLocation, toLocation, datePicker;
    private LatLng fromLatLng, toLatLng;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Google Places API
        Places.initialize(getApplicationContext(), "AIzaSyCZ333rAnp_GUksFja4yGyiUsl0RYmLF9Q");

        // Initialize UI elements
        fromLocation = findViewById(R.id.from_location);
        toLocation = findViewById(R.id.to_location);
        datePicker = findViewById(R.id.date_picker);
        searchButton = findViewById(R.id.search_rides_button);

        // Initialize the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Set up DatePicker
        datePicker.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        datePicker.setText(date);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Set up Google Places Autocomplete
        fromLocation.setOnClickListener(view -> openAutocomplete(AUTOCOMPLETE_REQUEST_CODE_FROM));
        toLocation.setOnClickListener(view -> openAutocomplete(AUTOCOMPLETE_REQUEST_CODE_TO));

        // Handle search button click to draw a route
        searchButton.setOnClickListener(v -> {
            if (fromLatLng != null && toLatLng != null) {
                fetchRoute(fromLatLng, toLatLng);
            } else {
                Toast.makeText(MainActivity.this, "Please select both locations", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize bottom navigation (if needed)
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            // Handle bottom navigation actions here
            return true;
        });
    }

    // Initialize the map when ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng defaultLocation = new LatLng(37.7749, -122.4194); // Example: San Francisco
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
    }

    // Open Google Places Autocomplete
    private void openAutocomplete(int requestCode) {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this);
        startActivityForResult(intent, requestCode);
    }

    // Handle the result from Google Places Autocomplete
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            LatLng latLng = place.getLatLng();

            if (requestCode == AUTOCOMPLETE_REQUEST_CODE_FROM) {
                fromLocation.setText(place.getName());
                fromLatLng = latLng;
            } else if (requestCode == AUTOCOMPLETE_REQUEST_CODE_TO) {
                toLocation.setText(place.getName());
                toLatLng = latLng;
            }

            if (latLng != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            }
        }
    }

    // Fetch route from Google Directions API
    private void fetchRoute(LatLng from, LatLng to) {
        String origin = from.latitude + "," + from.longitude;
        String destination = to.latitude + "," + to.longitude;
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + origin +
                "&destination=" + destination + "&key=AIzaSyCZ333rAnp_GUksFja4yGyiUsl0RYmLF9Q";

        // Start async task to fetch directions
        new FetchRouteTask().execute(url);
    }

    // AsyncTask to perform network request to Directions API
    private class FetchRouteTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();

            } catch (Exception e) {
                Log.e(TAG, "Error fetching directions: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                parseDirections(result);
            }
        }
    }

    // Parse the directions response and draw route on the map
    private void parseDirections(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray routes = jsonObject.getJSONArray("routes");

            if (routes.length() > 0) {
                JSONObject route = routes.getJSONObject(0);
                JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                String points = overviewPolyline.getString("points");

                List<LatLng> decodedPath = PolyUtil.decode(points);

                runOnUiThread(() -> {
                    mMap.clear();
                    mMap.addPolyline(new PolylineOptions().addAll(decodedPath).width(10).color(0xFF0000FF));
                });
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing directions: " + e.getMessage());
        }
    }
}
