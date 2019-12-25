package com.sustainablefood.com;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.squareup.picasso.Picasso;
import com.sustainablefood.com.Objects.Location;
import com.sustainablefood.com.Tasks.SessionManager;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Use the LocationComponent to easily add a device location "puck" to a Mapbox map.
 */

//TO-DO: Locationa username ve telefon eklenecek, markerlara click listener eklenecek.
public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback,  PermissionsListener{

    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private MarkerViewManager markerViewManager;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference locationReference = firebaseDatabase.getReference("Location");

    private View markerView;
    private ImageView markerViewImage;
    private TextView markerViewName;
    private TextView markerViewNotes;
    private TextView markerViewPhone;
    private Button markerViewButton;
    private FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_main);

        markerView = findViewById(R.id.marker_view_layout_main);
        markerViewImage = findViewById(R.id.activity_main_image);
        markerViewName = findViewById(R.id.activity_main_username);
        markerViewNotes = findViewById(R.id.activity_main_notes);
        markerViewPhone = findViewById(R.id.activity_main_phone);
        markerViewButton = findViewById(R.id.activity_main_button);
        floatingActionButton = findViewById(R.id.floating_action_button);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        MainActivity.this.mapboxMap = mapboxMap;

        mapboxMap.getUiSettings().setAttributionEnabled(false);
        mapboxMap.getUiSettings().setLogoEnabled(false);

        CameraPosition currentCameraPosition = mapboxMap.getCameraPosition();
        CameraPosition position = new CameraPosition.Builder()
                .target(currentCameraPosition.target)
                .zoom(14)
                .tilt(currentCameraPosition.tilt)
                .build();
        mapboxMap.setCameraPosition(position);

        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @Override
            public boolean onMapClick(@NonNull LatLng point) {
                floatingActionButton.show();
                markerView.setVisibility(View.GONE);
                return true;
            }
        });

        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjerxnqt3cgvp2rmyuxbeqme7"),
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                    }
                });


        locationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()!=0){
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        Location location = snapshot.getValue(Location.class);
                        double latitude = location.latitude;
                        double longtitude = location.longitude;

                        int category = location.category;
                        MarkerViewManager markerViewManager = new MarkerViewManager(mapView, mapboxMap);
                        Bitmap bm;
                        if(category == 5){
                             bm = BitmapFactory.decodeResource(getResources(), R.drawable.bread_marker);
                        }
                        else if(category == 2){
                             bm = BitmapFactory.decodeResource(getResources(), R.drawable.dairy_marker);
                        }
                        else if(category == 3){
                             bm = BitmapFactory.decodeResource(getResources(), R.drawable.fruit_marker);
                        }
                        else if(category == 4){
                             bm = BitmapFactory.decodeResource(getResources(), R.drawable.bavage_marker);
                        }
                        else{
                             bm = BitmapFactory.decodeResource(getResources(), R.drawable.meal_marker);
                        }

                        bm = Bitmap.createScaledBitmap(bm, 160, 160, false);
                        IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
                        Icon icon = iconFactory.fromBitmap(bm);


                        Marker marker = mapboxMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longtitude))
                                .title(snapshot.getKey())
                                .icon(icon));


                        mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(@NonNull Marker marker) {
                                String key = marker.getTitle();
                                locationReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        final Location selectedLocation = dataSnapshot.getValue(Location.class);
                                        markerViewName.setText(selectedLocation.userName);
                                        markerViewPhone.setText(selectedLocation.userPhone);
                                        markerViewNotes.setText(selectedLocation.note);
                                        Picasso.with(getApplicationContext()).load(selectedLocation.image).into(markerViewImage);
                                        markerView.setVisibility(View.VISIBLE);
                                        floatingActionButton.hide();

                                        if(!selectedLocation.userId.equals(firebaseAuth.getCurrentUser().getUid().toString())) {
                                            markerViewButton.setVisibility(View.VISIBLE);
                                            markerViewButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Toast.makeText(getApplicationContext(), selectedLocation.userId, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        else{
                                            markerViewButton.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                return true;
                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {

                    // Custom map style has been loaded and map is now ready


                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int i = item.getItemId();
        if(i==R.id.action_logout) {
            firebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            SessionManager sessionManager = new SessionManager(this);
            sessionManager.logout();
            startActivity(intent);
        }
        else if(i==R.id.action_notification) {
            firebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            SessionManager sessionManager = new SessionManager(this);
            sessionManager.logout();
            startActivity(intent);
        }

        return true;
    }

    public void goToAddLocation(View view) {
        Intent intent = new Intent(MainActivity.this, AddLocationActivity.class);
        startActivity(intent);
    }


}