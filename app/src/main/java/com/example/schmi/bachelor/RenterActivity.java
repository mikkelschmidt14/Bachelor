package com.example.schmi.bachelor;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schmi.bachelor.Fragments.SubFragments.RentedCardAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RenterActivity extends AppCompatActivity {

    TextView renterName, email;
    ListView listView;
    Button deleteBtn;
    String renterRFID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renter);
        getWindow().getEnterTransition().excludeTarget(android.R.id.statusBarBackground, true);
        postponeEnterTransition();

        listView = findViewById(R.id.listView);
        renterName = findViewById(R.id.renterName);
        email = findViewById(R.id.email);
        deleteBtn = findViewById(R.id.deleteBtn);

        renterRFID = getIntent().getExtras().getString("renterRFID");

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new deleteRenter().execute();
            }
        });

        new getRenterJSON().execute();
        new getRentedJSONS().execute();

    }

    private class getRenterJSON extends AsyncTask<Void, Void, Void> {

        JSONObject json;

        @Override
        protected  void onPreExecute(){

        }

        @Override
        protected Void doInBackground(Void... voids) {

            JSONArray jsonArray;

            String data = Services.getAPI("Renter.php?renterRFID=" + renterRFID);
            System.out.println(data);
            try {
                jsonArray = new JSONArray(data);
                json = jsonArray.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {

            try {
                renterName.setText(json.getString("rentername"));
                email.setText(json.getString("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            startPostponedEnterTransition();

        }

    }

    private class getRentedJSONS extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
        JSONArray jsonArray;

        @Override
        protected  void onPreExecute(){
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String username = Values.getInstance().getUsername();
            String data = Services.getAPI("Rents.php?username=" + username + "&renterRFID=" + renterRFID);
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

            RentedCardAdapter cardAdapter = new RentedCardAdapter(RenterActivity.this, jsonArray);
            listView.setAdapter(cardAdapter);

            if(jsonArray.length() == 0){
                listView.setBackground(ContextCompat.getDrawable(RenterActivity.this, R.drawable.ic_sentiment_dissatisfied_black_24dp));
            }

        }

    }

    private class deleteRenter extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String data = Services.getAPI("Rents.php?renterRFID=" + renterRFID);

            if (data.length() > 3)
                return "Loaner has borrowed items";

            Services.postAPI("Renter.php?delete=1&renterRFID=" + renterRFID);

            return null;
        }

        @Override
        protected void onPostExecute(String msg) {

            if (msg != null) {
                Toast.makeText(RenterActivity.this, msg, Toast.LENGTH_SHORT).show();
            } else {
                onBackPressed();
            }


        }

    }

}
