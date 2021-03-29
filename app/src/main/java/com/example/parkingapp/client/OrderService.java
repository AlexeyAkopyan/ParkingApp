package com.example.parkingapp.client;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.parkingapp.QRCodeEncoder;
import com.example.parkingapp.SuccessfulPaymentActivity;
import com.example.parkingapp.objects.Constants;
import com.example.parkingapp.objects.Order;
import com.google.android.gms.common.util.IOUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.WINDOW_SERVICE;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class OrderService {
    List<Order> orderList;

    public OrderService() {
        orderList = new ArrayList<>();
    }

    public void add(Order order) {
        this.orderList.add(order);
    }

    public void remove(Order order) {
        this.orderList.remove(order);
    }

    public void updateOrderId(Long oldId, Long newId) {
        for (Order order : orderList) {
            Log.i("TAG_UPDATE", "update");
            if (order.getId().equals(oldId))
                order.setId(newId);
        }
    }

    public List<Order> getOrderList() {
        return this.orderList;
    }

    public File getQR(Order order) throws Exception {

        Long id = order.getId();
        if (id < 0) {
            throw new Exception("Order is not confirmed");
        }
        File dir = new File(Constants.PATH_TO_QR);
        File qrFile = new File(dir, "qr" + id + ".svg");
        if (!qrFile.exists())
            throw new FileNotFoundException("QR code not found: get QR again from server");
        return qrFile;
    }

//    public byte[] getQR() throws Exception {
//
//        Long id = orderList.get(orderList.size() - 1).getId();
//        if (id < 0) {
//            throw new Exception("Order is not confirmed");
//        }
////        File dir = new File(Constants.PATH_TO_QR);
////        File qrFile = new File(dir, "qr" + id + ".svg");
////        if (!qrFile.exists())
////            throw new FileNotFoundException("QR code not found: get QR again from server");
////        return qrFile;
//        if (id < 0) {
//            throw new Exception("Order is not confirmed");
//        }
//        String filename = "qr" + id + ".svg";
//        Context context = SuccessfulPaymentActivity.getContext();
//        FileInputStream fis = context.openFileInput(filename);
//        int size = (int) fis.available();
//        byte[] bytes = new byte[size];
//        try (BufferedInputStream buf = new BufferedInputStream(fis)){
//            buf.read(bytes, 0, bytes.length);
//        } catch (IOException e) {
//            // Error occurred when opening raw file for reading.
//        }
//        return bytes;
//
//
//        try {
//            File f=new File(path, "profile.jpg");
//            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
//            img.setImageBitmap(b);
//        }
//        catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//        }
//        Bitmap bitmap = BitmapFactory.decodeFile();
//        ByteArrayOutputStream blob = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* Ignored for PNGs */, blob);
//        byte[] bitmapdata = blob.toByteArray();
//    }

//    public Bitmap getQR() throws Exception {
//
//        Long id = orderList.get(orderList.size() - 1).getId();
//        if (id < 0) {
//            throw new Exception("Order is not confirmed");
//        }
////        File dir = new File(Constants.PATH_TO_QR);
////        File qrFile = new File(dir, "qr" + id + ".svg");
////        if (!qrFile.exists())
////            throw new FileNotFoundException("QR code not found: get QR again from server");
////        return qrFile;
//
//        String filename = "qr" + id + ".png";
//        Context context = SuccessfulPaymentActivity.getContext();
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
////        Bitmap bMap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
//        try {
//            FileInputStream fis = context.openFileInput(filename);
//            System.out.println(fis.available());
////            Bitmap bitmap = BitmapFactory.decodeStream;
//            Bitmap bitmap = BitmapFactory.decodeFile(fis.getAbsolutePath(), options);
//            return bitmap;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public Bitmap encodeAsBitmap() throws Exception {
        Long id = orderList.get(orderList.size() - 1).getId();
        if (id < 0) {
            throw new Exception("Order is not confirmed");
        }
//        File dir = new File(Constants.PATH_TO_QR);
//        File qrFile = new File(dir, "qr" + id + ".svg");
//        if (!qrFile.exists())
//            throw new FileNotFoundException("QR code not found: get QR again from server");
//        return qrFile;

//        String filename = "qr" + id + ".png";
//        Context context = SuccessfulPaymentActivity.getContext();
//        File path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File file = new File(path, filename);
//        FileInputStream fis = context.openFileInput(filename);
//        byte[] bytes = FileUtils.rea
//        Bitmap bitmap = BitmapFactory.decode(fis);
//        return bitmap;

        String filename = "qr" + id + ".png";
        Context context = SuccessfulPaymentActivity.getContext();
        File path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(path, filename);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes, Long.BYTES, bytes.length - Long.BYTES);
        return bitmap;
//        Bitmap bitmap = BitmapFactory.decodeStream(file);

//        Map<EncodeHintType, Object> hints = null;
//        MultiFormatWriter writer = new MultiFormatWriter();
//        BitMatrix result = writer.encode("TEXT_TYPE", BarcodeFormat.valueOf(BarcodeFormat.QR_CODE.toString()), 230, 230);
//        int width = result.getWidth();
//        int height = result.getHeight();
//        int[] pixels = new int[width * height];
//        // All are 0, or black, by default
//        for (int y = 0; y < height; y++) {
//            int offset = y * width;
//            for (int x = 0; x < width; x++) {
//                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
//            }
//        }
//
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//        return bitmap;
    }
}
