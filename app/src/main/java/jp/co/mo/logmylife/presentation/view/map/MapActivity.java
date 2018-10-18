package jp.co.mo.logmylife.presentation.view.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import jp.co.mo.logmylife.AbstractBaseActivity;
import jp.co.mo.logmylife.R;
import jp.co.mo.logmylife.common.util.CopyDialog;
import jp.co.mo.logmylife.common.util.Logger;
import jp.co.mo.logmylife.domain.entity.map.InfoWindowData;

public class MapActivity extends AbstractBaseActivity implements GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        LocationListener{

    private static final String TAG = MapActivity.class.getSimpleName();

    private GoogleMap mMap;

    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    private LocationManager mLocationManager;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                MIN_TIME,
                MIN_DISTANCE,
                this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        checkMapPermission();

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                showAddressDialog(latLng.latitude, latLng.longitude);
            }
        });

//        // TODO: get info from API(DB).
//        LatLng snowqualmie = new LatLng(47.5287132, -121.8253906);
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(snowqualmie)
//                .title("Snowqualmie Falls")
//                .snippet("Snoqualmie Falls is located 25 miles east of Seattle.")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//
//        InfoWindowData info = new InfoWindowData();
//        info.setImage("snowqualmie");
//        info.setHotel("Hotel : excellent hotels available");
//        info.setFood("Food : all types of restaurants available");
//        info.setTransport("Reach the site by bus, car and train.");
//
//        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
//        mMap.setInfoWindowAdapter(customInfoWindow);
//
//        Marker m = mMap.addMarker(markerOptions);
//        m.setTag(info);
//        m.showInfoWindow();
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(snowqualmie));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.e(TAG, "onMarkerClick");
        InfoWindowData data = (InfoWindowData) marker.getTag();
        if(data != null) {
            InfoWindowData info = new InfoWindowData();
            info.setImage(data.getImage());
            info.setHotel(data.getHotel());
            info.setFood(data.getFood());
            info.setTransport(data.getTransport());
            MapInfoDialog dialog = new MapInfoDialog(this, info);
            dialog.show();


            Logger.debug(TAG, "data.getTransport()" + data.getTransport());
            Logger.debug(TAG, "data.getFood()" + data.getFood());
            Logger.debug(TAG, "data.getHotel()" + data.getHotel());
            Logger.debug(TAG, "data.getImage()" + data.getImage());
        }

        return false;
    }

    private void checkMapPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
            mMap.getUiSettings().setAllGesturesEnabled(true);
        } else {
            // TODO: Show rationale and request permission.
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        showAddressDialog(location.getLatitude(), location.getLongitude());
    }

    private void showAddressDialog(double lat, double lon) {
        StringBuilder sb = new StringBuilder();
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocation(lat, lon, 1);
            if (list != null && !list.isEmpty()) {
                for (Address addr : list) {
                    int idx = addr.getMaxAddressLineIndex();
                    for (int i = 0; i <= idx; i++) {
                        sb.append(addr.getAddressLine(i));
                    }
                }
            }
            CopyDialog cd = new CopyDialog(this, sb.toString());
            cd.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            mMap.animateCamera(cameraUpdate);
            mLocationManager.removeUpdates(this);

            // TODO: do in MapReady(get from API(DB)).
            LatLng snowqualmie = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(snowqualmie));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(snowqualmie)
                    .title("Snowqualmie Falls")
                    .snippet("Snoqualmie Falls is located 25 miles east of Seattle.")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            InfoWindowData info = new InfoWindowData();
            info.setImage("snowqualmie");
            info.setHotel("Hotel : excellent hotels available");
            info.setFood("Food : all types of restaurants available");
            info.setTransport("Reach the site by bus, car and train.");
            MapInfoDialog dialog = new MapInfoDialog(this, info);
            dialog.show();

            Marker m = mMap.addMarker(markerOptions);
            m.setTag(info);
//            m.showInfoWindow();

        }
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
}
