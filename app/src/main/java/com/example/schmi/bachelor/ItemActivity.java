package com.example.schmi.bachelor;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemActivity extends AppCompatActivity {

    int itemID;
    TextView itemName, itemDescription, stock, rented;
    ImageView itemImage;
    Button attachBtn, deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        postponeEnterTransition();

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

    }

    private class deleteItem extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            Services.postAPI("Item.php?delete=1&itemID=" + itemID);

            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {

            onBackPressed();
        }

    }


    private class getItemJSON extends AsyncTask<Void, Void, Void> {

        JSONObject json;

        @Override
        protected  void onPreExecute(){

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

}
