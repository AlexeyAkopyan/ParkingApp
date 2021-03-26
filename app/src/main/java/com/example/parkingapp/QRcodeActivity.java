package com.example.parkingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class QRcodeActivity extends AppCompatActivity {
    ImageView qr_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_rcode);
        Intent intent_payment = getIntent();
        int parkingId = intent_payment.getIntExtra("parkingId", 0);
        List<Integer> selected_time = intent_payment.getIntegerArrayListExtra("selected_time");
        String carNum = "car" + MainActivity.car_number;
        Calendar start = new GregorianCalendar();
        start.set(Calendar.HOUR_OF_DAY, selected_time.get(0));
        start.set(Calendar.MINUTE, selected_time.get(1));
        Calendar finish = new GregorianCalendar();
        finish.set(Calendar.HOUR_OF_DAY, selected_time.get(2));
        finish.set(Calendar.MINUTE, selected_time.get(3));
        String paymentInfo = "here comes some payment information " + intent_payment.getIntExtra("paymentId", 0);
        com.example.parkingapp.objects.Order order = new com.example.parkingapp.objects.Order(null, (long) parkingId, carNum, start.getTimeInMillis(),
                finish.getTimeInMillis(), paymentInfo);
        MainActivity.connection.sendOrder(order);
        qr_code = findViewById(R.id.img_qr_code);
    }

//    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
//    Bitmap bitmap;
//    QRGEncoder qrgEncoder;
//
//    // initializing a variable for default display.
//    Display display = manager.getDisplay();
//
//    // creating a variable for point which
//    // is to be displayed in QR Code.
//    Point point = new Point();
//    display.getSize(point);
//
//    // getting width and
//    // height of a point
//    int width = point.x;
//    int height = point.y;
//
//    // generating dimension from width and height.
//    int dimen = width < height ? width : height;
//    dimen = dimen * 3 / 4;
//
//    // setting this dimensions inside our qr code
//    // encoder to generate our qr code.
//    qrgEncoder = new QRGEncoder(dataEdt.getText().toString(), null, QRGContents.Type.TEXT, dimen);
//                    try {
//        // getting our qrcode in the form of bitmap.
//        bitmap = qrgEncoder.encodeAsBitmap();
//        // the bitmap is set inside our image
//        // view using .setimagebitmap method.
//        qrCodeIV.setImageBitmap(bitmap);
//    } catch (WriterException e) {
//        // this method is called for
//        // exception handling.
//        Log.e("Tag", e.toString());
}