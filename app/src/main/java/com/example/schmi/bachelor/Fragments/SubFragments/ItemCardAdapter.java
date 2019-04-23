package com.example.schmi.bachelor.Fragments.SubFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schmi.bachelor.ItemActivity;
import com.example.schmi.bachelor.R;
import com.example.schmi.bachelor.Services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemCardAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private JSONArray jsons;
    private Context context;

    ItemCardAdapter(Context c, JSONArray jsons){
        this.context = c;
        for(int i = 0; i < jsons.length(); i++){
            try {
                JSONObject json = jsons.getJSONObject(i);

                String imageString = json.getString("itemimage");
                Bitmap bitmap = Services.StringToBitMap(imageString);
                if(bitmap == null){
                    continue;
                }
                float aspectRatio = bitmap.getHeight() / (float) bitmap.getWidth();
                int height = 140;
                int width = Math.round(height / aspectRatio);

                Bitmap smallBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

                json.put("smallBitmap", Services.getStringImage(smallBitmap));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        this.jsons = jsons;

        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return jsons.length();
    }

    @Override
    public Object getItem(int i) {
        try {
            return jsons.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final JSONObject json = (JSONObject) getItem(i);
        View card = mInflater.inflate(R.layout.card_item, null);

        final TextView itemName = card.findViewById(R.id.itemName);
        final TextView itemDescription = card.findViewById(R.id.itemDescription);
        final ImageView itemImage = card.findViewById(R.id.itemImage);
        TextView amountRented = card.findViewById(R.id.amountRented);
        TextView amountStock = card.findViewById(R.id.amountStock);

        try {
            itemName.setText(json.getString("itemname"));
            itemDescription.setText(json.getString("itemdescription"));

            if(json.has("smallBitmap")){
                Bitmap smallBitmap = Services.StringToBitMap(json.getString("smallBitmap"));
                itemImage.setImageBitmap(smallBitmap);
            }

            amountRented.setText("" + json.getInt("rented"));
            amountStock.setText("" + json.getInt("stock"));

        } catch (Exception e){
            System.out.println("JSON ISSUE");
        }


        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Insert click here for click on item card
                System.out.println("Card have been clicked!");

                Intent productIntent = new Intent(context, ItemActivity.class);

                try {
                    int itemID = json.getInt("itemID");
                    productIntent.putExtra("itemID", itemID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Pair p1 = Pair.create(itemName, ViewCompat.getTransitionName(itemName));
                Pair p2 = Pair.create(itemDescription, ViewCompat.getTransitionName(itemDescription));
                Pair p3 = Pair.create(itemImage, ViewCompat.getTransitionName(itemImage));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, p1, p2, p3);

                context.startActivity(productIntent, options.toBundle());

            }
        });
        return card;
    }
}
