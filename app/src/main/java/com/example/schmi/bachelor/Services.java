package com.example.schmi.bachelor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class Services {

    public static String getAPI(String address) {

        System.out.println("Calling API");
        try {

            address = address.replaceAll(" ", "%20");
            URL url = new URL("https://mikkelschmidt.azurewebsites.net/" + address);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            bufferedReader.close();
            urlConnection.disconnect();

            return stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static void postAPI(String address) {

        System.out.println("Calling post API");
        try {

            address = address.replaceAll(" ", "%20");
            URL url = new URL("https://mikkelschmidt.azurewebsites.net/" + address);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            urlConnection.setDoOutput(true);

            int responseCode = urlConnection.getResponseCode();
            System.out.println("Response code is------------------ " + responseCode);
            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void postAPI(String address, Bitmap image) {

//        String username="mikkelschmidt14";
//        int rnd = new Random().nextInt(1000000) + 1;
//        String filename = "image" + username + rnd;

        System.out.println("Calling post API");
        try {

            address = address.replaceAll(" ", "%20");
            URL url = new URL("https://mikkelschmidt.azurewebsites.net/" + address);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            urlConnection.setDoOutput(true);

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            //writer.write("image=" + getStringImage(image) + "&imagename=" + filename);
            writer.write("image=" + getStringImage(image));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = urlConnection.getResponseCode();
            System.out.println("Response code is------------------ " + responseCode);
            String response = "";
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                response = br.readLine();
            } else {
                response = "Error Registering";
            }
            System.out.println("response is: " + response);
            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public static Bitmap StringToBitMap(String encodedString){
        encodedString = encodedString.replace("\\/", "/");
        encodedString = encodedString.replace(" ", "+");
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}
