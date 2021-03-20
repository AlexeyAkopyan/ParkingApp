package com.example.parkingapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements OnClickListener, ParkInfoFragment.OnSelectedButtonListener, ParkInfoFragment.SendTime, OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    public Button btn_spot;
    public Button btn_map;
    public ParkInfoFragment park_info_frag;
    public FragmentManager fragmentManager;
    public FragmentTransaction fTrans;
    public boolean IsDynamic;
    private GoogleMap map;
    private LinearLayout map_layout;
    public List<ParkingPlace> parking_book;
    public List<String> selected_time;
    public ParkingPlace selected_place = null;// = new ArrayList<Object>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parking_book = ReadParkingBook();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


//        map_layout = findViewById(R.id.MapLayout);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
//        map_layout.setLayoutParams(params);



    }
     List<ParkingPlace> ReadParkingBook(){
        List<ParkingPlace> parking_book = new ArrayList<ParkingPlace>();
        parking_book.add(new ParkingPlace(59.930413, 30.361137, "пр. Пятилеток, 1, лит. А", "wrk_hrs_1"));
        parking_book.add(new ParkingPlace(59.933686, 30.313075, "наб. р. Мойки, 75-79", "wrk_hrs_2"));
        parking_book.add(new ParkingPlace(59.930086, 30.344658, "ул. Рубинштейна, 11", "wrk_hrs_3"));
        parking_book.add(new ParkingPlace(59.927321, 30.385752, "пл. Восстания", "wrk_hrs_4"));

        return parking_book;
    }

    @Override
    public void sendData(List<String> message) {
        selected_time = message;

    }

    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {

        Log.i("TAG", "in onBackPressed");
        park_info_frag = (ParkInfoFragment) fragmentManager.findFragmentById(R.id.frgmCont);
        Log.i("TAG1", String.valueOf((park_info_frag == null)));
        Log.i("TAG2", String.valueOf(park_info_frag.isInLayout()));
//        fragment==null || ! fragment.isInLayout();
        if (park_info_frag != null && park_info_frag.isVisible()) {
            fTrans = fragmentManager.beginTransaction();
            fTrans.remove(park_info_frag);
            fTrans.commit();
            Log.i("TAG", "in if");
//            map_layout.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.MATCH_PARENT));
        }
        else {
            this.finish();
        }
    }


    @Override
    public void onButtonSelected(int id) {
        switch (id){
            case R.id.btn_scroll_down: // btn_close
                fTrans = fragmentManager.beginTransaction();
                fTrans.remove(park_info_frag);
                fTrans.commit();
//                map_layout.setLayoutParams(new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.MATCH_PARENT));
                break;
            case R.id.btn_go_on:
                Intent intent_go_on = new Intent(this, PaymentActivity.class);
                intent_go_on.putExtra("address", selected_place.getAddress());
                intent_go_on.putExtra("from_hours", selected_time.get(0));
                intent_go_on.putExtra("from_minutes", selected_time.get(1));
                intent_go_on.putExtra("to_hours", selected_time.get(2));
                intent_go_on.putExtra("to_minutes", selected_time.get(3));

                startActivity(intent_go_on);
                break;

            case R.id.btn_time_info:
                Intent intent_time_info = new Intent(this, InfoTimeActivity.class);
                startActivity(intent_time_info);
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
        map.setOnMarkerClickListener(this);

        // [END_EXCLUDE]

        // Show Sydney
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(59.930099, 30.361609), 12));
//        if (!googleMap.isTrafficEnabled()){
//            googleMap.setTrafficEnabled(true);
//        }
    }
    private void addMarkersToMap(List<ParkingPlace> parking_book) {
        // Uses a colored icon.
        //for(ParkingPlace place:parking_book)
        //ListIterator<ParkingPlace> iterator = parking_book.listIterator();
        //while (iterator.hasNext()) {
        //    Log.i("TAG", iterator.next().getAddress());
        //    map.addMarker(new MarkerOptions()
        //            .position(iterator.next().getLoc())
        //            .flat(true)
        //            ).setTag(iterator.nextIndex());
        for (Integer i = 0; i < parking_book.size(); i++) {
            map.addMarker(new MarkerOptions()
                    .position(parking_book.get(i).getLoc())
                    .flat(true)
            ).setTag(i);
        };

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Integer id = (Integer) marker.getTag();
        selected_place = parking_book.get(id);
        fragmentManager = getFragmentManager();
        park_info_frag = (ParkInfoFragment) fragmentManager.findFragmentById(R.id.frgmCont);
        if (id < parking_book.size()){
            if (park_info_frag == null || !park_info_frag.isVisible()){

                park_info_frag = new ParkInfoFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.frgmCont, park_info_frag)
                        .addToBackStack("name")
                        .commit();
                park_info_frag.setSelectedPlace(selected_place);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT, 1150);
//                map_layout.setLayoutParams(params);
                }
            else {
                park_info_frag.changeSelectedPlace(selected_place);

            }
        }
        return false;
    }
}


