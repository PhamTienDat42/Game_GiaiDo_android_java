package com.example.game_giaido;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
//        GoogleMap.OnMyLocationClickListener{
public class MapsActivity extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {
    FrameLayout map;
    GoogleMap gMap;
    Location currentLocation;
    Marker marker;
    FusedLocationProviderClient fusedClient;
    private static final int REQUEST_CODE = 101;
    SearchView searchView;
    Button ButtonQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //map = findViewById(R.id.map);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();

//        ButtonQuit = findViewById(R.id.btnquit);
//        ButtonQuit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MapsActivity.this, ProfileActivity.class);
//                startActivity(intent);
//            }
//        });

        fusedClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String loc = searchView.getQuery().toString();
                if (loc == null) {
                    Toast.makeText(MapsActivity.this, "Location Not Found", Toast.LENGTH_SHORT).show();
                } else {
                    Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    try {
                        List<Address> addressList = geocoder.getFromLocationName(loc, 1);
                        if (addressList.size() > 0) {
                            LatLng latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                            if (marker != null) {
                                marker.remove();
                            }
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(loc);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                            gMap.animateCamera(cameraUpdate);
                            marker = gMap.addMarker(markerOptions);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    // Phương thức cập nhật hướng trên Google Map
    private void updateMapBearing(float bearing) {
        if (gMap != null) {
            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                    new CameraPosition.Builder().target(gMap.getCameraPosition().target)
                            .zoom(gMap.getCameraPosition().zoom)
                            .bearing(bearing)
                            .tilt(gMap.getCameraPosition().tilt)
                            .build()));
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    currentLocation = location;
                    //Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        this.gMap = googleMap;
//        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("My Current Location");
//        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
//        googleMap.addMarker(markerOptions);
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gMap = googleMap;

        // Bật My Location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //gMap.setMyLocationEnabled(true);

        // Bật Zoom Controls
        //gMap.getUiSettings().setZoomControlsEnabled(true);

        // Enable the compass
        googleMap.getUiSettings().setCompassEnabled(true);

//        LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions().position(currentLatLng).title("My Current Location");
//        googleMap.animateCamera(CameraUpdateFactory.newLatLng(currentLatLng));
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
//        googleMap.addMarker(markerOptions);
//
//
//        // Tạo thêm 3 marker ở các vị trí cách 500m
//        createMarkersAroundCurrentLocation(googleMap);
        if (currentLocation != null) {
            LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(currentLatLng).title("My Current Location");
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(currentLatLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
            googleMap.addMarker(markerOptions);

            // Tạo thêm 3 marker ở các vị trí cách 500m
            createMarkersAroundCurrentLocation(googleMap);
        } else {
            // Xử lý trường hợp currentLocation là null, có thể thông báo lỗi hoặc thực hiện hành động phù hợp
            Toast.makeText(this,"Khong tim duoc current location!", Toast.LENGTH_LONG);
        }
    }

    private BitmapDescriptor bitmapDescriptor(Context context, int vectoerResId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context,vectoerResId);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void createMarkersAroundCurrentLocation(GoogleMap googleMap) {
        if (currentLocation == null) {
            return;
        }

        // Xác định vị trí hiện tại
        LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        //LatLng currentLatLng = new LatLng(20.984350, 105.841360);
        // Tính toán khoảng cách 500m theo các hướng khác nhau (0, 90, 180, 270 độ)
//        Random random = new Random();
//
//        LatLng nearbyLatLng1 = calculateLatLng(currentLatLng, 0.5, random.nextInt(360));
//        LatLng nearbyLatLng2 = calculateLatLng(currentLatLng, 0.5, random.nextInt(360));
//        LatLng nearbyLatLng3 = calculateLatLng(currentLatLng, 0.5, random.nextInt(360));
        LatLng nearbyLatLng1 = calculateLatLng(currentLatLng, 0.5, 0);
        LatLng nearbyLatLng2 = calculateLatLng(currentLatLng, 0.5, 90);
        LatLng nearbyLatLng3 = calculateLatLng(currentLatLng, 0.5, 180);

        LatLng quiz = new LatLng(nearbyLatLng1.latitude, nearbyLatLng1.longitude);
        googleMap.addMarker(new MarkerOptions().position(quiz).title("Vị trí câu đố")
                .icon(bitmapDescriptor(getApplicationContext(),R.drawable.hopcauhoi1)));
        LatLng quiz1 = new LatLng(nearbyLatLng2.latitude, nearbyLatLng2.longitude);
        googleMap.addMarker(new MarkerOptions().position(quiz1).title("Vị trí câu đố")
                .icon(bitmapDescriptor(getApplicationContext(),R.drawable.hopcauhoi1)));
        LatLng quiz2 = new LatLng(nearbyLatLng3.latitude, nearbyLatLng3.longitude);
        googleMap.addMarker(new MarkerOptions().position(quiz2).title("Vị trí câu đố")
                .icon(bitmapDescriptor(getApplicationContext(),R.drawable.hopcauhoi1)));
        // Tạo marker cho các vị trí tính toán được
//        addMarker(nearbyLatLng1, "Location 1");
//        addMarker(nearbyLatLng2, "Location 2");
//        addMarker(nearbyLatLng3, "Location 3");
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                    openQuizDialog(Gravity.CENTER);
                return false;
            }
        });
    }

    private LatLng calculateLatLng(LatLng source, double distance, float angle) {
        double radiusEarth = 6371; // Đường kính trái đất trong km

        double lat1 = Math.toRadians(source.latitude);
        double lon1 = Math.toRadians(source.longitude);
        double angularDistance = distance / radiusEarth;

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(angularDistance) +
                Math.cos(lat1) * Math.sin(angularDistance) * Math.cos(Math.toRadians(angle)));
        double lon2 = lon1 + Math.atan2(Math.sin(Math.toRadians(angle)) *
                        Math.sin(angularDistance) * Math.cos(lat1),
                Math.cos(angularDistance) - Math.sin(lat1) * Math.sin(lat2));

        lat2 = Math.toDegrees(lat2);
        lon2 = Math.toDegrees(lon2);

        return new LatLng(lat2, lon2);
    }

    private void addMarker(LatLng latLng, String title) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        this.gMap.addMarker(markerOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Đang di chuyển đến vị trí của bạn!", Toast.LENGTH_SHORT).show();
        // Trả về false để chúng ta không sử dụng sự kiện và hành vi mặc định vẫn xảy ra
        // (Camera hoạt hình theo vị trí hiện tại của người dùng).
        return false;
    }

    private void openQuizDialog(int gravity){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_quiz);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if (Gravity.CENTER == gravity){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);
        }
        dialog.show();
    }
}