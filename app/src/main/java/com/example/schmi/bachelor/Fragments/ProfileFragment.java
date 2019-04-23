package com.example.schmi.bachelor.Fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schmi.bachelor.R;
import com.example.schmi.bachelor.Services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ProfileFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.view = view;

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
            String username = "mikkelschmidt14"; //Get user name
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

                devices.setText("Devices: " + json.getInt("devices"));
                renters.setText("Renters: " + json.getInt("renters"));
                items.setText("Items: " + json.getInt("items"));
                rented.setText("Rented: " + json.getInt("rented"));
                stock.setText("Stock: " + json.getInt("stock"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            dialog.dismiss();
        }
    }
}
