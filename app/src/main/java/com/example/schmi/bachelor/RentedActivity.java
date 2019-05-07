package com.example.schmi.bachelor;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.schmi.bachelor.Fragments.SubFragments.ItemCardAdapter;
import com.example.schmi.bachelor.Fragments.SubFragments.RenterCardAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RentedActivity extends AppCompatActivity {
    TextView renterName, itemName, itemDescription, rentDate, returnDate, location;

    Button turnIn, changeDate;
    String itemRFID = "", renterRFID = "", newReturnDate;
    int itemID;
    ListView itemListView, renterListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rented);
        getWindow().getEnterTransition().excludeTarget(android.R.id.statusBarBackground, true);
        postponeEnterTransition();

        itemID = getIntent().getExtras().getInt("itemID");
        itemRFID = getIntent().getExtras().getString("itemRFID");
        renterRFID = getIntent().getExtras().getString("renterRFID");

        renterName = findViewById(R.id.renterName);
        itemName = findViewById(R.id.itemName);
        itemDescription = findViewById(R.id.itemDescription);
        location = findViewById(R.id.location);
        rentDate = findViewById(R.id.rentDate);
        returnDate = findViewById(R.id.returnDate);
        turnIn = findViewById(R.id.turnIn);
        changeDate = findViewById(R.id.changeDate);
        itemListView = findViewById(R.id.itemListView);
        renterListView = findViewById(R.id.renterListView);

        turnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new turnInItem().execute();
            }
        });

        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                newReturnDate = sdf.format(calendar.getTime());
                new updateReturnDate().execute();
            }

        };

        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change date
                DatePickerDialog datePickerDialog = new DatePickerDialog(RentedActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.cardTextAccentColor));
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.cardTextMainColor));
            }
        });



        new getRentedJSON().execute();
        new getItemJSON().execute();
        new getRenterJSON().execute();

    }

    private class turnInItem extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            Services.postAPI("Rents.php?delete=1&itemRFID=" + itemRFID);

            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {

            onBackPressed();
        }

    }

    private class getRentedJSON extends AsyncTask<Void, Void, Void> {

        JSONObject json;

        @Override
        protected  void onPreExecute(){

        }

        @Override
        protected Void doInBackground(Void... voids) {

            JSONArray jsonArray;

            String username = Values.getInstance().getUsername();
            String data = Services.getAPI("rents.php?itemRFID=" + itemRFID + "&username=" + username);
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
                itemName.setText(json.getString("itemname"));
                itemDescription.setText(json.getString("itemdescription"));
                rentDate.setText(json.getString("date"));
                returnDate.setText(json.getString("returndate"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            startPostponedEnterTransition();

        }

    }

    private class updateReturnDate extends AsyncTask<Void, Void, Void> {


        @Override
        protected  void onPreExecute(){

        }

        @Override
        protected Void doInBackground(Void... voids) {

            Services.postAPI("Rents.php?itemRFID=" + itemRFID + "&returndate=" + newReturnDate);


            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {

            new getRentedJSON().execute();

        }

    }

    private class getItemJSON extends AsyncTask<Void, Void, Void> {

        JSONArray jsonArray;

        @Override
        protected  void onPreExecute(){

        }

        @Override
        protected Void doInBackground(Void... voids) {
            String username = Values.getInstance().getUsername();
            String data = Services.getAPI("Item.php?username=" + username + "&itemID=" + itemID);
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

            ItemCardAdapter cardAdapter = new ItemCardAdapter(RentedActivity.this, jsonArray);
            itemListView.setAdapter(cardAdapter);

            if(jsonArray.length() == 0){
                itemListView.setBackground(ContextCompat.getDrawable(RentedActivity.this, R.drawable.ic_sentiment_dissatisfied_black_24dp));
            }

        }

    }

    private class getRenterJSON extends AsyncTask<Void, Void, Void> {

        JSONArray jsonArray;

        @Override
        protected  void onPreExecute(){

        }

        @Override
        protected Void doInBackground(Void... voids) {
            String username = Values.getInstance().getUsername();
            String data = Services.getAPI("Renter.php?username=" + username + "&renterRFID=" + renterRFID);
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

            RenterCardAdapter cardAdapter = new RenterCardAdapter(RentedActivity.this, jsonArray);
            renterListView.setAdapter(cardAdapter);

            if(jsonArray.length() == 0){
                itemListView.setBackground(ContextCompat.getDrawable(RentedActivity.this, R.drawable.ic_sentiment_dissatisfied_black_24dp));
            }

        }

    }

}
