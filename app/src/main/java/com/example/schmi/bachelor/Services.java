package com.example.schmi.bachelor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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

    public static void sendReminders(){

        String username = Values.getInstance().getUsername();

        String dataRenters = getAPI("Renter.php?username=" + username);
        String dataRents = getAPI("Rents.php?reminder=1&username=" + username);
        try {
            JSONArray renters = new JSONArray(dataRenters);
            JSONArray rents = new JSONArray(dataRents);

            for(int i = 0; i < renters.length(); i++){
                JSONObject renter = renters.getJSONObject(i);
                String renterEmail = renter.getString("email");
                String renterName = renter.getString("rentername");

                ArrayList<JSONObject> rentsPerRenter = new ArrayList<>();

                for(int j = 0; j < rents.length(); j++){
                    JSONObject rent = rents.getJSONObject(j);
                    String rentEmail = rent.getString("email");
                    String rentName = rent.getString("rentername");

                    if(renterEmail.equals(rentEmail) && renterName.equals(rentName))
                        rentsPerRenter.add(rent);

                }

                if(rentsPerRenter.size() > 0){

                    String msg = buildMsg(rentsPerRenter, renterName);

                    sendEmail(renterEmail, msg);

                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private static String buildMsg(ArrayList<JSONObject> rentsPerRenter, String renterName) throws JSONException {
        String msg = "Hi " + renterName + ",\n" +
                "\n" +
                "You currently have the following items that have not been turned in on time:\n" +
                "\n";

        for(JSONObject rent : rentsPerRenter){
            String itemString = "Item: " + rent.getString("itemname") + "\t\t" +
                    "Loandate: " + rent.getString("date") + "\t\t" +
                    "Returndate: " + rent.getString("returndate") + "\n";
            msg += itemString;
        }
        msg += "\n" +
                "Please turn in the items as soon as possible\n" +
                "\n" +
                "Many thanks\n" +
                "HomeLogistics";

        return msg;

    }

    private static void sendEmail(String recipant, String msg){
        final String email = "NoReplyHomeLogistics@gmail.com";
        final String password = "NRHL1337";

        javax.mail.Session session;
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");


        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, password);
                    }
                });

        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);

            //Setting sender address
            mm.setFrom(new InternetAddress(email));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(recipant));
            //Adding subject
            mm.setSubject("Reminder to turn in items");
            //Adding message
            mm.setText(msg);

            //Sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
