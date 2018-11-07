package jp.co.mo.logmylife.presentation.view.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.co.mo.logmylife.AbstractBaseActivity;
import jp.co.mo.logmylife.R;
import jp.co.mo.logmylife.common.util.CopyDialog;
import jp.co.mo.logmylife.common.util.DateUtil;
import jp.co.mo.logmylife.common.util.Logger;
import jp.co.mo.logmylife.domain.entity.map.MapPlaceData;
import jp.co.mo.logmylife.domain.usecase.MapUseCaseImpl;

public class MapActivity extends AbstractBaseActivity implements GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMapLongClickListener,
        OnMapReadyCallback,
        LocationListener,
        MapDetailDialogFragment.UpdateMarker{

    private static final String TAG = MapActivity.class.getSimpleName();

    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    private GoogleMap mMap;
    private Marker mMarker;
    private LocationManager mLocationManager;
    private MapUseCaseImpl mapUseCase;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.debug(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                MIN_TIME,
                MIN_DISTANCE,
                this);
        mapUseCase = new MapUseCaseImpl();
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
        Logger.debug(TAG, "onMapReady");
        mMap = googleMap;
        checkMapPermission();

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLongClickListener(this);

        List<MapPlaceData> mapPlaceDataList = mapUseCase.getMapPlaceDatas(this);
        if(mapPlaceDataList != null) {
            for (MapPlaceData data : mapPlaceDataList) {
                if(data == null) {
                    continue;
                }

                LatLng latLng = new LatLng(data.getLat(), data.getLng());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng)
                        .title(data.getTitle())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                Marker m = mMap.addMarker(markerOptions);
                m.setTag(data);
                mMarker = m;

            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Logger.debug(TAG, "onMarkerClick");
        mMarker = marker;
        MapPlaceData data = (MapPlaceData) marker.getTag();
        if(data != null) {
            MapPlaceData placeData = new MapPlaceData();
            placeData.setId(data.getId());
            placeData.setUserId(data.getUserId());
            placeData.setTitle(data.getTitle());
            placeData.setLat(data.getLat());
            placeData.setLng(data.getLng());
            placeData.setTypeId(data.getTypeId());
            placeData.setTypeDetailId(data.getTypeDetailId());
            placeData.setUrl(data.getUrl());
            placeData.setDetail(data.getDetail());
            placeData.setCreateDate(data.getCreateDate());
            placeData.setUpdateDate(data.getUpdateDate());

            MapDetailDialogFragment fragment = MapDetailDialogFragment.newInstance(placeData);
            android.support.v4.app.FragmentManager fm = this.getSupportFragmentManager();
            fragment.show(fm, "mapDialog");

        }

        return false;
    }

    private void checkMapPermission() {
        Logger.debug(TAG, "checkMapPermission");
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
        Logger.debug(TAG, "onMyLocationClick");
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
        Logger.debug(TAG, "onLocationChanged");
        if (location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            mMap.moveCamera(cameraUpdate);
            mLocationManager.removeUpdates(this);
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

    @Override
    public void onMapLongClick(LatLng latLng) {
        Logger.debug(TAG, "onMapLongClick");
        LatLng snowqualmie = new LatLng(latLng.latitude, latLng.longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(snowqualmie)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        MapPlaceData placeData = new MapPlaceData();
        placeData.setLat(latLng.latitude);
        placeData.setLng(latLng.longitude);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FORMAT_YYYYMMDDHHMMSS);
            String date = sdf.format(new Date());
            placeData.setCreateDate(date);
            placeData.setUpdateDate(date);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        Marker m = mMap.addMarker(markerOptions);
        mMarker = m;
        m.setTag(placeData);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(snowqualmie));

        MapDetailDialogFragment fragment = MapDetailDialogFragment.newInstance(placeData);
        android.support.v4.app.FragmentManager fm = this.getSupportFragmentManager();
        fragment.show(fm, "mapDialog");

    }

    @Override
    public void onActivityResult (
            int requestCode,
            int resultCode,
            Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void updateMarker(MapPlaceData mapPlaceData) {
        // マーカーにセットし直す。
        if(mapPlaceData != null) {
            mMarker.setTitle(mapPlaceData.getTitle());
            mMarker.setTag(mapPlaceData);
        }
    }

    @Override
    public void deleteMarker() {
        // TODO: delete marker
    }
}
