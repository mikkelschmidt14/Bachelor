package com.example.schmi.bachelor.Fragments.SubFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.schmi.bachelor.R;
import com.example.schmi.bachelor.RentedActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RentedCardAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private JSONArray jsons;
    private Context context;

    public RentedCardAdapter(Context c, JSONArray jsons){
        this.context = c;
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
    public View getView(int i, View view, ViewGroup viewGroup) {

        final JSONObject json = (JSONObject) getItem(i);
        View card = mInflater.inflate(R.layout.card_rented, null);

        final TextView renterName = card.findViewById(R.id.renterName);
        final TextView itemName = card.findViewById(R.id.itemName);
        final TextView location = card.findViewById(R.id.location);
        final TextView returnDate = card.findViewById(R.id.returnDate);


        try {
            renterName.setText(json.getString("rentername"));
            itemName.setText(json.getString("itemname"));
            location.setText(json.getString("devicename"));

            String rd = json.getString("returndate");
            returnDate.setText(rd);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date strDate = sdf.parse(rd);
            if (new Date().after(strDate)) {
                returnDate.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
            }


        } catch (Exception e){
            System.out.println("JSON ISSUE");
        }


        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent rentedIntent = new Intent(context, RentedActivity.class);

                try {
                    int itemID = json.getInt("itemID");
                    String itemRFID = json.getString("itemRFID");
                    String renterRFID = json.getString("renterRFID");
                    rentedIntent.putExtra("itemID", itemID);
                    rentedIntent.putExtra("itemRFID", itemRFID);
                    rentedIntent.putExtra("renterRFID", renterRFID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Pair p1 = Pair.create(itemName, ViewCompat.getTransitionName(itemName));
                Pair p2 = Pair.create(renterName, ViewCompat.getTransitionName(renterName));
                Pair p3 = Pair.create(returnDate, ViewCompat.getTransitionName(returnDate));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, p1, p2, p3);

                context.startActivity(rentedIntent, options.toBundle());
            }
        });
        return card;
    }
}
