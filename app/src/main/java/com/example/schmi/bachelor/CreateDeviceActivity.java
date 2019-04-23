package com.example.schmi.bachelor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateDeviceActivity extends AppCompatActivity {

    Button create;
    EditText deviceIDET, deviceNameET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_device);

        create = findViewById(R.id.create);
        deviceIDET = findViewById(R.id.deviceIDET);
        deviceNameET = findViewById(R.id.deviceNameET);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInput()){
                    new addDevice().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "input is not valid",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private boolean validateInput(){

        int deviceID = Integer.valueOf(String.valueOf(deviceIDET.getText()));
        String deviceName = String.valueOf(deviceNameET.getText());

        return !(deviceID == 0 || deviceName.equals(""));
    }

    private class addDevice extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(CreateDeviceActivity.this, "Creating device", "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String username = "mikkelschmidt14"; //Get user name

            String data = Services.getAPI("Device.php?deviceID=" + Integer.valueOf(String.valueOf(deviceIDET.getText())));

            if(data.length() > 3){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(CreateDeviceActivity.this, "Device ID already exists", Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            }

            data = Services.getAPI("Device.php?username=" + username + "&devicename=" + String.valueOf(deviceNameET.getText()));

            if(data.length() > 3){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(CreateDeviceActivity.this, "User already has device with that name", Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            }


            Services.postAPI("Device.php?username=" + username + "&deviceID=" + Integer.valueOf(String.valueOf(deviceIDET.getText())) + "&devicename=" + String.valueOf(deviceNameET.getText()));
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {

            Toast.makeText(CreateDeviceActivity.this, "Device has been created", Toast.LENGTH_SHORT);
            dialog.dismiss();
            onBackPressed();
        }
    }
}
