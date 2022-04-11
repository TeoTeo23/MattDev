package com.example.mattdev;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap gmap;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        Button coordinatesPush = findViewById(R.id.coordinatesLocator);
        coordinatesPush.setOnClickListener(view -> {
            Server google = new Server("Google");
            Server openweather = new Server("OpenWeather");

            EditText choice = findViewById(R.id.cityInput);
            if(choice.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "Please, insert a location", Toast.LENGTH_SHORT).show();
                return;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // async request
                    String p = choice.getText().toString();
                    Place plc = google.getCoordinates(p);
                    google.setCallStatus(true);
                    String temperature = openweather.requestTemperature(p, "xml");
                    openweather.setCallStatus(true);
                    view.post(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            // Modify interface
                            if(plc == null){
                                Toast.makeText(getApplicationContext(), "Invalid location", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(temperature.equals("")){
                                Toast.makeText(getApplicationContext(), "Error while parsing temperature", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Double finalTemperature = Double.parseDouble(temperature) - 273.15; // Convert from K to °C
                            plc.setTemperature(finalTemperature);
                            TextView laOut = findViewById(R.id.latOut);
                            TextView loOut = findViewById(R.id.lonOut);
                            TextView tempOut = findViewById(R.id.temperatureOutput);
                            TextView serversOut = findViewById(R.id.serversInvOut);
                            // laOut.setText(String.valueOf(plc.getLatitude()));
                            // loOut.setText(String.valueOf(plc.getLongitude()));

                            float zoomLevel = 16.0f;
                            if(!(marker == null))
                                marker.remove();
                            LatLng place = new LatLng(plc.getLatitude(), plc.getLongitude());
                            gmap.addMarker(new MarkerOptions().position(place).title(p));
                            gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, zoomLevel));
                            /* gmap.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds( new LatLng(plc.getBounds().getLatSW(),
                                    plc.getBounds().getLngSW()),
                                    new LatLng(plc.getBounds().getLatNE(), plc.getBounds().getLngNE())), 10));
                            marker = gmap.addMarker(new MarkerOptions().position(new LatLng(plc.getLatitude(), plc.getLongitude())).title(p));
                             */
                            laOut.setText(String.format(Locale.ENGLISH, "%f", plc.getLatitude()));
                            loOut.setText(String.format(Locale.ENGLISH, "%f", plc.getLongitude()));
                            tempOut.setText(String.format(Locale.ENGLISH, "%f", plc.getTemperature())); // put finalTemperature in TextView box.
                            if(google.calledServer())
                                serversOut.setText(google.getInstance());
                            else if(openweather.calledServer())
                                serversOut.setText(openweather.getInstance());
                            if(google.calledServer() && openweather.calledServer())
                                serversOut.setText(google.getInstance() + " " + openweather.getInstance());
                        }
                    });
                }
            }).start();
        });
        Button reset = findViewById(R.id.resetPush);
        reset.setOnClickListener(view -> {
            EditText ctyR = findViewById(R.id.cityInput);

            TextView latR = findViewById(R.id.latOut);
            TextView lonR = findViewById(R.id.lonOut);
            TextView tmpR = findViewById(R.id.temperatureOutput);
            TextView srvR = findViewById(R.id.serversInvOut);

            ctyR.setText("");
            latR.setText("");
            lonR.setText("");
            tmpR.setText("");
            srvR.setText("");
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap){
        gmap = googleMap;
    }
}