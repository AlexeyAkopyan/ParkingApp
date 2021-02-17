package com.example.parkingapp;

import android.app.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;




public class MainActivity extends AppCompatActivity implements OnClickListener, Fragment1.OnSelectedButtonListener, OnMapReadyCallback{

    public Button btn_spot;
    public Button btn_map;
    public Fragment1 frag1;
    public FragmentManager fragmentManager;
    public FragmentTransaction fTrans;
    public boolean IsDynamic;
    private GoogleMap map;
    private List<ParkingPlace> parking_book;// = new ArrayList<Object>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_spot = findViewById(R.id.btn_spot);
        btn_spot.setOnClickListener(this);
        btn_map = findViewById(R.id.btn_map);
        btn_map.setOnClickListener(this);
        parking_book = ReadParkingBook();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }
    public List<ParkingPlace> ReadParkingBook(){
        List<ParkingPlace> parking_book = new ArrayList<ParkingPlace>();
        parking_book.add(new ParkingPlace(59.930413, 30.361137, "adress_1", "wrk_hrs_1"));
        parking_book.add(new ParkingPlace(59.933686, 30.313075, "adress_2", "wrk_hrs_2"));
        parking_book.add(new ParkingPlace(59.930086, 30.344658, "adress_3", "wrk_hrs_3"));
        parking_book.add(new ParkingPlace(59.927321, 30.385752, "adress_4", "wrk_hrs_4"));
        return parking_book;
    }

    public void onClick(View v) {
        fragmentManager = getFragmentManager();
        frag1 = (Fragment1) fragmentManager.findFragmentById(R.id.frgmCont);
        IsDynamic = frag1 == null || !frag1.isVisible();
        fTrans = fragmentManager.beginTransaction();
        if (v.getId() == R.id.btn_spot && IsDynamic){
                frag1 = new Fragment1();
                fTrans.add(R.id.frgmCont, frag1);
                fTrans.commit();
        }
        if (v.getId() == R.id.btn_map) {
            Intent intent = new Intent(this, MapsActivity.class);
            //intent.p
            startActivity(intent);
        }

    }


    @Override
    public void onButtonSelected(int id) {
        switch (id){
            case R.id.button_remove: // btn_close
                fTrans = fragmentManager.beginTransaction();
                fTrans.remove(frag1);
                fTrans.commit();
                break;
            case R.id.button_reserve:
                Intent intent = new Intent(this, ActivityTwo.class);
                //intent.p
                startActivity(intent);
                break;
            default:
                break;


        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //googleMap.addMarker(new MarkerOptions()
        //        .position(new LatLng(59.930099, 30.361609))
        //        .title("Marker"));

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map = googleMap;

        //map.setOnCameraIdleListener(this);
        //map.setOnCameraMoveStartedListener(this);
        //map.setOnCameraMoveListener(this);
        //map.setOnCameraMoveCanceledListener(this);
        // [START_EXCLUDE silent]
        // We will provide our own zoom controls.
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        // Add lots of markers to the map.
        addMarkersToMap(parking_book);

        // [END_EXCLUDE]

        // Show Sydney
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(59.930099, 30.361609), 12));
//        if (!googleMap.isTrafficEnabled()){
//            googleMap.setTrafficEnabled(true);
//        }
    }
    private void addMarkersToMap(List<ParkingPlace> parking_book) {
        // Uses a colored icon.
        for(ParkingPlace place:parking_book){
            map.addMarker(new MarkerOptions()
                    .position(place.getLoc())
                    .flat(true)
                    );
        }
    }
}