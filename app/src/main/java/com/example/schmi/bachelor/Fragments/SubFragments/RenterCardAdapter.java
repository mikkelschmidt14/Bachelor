package com.example.schmi.bachelor.Fragments.SubFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.schmi.bachelor.R;
import com.example.schmi.bachelor.RenterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RenterCardAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private JSONArray jsons;
    private Context context;

    public RenterCardAdapter(Context c, JSONArray jsons){
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
        View card = mInflater.inflate(R.layout.card_renter, null);

        final TextView renterName = card.findViewById(R.id.renterName);
        final TextView amountRented = card.findViewById(R.id.location);
        final TextView email = card.findViewById(R.id.email);

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

                Intent renterIntent = new Intent(context, RenterActivity.class);

                try {
                    String renterRFID = json.getString("renterRFID");
                    renterIntent.putExtra("renterRFID", renterRFID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Pair p1 = Pair.create(renterName, ViewCompat.getTransitionName(renterName));
                Pair p2 = Pair.create(email, ViewCompat.getTransitionName(email));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, p1, p2);

                context.startActivity(renterIntent, options.toBundle());
            }
        });
        return card;
    }
}
