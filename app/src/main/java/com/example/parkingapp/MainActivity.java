package com.example.parkingapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.util.Log;
import android.widget.TextView;

import com.example.parkingapp.objects.Order;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Date;
import java.util.Random;


public class MainActivity extends AppCompatActivity
        implements OnClickListener, ParkInfoFragment.OnSelectedButtonListener, ParkInfoFragment.SendTime, OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    public Button btn_spot;
    public Button btn_map;
    public ImageButton btn_menu;
    public ParkInfoFragment park_info_frag;
    public FragmentManager fragmentManager;
    public FragmentTransaction fTrans;
    public boolean IsDynamic;
    private GoogleMap map;
    private LinearLayout map_layout;
    public List<ParkingPlace> parking_book;
    public List<Integer> selected_time;
    private BottomSheetBehavior bottomSheetBehavior;
    public ParkingPlace selected_place = null;// = new ArrayList<Object>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_menu = findViewById(R.id.btn_menu);
        btn_menu.setOnClickListener(this);

        parking_book = ReadParkingBook();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fragmentManager = getFragmentManager();

        FrameLayout bottomSheet = findViewById(R.id.frgmCont);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator_layout);

//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

// ..
// here you could find all the UI views inside your bottom sheet and initialize them
// ..

// ..
// setting the bottom sheet callback for interacting with state changes and sliding
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View view, int i) {
                // do something when state changes
//                if (i < bottomSheetBehavior.getPeekHeight()){
//                    coordinatorLayout.setLayoutParams(
//                            new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, i));
//                }
            }

            @Override
            public void onSlide(View view, float v) {
                // do something when slide
                if (v > 0) {
                    btn_menu.animate().scaleX(1 - v).scaleY(1 - v).setDuration(0).start();
                }
            }
        });

// ..
// set the bottom sheet callback state to hidden when you just start your app
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);


//        map_layout = findViewById(R.id.MapLayout);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
//        map_layout.setLayoutParams(params);



    }
     List<ParkingPlace> ReadParkingBook(){
        List<ParkingPlace> parking_book = new ArrayList<ParkingPlace>();
        parking_book.add(new ParkingPlace(
                59.930413, 30.361137,
                "пр. Пятилеток, 1, лит. А", Arrays.asList(15, 0, 23, 59),
                7));
        parking_book.add(new ParkingPlace(59.933686, 30.313075,
                "наб. р. Мойки, 75-79", Arrays.asList(12, 0, 22, 00),
                20));
        parking_book.add(new ParkingPlace(59.930086, 30.344658,
                "ул. Рубинштейна, 11", Arrays.asList(17, 30, 23, 59),
                12));
        parking_book.add(new ParkingPlace(59.927321, 30.385752,
                "пл. Восстания", Arrays.asList(0, 0, 23, 59),
                1));
        return parking_book;
    }

    @Override
    public void sendData(List<Integer> time) {
        selected_time = time;

    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_menu){
//            try {
//                connect();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            Intent intent_menu = new Intent(this, MenuActivity.class);
            startActivity(intent_menu);
//            fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
        }
    }

    @Override
    public void onBackPressed() {

        Log.i("TAG", "in onBackPressed");
        park_info_frag = (ParkInfoFragment) fragmentManager.findFragmentById(R.id.frgmCont);
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
            this.finishAffinity();
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
                Log.i("TAG_main_selected_time", TimeFormat(selected_time.get(0)) +
                        ":" + TimeFormat(selected_time.get(1)) + "   по " + TimeFormat(selected_time.get(2)) +
                        ":" + TimeFormat(selected_time.get(3)));
                intent_go_on.putExtra("address", selected_place.getAddress());
                Log.i("TAG_main", "put_address");
                intent_go_on.putExtra("from_hours", selected_time.get(0));
                Log.i("TAG_main", "put_time_0");
                intent_go_on.putExtra("from_minutes", selected_time.get(1));
                intent_go_on.putExtra("to_hours", selected_time.get(2));
                intent_go_on.putExtra("to_minutes", selected_time.get(3));

                startActivity(intent_go_on);
                break;

            case R.id.btn_time_info:
                Intent intent_time_info = new Intent(this, InfoTimeActivity.class);
                List<Integer> time = selected_place.getWorkingHours();
                String working_hours = "c " + TimeFormat(time.get(0)) +
                        ":" + TimeFormat(time.get(1)) + "   по " + TimeFormat(time.get(2)) +
                        ":" + TimeFormat(time.get(3));
                intent_time_info.putExtra("working_hours", working_hours);
                startActivity(intent_time_info);
                break;
            default:
                break;


        }
    }

    public String TimeFormat(Integer i){
        return ((i<10)?"0":"") + i;
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
        Log.i("TAG_HIDDEN", String.valueOf(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        selected_place = parking_book.get(id);
        park_info_frag = (ParkInfoFragment) fragmentManager.findFragmentById(R.id.frgmCont);
        if (id < parking_book.size()){
            if (park_info_frag == null || !park_info_frag.isVisible()){

                park_info_frag = new ParkInfoFragment();

                fragmentManager.beginTransaction()
                        .add(R.id.frgmCont, park_info_frag)
                        .addToBackStack("name")
                        .commit();
                park_info_frag.setSelectedPlace(selected_place);
                bottomSheetBehavior.setHideable(true);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT, 1150);
//                map_layout.setLayoutParams(params);
                }
            else {
                park_info_frag.changeSelectedPlace(selected_place);

            }
        }
        return true;
    }


    public static void connect() throws InterruptedException {
        WebSocketConnection connection = new WebSocketConnection("http://10.0.2.2:8080/client");
        connection.init();
        Random random = new Random();
        Integer count = 0;
        while (count < 100) {
            count++;
            if (connection.getParkingList() != null) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                int parkingId = random.nextInt(connection.getParkingList().size());
                String carNum = "car" + random.nextInt(100);
                Date start = new Date(new Date().getTime() + random.nextInt(3600 * 1000 * 8) + 3600 * 1000);
                Date finish = new Date(start.getTime() + 3600 * 1000);
                String paymentInfo = "here comes some payment information " + random.nextInt(100);
                Order order = new Order(null, (long) parkingId, carNum, start.getTime(),
                        finish.getTime(), paymentInfo);
                connection.sendOrder(order);
                Log.i("TAG_CONNECT", start.toString());
            } else {
                System.out.println("pList is null");

                Thread.sleep(2000);
            }
        }
    }
}


