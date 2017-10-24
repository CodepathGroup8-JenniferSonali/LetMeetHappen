package com.example.skarwa.letmeethappen.utils;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jennifergodinez on 10/19/17.
 */

public class FCM {
    // Method to send Notifications from server to client end.

    public final static String AUTH_KEY_FCM = "AAAAI2oBZc8:APA91bFkpgjmhaJg1pd6xbsdpXFZDkpOoWpL3Ftpac0qQ3BAZSI36B8X07QiLCJgFpy-VnwzmMJpUgDXhjJEHpb4O22kgyGt0jzyW1P79RwRG0gC-N9wEiCPOrlD5ErzTBoMyw5YQ46-";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

// userDeviceIdKey is the device id you will query from your database

    public static void pushFCMNotification(final ArrayList<String> tokens) throws Exception{
        new Thread(new Runnable() {
            @Override
            public void run() {
                
                try {
                    String authKey = AUTH_KEY_FCM; // You FCM AUTH key
                    String FMCurl = API_URL_FCM;
                    URL url = new URL(FMCurl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                   
                    conn.setUseCaches(false);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                   
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Authorization", "key="+authKey);
                    conn.setRequestProperty("Content-Type", "application/json");

                    JSONObject json = new JSONObject();
                    String idTokens = "[";
                    for (String token : tokens) {
                        idTokens += token+",";
                    }
                    char[] chArry = idTokens.toCharArray();
                    chArry[idTokens.length() - 1] = ']';

                    //json.put("registration_ids", chArry); TODO commented for testing only  e0BmSKx-hmQ:APA91bEBMqJ_Vh0k40kF01DKRJ79Nu--GhE1FxI8sJITTqB8MJNpHI0IZBPETzUpaNRzJ8fjhXBAmncpu6uQzNH1ka-JQVi_VVweGiR0715kv4Jc2U7kHOrMGnjv6Z_VKZCFDLw-Jj1C
                    String userDeviceIdKey = "e0BmSKx-hmQ:APA91bEBMqJ_Vh0k40kF01DKRJ79Nu--GhE1FxI8sJITTqB8MJNpHI0IZBPETzUpaNRzJ8fjhXBAmncpu6uQzNH1ka-JQVi_VVweGiR0715kv4Jc2U7kHOrMGnjv6Z_VKZCFDLw-Jj1C";
                    json.put("to",userDeviceIdKey.trim());
                   
                    JSONObject info = new JSONObject();
                    info.put("title", "Let Meet Happen"); // Notification title
                    info.put("body", "You have a new invite!"); // Notification body
                    json.put("notification", info);

                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(json.toString());
                    wr.flush();
                    conn.getInputStream();
                } catch (Exception e) {
                   
                }
            }}).start();
    }
}
