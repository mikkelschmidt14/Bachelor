package com.example.schmi.bachelor;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schmi.bachelor.Fragments.SubFragments.RentedCardAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemActivity extends AppCompatActivity {

    int itemID;
    TextView itemName, itemDescription, stock, rented;
    ImageView itemImage;
    Button attachBtn, deleteBtn;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        getWindow().getEnterTransition().excludeTarget(android.R.id.statusBarBackground, true);
        postponeEnterTransition();

        listView = findViewById(R.id.listView);
        itemName = findViewById(R.id.itemName);
        itemDescription = findViewById(R.id.itemDescription);
        attachBtn = findViewById(R.id.attachBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        itemImage = findViewById(R.id.itemImage);
        stock = findViewById(R.id.stock);
        rented = findViewById(R.id.rented);

        attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customSelectDeviceDialog dialog = new customSelectDeviceDialog(ItemActivity.this, itemID);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new deleteItem().execute();
            }
        });

        itemID = getIntent().getExtras().getInt("itemID");

        new getItemJSON().execute();
        new getRentedJSONS().execute();

    }

    private class deleteItem extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String username = Values.getInstance().getUsername();
            String data = Services.getAPI("Rents.php?username=" + username + "&itemID=" + itemID);

            if (data.length() > 3)
                return "Item is being lent";

            Services.postAPI("Item.php?delete=1&itemID=" + itemID);

            return null;
        }

        @Override
        protected void onPostExecute(String msg) {

            if (msg != null) {
                Toast.makeText(ItemActivity.this, msg, Toast.LENGTH_SHORT).show();
            } else {
                onBackPressed();
            }

        }

    }

    private class getItemJSON extends AsyncTask<Void, Void, Void> {

        JSONObject json;

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected Void doInBackground(Void... voids) {

            JSONArray jsonArray;

            String data = Services.getAPI("Item.php?itemID=" + itemID);
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
                itemName.setText(json.getString("itemname"));
                itemDescription.setText(json.getString("itemdescription"));
                stock.setText(json.getString("stock"));
                rented.setText(json.getString("rented"));

                String imageString = json.getString("itemimage");
                itemImage.setImageBitmap(Services.StringToBitMap(imageString));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            startPostponedEnterTransition();

        }

    }

    private class getRentedJSONS extends AsyncTask<Void, Void, Void> {

        JSONArray jsonArray;

        @Override
        protected  void onPreExecute(){

        }

        @Override
        protected Void doInBackground(Void... voids) {
            String username = Values.getInstance().getUsername();
            String data = Services.getAPI("Rents.php?username=" + username + "&itemID=" + itemID);
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

            RentedCardAdapter cardAdapter = new RentedCardAdapter(ItemActivity.this, jsonArray);
            listView.setAdapter(cardAdapter);

            if(jsonArray.length() == 0){
                listView.setBackground(ContextCompat.getDrawable(ItemActivity.this, R.drawable.ic_sentiment_dissatisfied_black_24dp));
            }

        }

    }

}
