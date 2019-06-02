package com.example.schmi.bachelor.Fragments.SubFragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.schmi.bachelor.R;
import com.example.schmi.bachelor.Services;
import com.example.schmi.bachelor.Values;

import org.json.JSONArray;
import org.json.JSONException;


public class RentedFragment extends Fragment {

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rented, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        listView = view.findViewById(R.id.listView);

        new getRentedJSONS().execute();

    }

    private class getRentedJSONS extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
        JSONArray jsonArray;

        @Override
        protected  void onPreExecute(){
            dialog = ProgressDialog.show(getContext(), "Retrieving lent items","Loading. Please wait...");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String username = Values.getInstance().getUsername();
            String data = Services.getAPI("Rents.php?username=" + username);
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

            RentedCardAdapter cardAdapter = new RentedCardAdapter(getContext(), jsonArray);
            listView.setAdapter(cardAdapter);

            if(jsonArray.length() == 0){
                listView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_sentiment_dissatisfied_black_24dp));
            }

            dialog.dismiss();
        }

    }
}
