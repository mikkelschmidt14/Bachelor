package com.example.schmi.bachelor.Fragments.SubFragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.schmi.bachelor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RenterCardAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private JSONArray jsons;
    private Context context;

    RenterCardAdapter(Context c, JSONArray jsons){
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

        JSONObject json = (JSONObject) getItem(i);
        View card = mInflater.inflate(R.layout.card_renter, null);

        TextView renterName = card.findViewById(R.id.renterName);
        TextView amountRented = card.findViewById(R.id.location);
        TextView email = card.findViewById(R.id.email);

        try {
            renterName.setText(json.getString("rentername"));
            email.setText(json.getString("email"));
            amountRented.setText("Rented: " + json.getInt("rented"));
        } catch (Exception e){
            System.out.println("JSON ISSUE");
        }


        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Insert click here for click on item card
                System.out.println("Card have been clicked!");
            }
        });
        return card;
    }
}
