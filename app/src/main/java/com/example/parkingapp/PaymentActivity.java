package com.example.parkingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkingapp.objects.Order;

import java.util.Date;
import java.util.Random;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener
{
    private Button btn_pay;
    private ImageButton btn_back;
    private TextView txt_street,
            txt_price,
            txt_time_from_hours,
            txt_time_from_minutes,
            txt_time_to_hours,
            txt_time_to_minutes;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        btn_pay = findViewById(R.id.btn_pay);
        btn_back = findViewById(R.id.btn_back);
        txt_street = findViewById(R.id.txt_street2);
        txt_price = findViewById(R.id.txt_price);
        txt_time_from_hours = findViewById(R.id.txt_time_from_hours2);
        txt_time_from_minutes = findViewById(R.id.txt_time_from_minutes2);
        txt_time_to_hours = findViewById(R.id.txt_time_to_hours2);
        txt_time_to_minutes = findViewById(R.id.txt_time_to_minutes2);
        btn_pay.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        Intent intent=getIntent();
        txt_street.setText(intent.getStringExtra("address"));
        txt_price.setText("245");
        txt_time_from_hours.setText(TimeFormat(intent.getIntExtra("from_hours", 0)));
        txt_time_from_minutes.setText(TimeFormat(intent.getIntExtra("from_minutes", 0)));
        txt_time_to_hours.setText(TimeFormat(intent.getIntExtra("to_hours", 0)));
        txt_time_to_minutes.setText(TimeFormat(intent.getIntExtra("to_minutes", 0)));

        Toast toast = Toast.makeText(this, "Paid "  + "\nPaymentId:" + "245", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
        toast.show();
    }

    public String TimeFormat(Integer i){
        return ((i<10)?"0":"") + i;
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_pay) {
//            try {
//                connect();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            Intent intent = new Intent(this, CardActivity.class);
            intent.putExtra("amount", txt_price.getText());
            Log.i("TAG_PRICE", (String) txt_price.getText());
            startActivity(intent);
        }
        if (v.getId() == R.id.btn_back){
            this.finish();
        }
    }

    public static void connect() throws InterruptedException {

        WebSocketConnection connection = new WebSocketConnection("http://10.0.2.3:8080/client");
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
            } else {
                System.out.println("pList is null");

                Thread.sleep(2000);
            }
        }
    }
}