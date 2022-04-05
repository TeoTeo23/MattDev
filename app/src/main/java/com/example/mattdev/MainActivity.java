package com.example.mattdev;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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
        coordinatesPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleServer gs = new GoogleServer();
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
                        Place plc = gs.getCoordinates(p);

                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                // Modify interface
                                if(plc == null){
                                    Toast.makeText(getApplicationContext(), "Invalid location", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                TextView laOut = findViewById(R.id.latOut);
                                TextView loOut = findViewById(R.id.lonOut);

                                //laOut.setText(String.valueOf(plc.getLatitude()));
                                // loOut.setText(String.valueOf(plc.getLongitude()));

                                float zoomLevel = 16.0f;
                                if(!(marker == null))
                                    marker.remove();
                                LatLng place = new LatLng(plc.getLatitude(), plc.getLongitude());
                                gmap.addMarker(new MarkerOptions().position(place).title(p));
                                gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, zoomLevel));
                                /*gmap.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds( new LatLng(plc.getBounds().getLatSW(),
                                        plc.getBounds().getLngSW()),
                                        new LatLng(plc.getBounds().getLatNE(), plc.getBounds().getLngNE())), 10));
                                marker = gmap.addMarker(new MarkerOptions().position(new LatLng(plc.getLatitude(), plc.getLongitude())).title(p));
                                 */
                                laOut.setText(String.format(Locale.ENGLISH, "%f", plc.getLatitude()));
                                loOut.setText(String.format(Locale.ENGLISH, "%f", plc.getLongitude()));
                            }
                        });
                    }
                }).start();
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap){
        gmap = googleMap;
    }
}