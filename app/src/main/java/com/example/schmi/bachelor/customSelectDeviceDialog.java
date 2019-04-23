package com.example.schmi.bachelor;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class customSelectDeviceDialog extends Dialog{

    Button select;
    Spinner spinner;
    JSONArray jsons;
    int itemID;
    String renterName, email;
    String type;


    public customSelectDeviceDialog(Activity a, int itemID) {
        super(a);
        this.itemID = itemID;
        type = "item";
    }

    public customSelectDeviceDialog(Activity a, String renterName, String email) {
        super(a);
        this.renterName = renterName;
        this.email = email;
        type = "renter";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select_device_dialog);

        spinner = findViewById(R.id.spinner);
        select = findViewById(R.id.select);


        new addDevicesToSpinner().execute();

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new attachRFIDs().execute();
            }
        });

    }

    private String getAddress(){

        String result = "";
        String username = "mikkelschmidt14";

        switch (type){
            case "item" :
                result = "state.php?username=" + username + "&devicename=" + spinner.getSelectedItem() + "&state=create item&info=" + "{\\\"itemID\\\":" + itemID + "}";
                break;
            case "renter" :
                result = "state.php?username=" + username + "&devicename=" + spinner.getSelectedItem() + "&state=create renter&info=" + "{\\\"userID\\\":" + "\\\"" + username +"\\\"" + ", \\\"renterName\\\":" + "\\\"" + renterName +"\\\"" + ", \\\"email\\\":" + "\\\"" + email +"\\\"" + "}";
                break;
            default:
            break;
        }

        return result;

    }

    private class attachRFIDs extends AsyncTask<Void, Void, Void> {

        Dialog dialog;

        @Override
        protected  void onPreExecute(){
            //create loader.....
            dialog = ProgressDialog.show(getContext(), "Device is scanning","Please wait...");
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String address = getAddress();
            Services.postAPI(address);
            System.out.println(address);

            String username = "mikkelschmidt14";

            while(true){
                System.out.println("in while....................");
                String data = Services.getAPI("state.php?username=" + username + "&devicename=" + spinner.getSelectedItem());
                System.out.println(data);
                try {
                    JSONArray jsons = new JSONArray(data);
                    JSONObject json = jsons.getJSONObject(0);
                    if(json.getString("state").equals("idle"))
                        break;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {

            dialog.dismiss();
        }

    }

    private class addDevicesToSpinner extends AsyncTask<Void, Void, Void> {

        List<String> list;

        @Override
        protected  void onPreExecute(){
            //create loader.....
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String username = "mikkelschmidt14";
            String data = Services.getAPI("Device.php?username=" + username);

            try {
                JSONArray jsons = new JSONArray(data);

                list = new ArrayList<String>();

                for (int i = 0; i < jsons.length(); i++) {
                    JSONObject json = jsons.getJSONObject(i);
                    if (json.getString("state").equals("idle"))
                        list.add(json.getString("devicename"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);

        }

    }

}
