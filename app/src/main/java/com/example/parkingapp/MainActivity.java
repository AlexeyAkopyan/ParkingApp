package com.example.parkingapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.util.Log;
import android.widget.TextView;

import com.example.parkingapp.client.OrderService;
import com.example.parkingapp.client.ParkingService;
import com.example.parkingapp.objects.Constants;
import com.example.parkingapp.objects.Order;
import com.example.parkingapp.objects.Parking;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.example.parkingapp.client.OrderService;
import com.example.parkingapp.client.ParkingService;
import com.example.parkingapp.client.WebSocketConnection;
import com.example.parkingapp.objects.Constants;
import com.example.parkingapp.objects.Order;
import com.example.parkingapp.objects.Parking;

import java.io.File;
import java.util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Date;
import java.util.Random;


public class MainActivity extends AppCompatActivity
        implements OnClickListener,
        ParkInfoFragment.OnSelectedButtonListener,
        ParkInfoFragment.SendTime,
        SelectPayTypeFragment.BottomSheetDialogListener,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    private ImageButton btn_menu;
    private Button btn_show_qr_code;
    private ParkInfoFragment park_info_frag;
    private FragmentManager fragmentManager;
    private FrameLayout bottomSheet;
    private GoogleMap map;
    private ConstraintLayout toolbar;
    public static List<ParkingPlace> parking_book;
    public static List<Integer> selected_time;
    private BottomSheetBehavior bottomSheetBehavior;
    public static ParkingPlace selected_place;
    private List<Parking> parkingList;
    public static WebSocketConnection connection;
    public static OrderService orderService;
    public static ParkingService parkingService;
    private String amount;
    public static String car_number;
    private Integer pay_type;
    boolean hidden;
    public static Bitmap bitmap = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(
                "CarNumberPref", MODE_PRIVATE);
        car_number = pref.getString("carNumber",
                getResources().getString(R.string.type_car_number));

        btn_menu = findViewById(R.id.btn_menu);
        btn_menu.setOnClickListener(this);

        parkingService = new ParkingService();
        orderService = new OrderService();
        connection = new WebSocketConnection(Constants.LOCALHOST_URL_ANDROID, parkingService, orderService);
        connection.init();

        while (parkingService.getParkingList().size() < 1) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        parkingList = parkingService.getParkingList();



        Log.i("TAG_CREATE", "MainActivity Created");
        parking_book = getParkingBook(parkingList);
        System.out.println(parking_book);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_show_qr_code = findViewById(R.id.btn_show_qr_code);
        btn_show_qr_code.setOnClickListener(this);

        bottomSheet = findViewById(R.id.frgmCont);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        hidden = true;
        toolbar = findViewById(R.id.toolbar);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View view, int i) {
                if (i == BottomSheetBehavior.STATE_HIDDEN && !hidden) {
                    Animation anim_appear = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.toolbar_down);
                    toolbar.startAnimation(anim_appear);
                    toolbar.setVisibility(View.VISIBLE);
                    if (orderService.getOrderList().size() > 0){btn_show_qr_code.setVisibility(View.VISIBLE);}
                    hidden = true;
                }
                if (i != BottomSheetBehavior.STATE_HIDDEN && hidden){
                    Animation anim_disappear = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.toolbar_up);
                    toolbar.startAnimation(anim_disappear);
                    toolbar.setVisibility(View.INVISIBLE);
                    btn_show_qr_code.setVisibility(View.INVISIBLE);
                    hidden = false;
                }

            }

            @Override
            public void onSlide(View view, float v) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (orderService.getOrderList().size() > 0){
            btn_show_qr_code.setVisibility(View.VISIBLE);
        }
        else{
            btn_show_qr_code.setVisibility(View.INVISIBLE);
        }

        park_info_frag = new ParkInfoFragment();
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.frgmCont, park_info_frag)
                .addToBackStack("name")
                .commit();
    }

    List<ParkingPlace> getParkingBook(List<Parking> parkingList) {
        List<ParkingPlace> parking_book = new ArrayList<ParkingPlace>();
        for (Integer i = 0; i < parkingList.size(); i++){
            parking_book.add(new ParkingPlace(parkingList.get(i)));
        }
        return parking_book;
    }

    @Override
    public void sendData(List<Integer> time, String amount) {
        selected_time = time;
        this.amount = amount;
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_menu) {
            Intent intent_menu = new Intent(this, MenuActivity.class);
            intent_menu.putExtra("car_number", car_number);
            startActivity(intent_menu);
        }
        if (v.getId() == R.id.btn_show_qr_code){
            Intent intent_qr_codes = new Intent(this, QRcodeActivity.class);
            intent_qr_codes.putExtra("selected_time", new ArrayList<>(selected_time));
            intent_qr_codes.putExtra("address", selected_place.getAddress());
            startActivity(intent_qr_codes);
        }
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            this.finishAffinity();
        }
    }


    @Override
    public void onButtonSelected(int id) {
        switch (id) {
            case R.id.btn_scroll_down: // btn_close
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                Animation anim_appear = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.toolbar_down);
                toolbar.startAnimation(anim_appear);
                toolbar.setVisibility(View.VISIBLE);
                if (orderService.getOrderList().size() > 0){btn_show_qr_code.setVisibility(View.VISIBLE);}
                hidden = true;
                break;
            case R.id.btn_go_on:
                Intent intent_go_on = new Intent(this, CardActivity.class);
                Log.i("TAG_main_selected_time", TimeFormat(selected_time.get(0)) +
                        ":" + TimeFormat(selected_time.get(1)) + "   по " + TimeFormat(selected_time.get(2)) +
                        ":" + TimeFormat(selected_time.get(3)));
                intent_go_on.putExtra("parkingId", selected_place.getId());
                intent_go_on.putExtra("amount", amount);
                intent_go_on.putIntegerArrayListExtra("selected_time", (ArrayList<Integer>) selected_time);
                Log.i("TAG_main", "put_address");
                intent_go_on.putExtra("selected_time", new ArrayList<>(selected_time));
                intent_go_on.putExtra("PayType", pay_type);
                startActivity(intent_go_on);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.btn_pay_type:
                BottomSheetDialogFragment frag_car_type = new SelectPayTypeFragment();
                frag_car_type.show(getSupportFragmentManager(),
                        "ModalBottomSheet");
                Log.i("TAG_PAY_TYPE", "open pay type");
            default:
                break;


        }
    }

    public String TimeFormat(Integer i) {
        return ((i < 10) ? "0" : "") + i;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        addMarkersToMap(parking_book);
        map.setOnMarkerClickListener(this);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(59.930099, 30.361609), 12));
        if (map.isTrafficEnabled()){
            map.setTrafficEnabled(true);
        }
    }

    private void addMarkersToMap(List<ParkingPlace> parking_book) {
        for (Integer i = 0; i < parking_book.size(); i++) {
            map.addMarker(new MarkerOptions()
                    .position(parking_book.get(i).getLoc())
                    .flat(true)
            ).setTag(i);
        }
        ;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Integer id = (Integer) marker.getTag();
        Log.i("TAG_HIDDEN", String.valueOf(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN));

        selected_place = parking_book.get(id);
        park_info_frag.setSelectedPlace(selected_place);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        return true;
    }

    @Override
    public void OnCardSelected(Integer id) {
        park_info_frag.setSelectedPayType(id);
    }



}





