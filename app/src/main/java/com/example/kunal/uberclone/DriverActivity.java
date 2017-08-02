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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DriverActivity extends AppCompatActivity {

    private static final String TAG = "DriverActivity";

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference driverRef = database.getReference("drivers");
    DatabaseReference callByRidersRef = database.getReference("callByRidersRef");

    LocationManager locationManager;
    LocationListener locationListener;

    ListView requestListView;
    ArrayList requestLists;
    ArrayAdapter adapter;

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
        setContentView(R.layout.activity_driver);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        final Driver driver = new Driver();

        requestListView = (ListView) findViewById(R.id.requestListView);
        requestLists = new ArrayList();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, requestLists);
        requestListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Button logout = (Button) findViewById(R.id.driverLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(DriverActivity.this, MainActivity.class));
                finish();
            }
        });

        driver.setAvailable(true);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                driver.setLat(location.getLatitude());
                driver.setLng(location.getLongitude());

                driverRef.child(user.getUid()).setValue(driver);

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

                if(lastKnownLocation != null) {

                    driver.setLat(lastKnownLocation.getLatitude());
                    driver.setLng(lastKnownLocation.getLongitude());

                    driverRef.child(user.getUid()).setValue(driver);

                }

            }

        }

        callByRidersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(dataSnapshot != null) {

                    MyLatLng myLatLng = dataSnapshot.getValue(MyLatLng.class);
                    Log.i(TAG, "onChildAdded: " +myLatLng.getLatitude());
                    Log.i(TAG, "onChildAdded: " +myLatLng.getLongitude());
                    requestLists.add(myLatLng.getLatitude().toString() + ", " + myLatLng.getLongitude().toString());
                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
