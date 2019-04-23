package com.example.schmi.bachelor;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RentedActivity extends AppCompatActivity {
    TextView renterName, itemName, itemDescription, rentDate, returnDate, location;

    Button turnIn, changeDate;
    String itemRFID = "", newReturnDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rented);
        postponeEnterTransition();

        itemRFID = getIntent().getExtras().getString("itemRFID");

        renterName = findViewById(R.id.renterName);
        itemName = findViewById(R.id.itemName);
        itemDescription = findViewById(R.id.itemDescription);
        location = findViewById(R.id.location);
        rentDate = findViewById(R.id.rentDate);
        returnDate = findViewById(R.id.returnDate);
        turnIn = findViewById(R.id.turnIn);
        changeDate = findViewById(R.id.changeDate);

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

            String username = "mikkelschmidt14";
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

}
