package com.example.kunal.uberclone;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RiderActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    private static final String TAG = "RiderActivity";
    LatLng latLng;

    boolean callUber = true;

    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference riderRef = database.getReference("riders");
    DatabaseReference callByRidersRef = database.getReference("callByRidersRef");

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mAuth = FirebaseAuth.getInstance();

        final Button bookUber = (Button) findViewById(R.id.bookUber);
        bookUber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callUber) {
                    callByRidersRef.child(mAuth.getCurrentUser().getUid()).setValue(latLng);
                    callUber = false;
                    bookUber.setText("Cancel Uber");
                }
                else {
                    callByRidersRef.child(mAuth.getCurrentUser().getUid()).removeValue();
                    callUber = true;
                    bookUber.setText("Call Uber");
                }

            }
        });

        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = mAuth.getCurrentUser();

                riderRef.child(mAuth.getCurrentUser().getUid()).removeValue();
                mAuth.signOut();
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onComplete: Account deleted!!");
                    }
                });

                startActivity(new Intent(RiderActivity.this, MainActivity.class));
                finish();

            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                mMap.clear();

                latLng = new LatLng(location.getLatitude(), location.getLongitude());

                LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(myLocation).title("Marker in your location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));

                riderRef.child(mAuth.getCurrentUser().getUid()).setValue(myLocation);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        // If device is running SDK < 23

        if (Build.VERSION.SDK_INT < 23) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } else {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // ask for permission

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);


            } else {

                // we have permission!

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                mMap.clear();

                if(lastKnownLocation != null) {

                    latLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

                    LatLng myLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(myLocation).title("Marker in your location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));

                    riderRef.child(mAuth.getCurrentUser().getUid()).setValue(myLocation);
                }

            }

        }

    }
}
