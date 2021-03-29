package com.example.parkingapp.client;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.parkingapp.SuccessfulPaymentActivity;
import com.example.parkingapp.objects.Constants;
import com.example.parkingapp.objects.MessageGenerator;
import com.example.parkingapp.objects.Order;
import com.example.parkingapp.objects.Parking;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

import okhttp3.*;
import okio.ByteString;

//import javax.swing.*;
import java.io.*;
//import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class WebSocketConnection {
    private OkHttpClient client ;
    private Request request;
    private WebSocket webSocket;
    private ClientWebSocketListener listener;
    private ParkingService parkingService;
    private OrderService orderService;

    public WebSocketConnection(String url, ParkingService parkingService, OrderService orderService) {
        client = new OkHttpClient();
        request = new Request.Builder().url(url).build();
        listener = new ClientWebSocketListener(this);
        this.parkingService = parkingService;
        this.orderService = orderService;
        //parkingList = null;
    }

    public void init() {
        webSocket = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    public void send(String message) {
        webSocket.send(message);
        System.out.println("Text sent: " + message);
    }

    public void sendMessage(String message) {
        message = MessageGenerator.MESSAGE + message;
        webSocket.send(message);
        System.out.println("Message sent: " + message);
    }

    public void sendError(String error) {
        error = MessageGenerator.ERROR + error;
        webSocket.send(error);
        System.out.println("Error sent: " + error);
    }

    public void sendOrder(Order order) {
        String message = MessageGenerator.SEND_ORDER + order.toString();
        webSocket.send(message);
        this.orderService.add(order);
        System.out.println("Order sent: " + message);
    }

    public void onParkingListGet(List<Parking> parkingList) {
        // place here your code
        // this is an example
        System.out.println("Received all parkings:");
        for (int i = 0; i < parkingList.size(); i++) {
            System.out.println(parkingList.get(i).toString());
        }
        //this.parkingList = parkingList;
        parkingService.setParkingList(parkingList);
        //System.out.println(this.parkingList.size());
    }

    public void onParkingGet(Parking parking) {
        // place here your code
        // this is an example
        System.out.println("Received parking update:");
        System.out.println(parking.toString());
        parkingService.updateParking(parking);
        /*
        for (Parking p : parkingList) {
            if (p.getId().equals(parking.getId())) {
                parkingList.remove(p);
                parkingList.add(parking);
                break;
            }
        }
        */

    }

    public void onMessageGet(String message) {
        // place here your code
        // this is an example
        System.out.println("Received message:");
        System.out.println(message);
    }

    public void onOrderIdGet(String message) {
        int split = message.indexOf('|');
        Long oldId = Long.parseLong(message.substring(0, split));
        Long newId = Long.parseLong(message.substring(split + 1));
        String getQR = MessageGenerator.GET_QR + newId;
        this.orderService.updateOrderId(oldId, newId);
        this.send(getQR);
        System.out.println("send qr request: " + getQR);
    }
    // Checks if a volume containing external storage is available
    // for read and write.
    private boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    // Checks if a volume containing external storage is available to at least read.
    private boolean isExternalStorageReadable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }

//    public File onQRGet(ByteString bytes) {
//        Long id = bytes.asByteBuffer().getLong();
//        Context context = SuccessfulPaymentActivity.getContext();
//        String filename = "qr" + id + ".png";
//////        try (FileOutputStream qrFile = context.openFileOutput(filename, Context.MODE_PRIVATE)){
//////             {
//////                 qrFile.write(bytes.toByteArray());
//////                 System.out.println("QR is received and written at file: " + qrFile.getFD().toString() + filename);
//////                 return filename;}
////        try {
////            File qrFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename);
//////            qrFile.createNewFile();
////            FileOutputStream outputStream = new FileOutputStream(qrFile, false);
////            outputStream.write(bytes.toByteArray(), Long.BYTES, bytes.size() - Long.BYTES);
////            outputStream.flush();
////            outputStream.close();
////            return qrFile;
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        return null;
//
//        com.google.zxing.Writer writer = new QRCodeWriter();
//        String decoded = "";
//        try {
//            decoded = new String(bytes.toByteArray(), "ISO-8859-1");
//        } catch (UnsupportedEncodingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        BitMatrix result = null;
//        try {
//            if (decoded != "") {
//                result = writer.encode(decoded, BarcodeFormat.QR_CODE, 500, 500);
//            }
//            else {System.out.println("Decoded is empty");}
//        } catch (WriterException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//
//        LuminanceSource source = new PlanarYUVLuminanceSource(bytes.toByteArray(),640,480,0,0,640,480,false);
//        BinaryBitmap bmtobedecoded = new BinaryBitmap(new HybridBinarizer(source));
//        Map<DecodeHintType,Object> mp=new HashMap<DecodeHintType, Object>();
//
//        mp.put(DecodeHintType.PURE_BARCODE, true);
//        try {
//            result= qrr.decode(bmtobedecoded,mp);
//        } catch (NotFoundException e) {
//            Log.i("123","not found");
//            e.printStackTrace();
//        } catch (ChecksumException e) {
//            Log.i("123","checksum");
//            e.printStackTrace();
//        } catch (FormatException e) {
//            Log.i("123","format");
//            e.printStackTrace();
//        }
//    }



    void createExternalStoragePrivatePicture(ByteString bytes) {
        // Create a path where we will place our picture in our own private
        // pictures directory.  Note that we don't really need to place a
        // picture in DIRECTORY_PICTURES, since the media scanner will see
        // all media in these directories; this may be useful with other
        // media types such as DIRECTORY_MUSIC however to help it classify
        // your media for display to the user.

//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes.toByteArray(), 0, bytes.size(), options);
//        return bitmap;


//        com.google.zxing.Writer writer = new QRCodeWriter();
//        String decoded = "";
//        try {
//            decoded = new String(bytes.toByteArray(), "ISO-8859-1");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        BitMatrix result = null;
//        if (decoded != "") {
//            try {
//                result = writer.encode(decoded, BarcodeFormat.QR_CODE, 100, 100);
//                int width = result.getWidth();
//                int height = result.getHeight();
//                int[] pixels = new int[width * height];
//                // All are 0, or black, by default
//                for (int y = 0; y < height; y++) {
//                    int offset = y * width;
//                    for (int x = 0; x < width; x++) {
//                        pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
//                    }
//                }
//
//                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//                bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//                return bitmap;
//            } catch (WriterException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;



        Long id = bytes.asByteBuffer().getLong();
        Context context = SuccessfulPaymentActivity.getContext();
        File path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String filename = "qr" + id + ".png";
        File file = new File(path, filename);

        try {
            // Very simple code to copy a picture from the application's
            // resource into the external file.  Note that this code does
            // no error checking, and assumes the picture is small (does not
            // try to copy it in chunks).  Note that if external storage is
            // not currently mounted this will silently fail.
            OutputStream os = new FileOutputStream(file);

            os.write(bytes.toByteArray(), 0, bytes.size());//, Long.BYTES, bytes.size() - Long.BYTES);
            os.close();

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(context,
                    new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
//
//        try {
//            // Very simple code to copy a picture from the application's
//            // resource into the external file.  Note that this code does
//            // no error checking, and assumes the picture is small (does not
//            // try to copy it in chunks).  Note that if external storage is
//            // not currently mounted this will silently fail.
////            InputStream is = context.getResources().openRawResource(R.drawable.balloons);
//            OutputStream os = new FileOutputStream(file);
//            os.write(bytes.toByteArray());
//            os.close();
//
//            // Tell the media scanner about the new file so that it is
//            // immediately available to the user.
//            MediaScannerConnection.scanFile(context,
//                    new String[] { file.toString() }, null,
//                    new MediaScannerConnection.OnScanCompletedListener() {
//                        public void onScanCompleted(String path, Uri uri) {
//                            Log.i("ExternalStorage", "Scanned " + path + ":");
//                            Log.i("ExternalStorage", "-> uri=" + uri);
//                        }
//                    });
//        } catch (IOException e) {
//            // Unable to create file, likely because external storage is
//            // not currently mounted.
//            Log.w("ExternalStorage", "Error writing " + file, e);
//        }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void deleteExternalStoragePrivatePicture() {
        // Create a path where we will place our picture in the user's
        // public pictures directory and delete the file.  If external
        // storage is not currently mounted this will fail.
        Context context = SuccessfulPaymentActivity.getContext();
        File path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (path != null) {
            File file = new File(path, "DemoPicture.jpg");
            file.delete();
        }
    }

    boolean hasExternalStoragePrivatePicture() {
        // Create a path where we will place our picture in the user's
        // public pictures directory and check if the file exists.  If
        // external storage is not currently mounted this will think the
        // picture doesn't exist.
        Context context = SuccessfulPaymentActivity.getContext();
        File path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (path != null) {
            File file = new File(path, "DemoPicture.jpg");
            return file.exists();
        }
        return false;
    }

    public void onError(String error) {
        // place here your code
        // this is an example
        System.out.println("Received error message");
        System.out.println(error);
    }

    public void close() {
        // place here your code
        // this is an example
        webSocket.close(1000, "");
        System.out.println("Connection closed");
    }

    public void reconnect() {
        // place here your code
    }

    public List<Parking> getParkingList() {
        //return this.parkingList;
        return null;
    }
}