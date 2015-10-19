package csd.jt.capsmobile;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Intent intent = getIntent();
        String address = intent.getStringExtra("address");
        String str = intent.getStringExtra("street");
        String zip = intent.getStringExtra("zipcode");
        String area = intent.getStringExtra("area");

        String addToGeocode = address + ", " + str + ", " + zip + ", " + area + ", Thessaloniki";

        // latitude and longitude
        double latitude = 0;
        double longitude = 0;

        Geocoder gc = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> list = gc.getFromLocationName(addToGeocode, 1);
            Address add = null;
            if (!list.isEmpty()) {
                add = list.get(0);
                latitude = add.getLatitude();
                longitude = add.getLongitude();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Δράση");
        map.addMarker(marker);

        LatLng ll = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
    }
}
