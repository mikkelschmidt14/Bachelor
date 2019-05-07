package com.example.schmi.bachelor.Fragments;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schmi.bachelor.ItemActivity;
import com.example.schmi.bachelor.R;
import com.example.schmi.bachelor.Services;
import com.example.schmi.bachelor.Values;
import com.example.schmi.bachelor.customSelectDeviceDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ProfileFragment extends Fragment {

    View view;
    Button sendRemindersBtn, deleteItemtagsBtn, deleteRenterTagsBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.view = view;


        sendRemindersBtn = view.findViewById(R.id.sendRemindersBtn);
        deleteItemtagsBtn = view.findViewById(R.id.deleteItemtagsBtn);
        deleteRenterTagsBtn = view.findViewById(R.id.deleteRentersBtn);

        sendRemindersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new sendReminders().execute();
            }
        });

        deleteItemtagsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customSelectDeviceDialog dialog = new customSelectDeviceDialog(getActivity(), "delete item tag");
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        deleteRenterTagsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customSelectDeviceDialog dialog = new customSelectDeviceDialog(getActivity(), "delete renter");
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        new getStatsJSONS().execute();

    }

    private class getStatsJSONS extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
        JSONArray jsonArray;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(getContext(), "Retrieving profile", "Loading. Please wait...");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String username = Values.getInstance().getUsername();
            String data = Services.getAPI("Stats.php?username=" + username);
            System.out.println("Result from call is" + data);
            try {
                jsonArray = new JSONArray(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {

            try {
                JSONObject json = jsonArray.getJSONObject(0);

                TextView devices = view.findViewById(R.id.devices);
                TextView renters = view.findViewById(R.id.renters);
                TextView items = view.findViewById(R.id.items);
                TextView rented = view.findViewById(R.id.rented);
                TextView stock = view.findViewById(R.id.stock);

                devices.setText("Scanners: " + json.getInt("devices"));
                renters.setText("Loaners: " + json.getInt("renters"));
                items.setText("Items: " + json.getInt("items"));
                rented.setText("Out: " + json.getInt("rented"));
                stock.setText("Total: " + json.getInt("stock"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            dialog.dismiss();
        }
    }

    private class sendReminders extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected Void doInBackground(Void... voids) {

            Services.sendReminders();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }

    }

}
