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

import org.json.JSONArray;
import org.json.JSONException;


public class ItemsFragment extends Fragment {

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        listView = view.findViewById(R.id.listView);

        new getItemJSONS().execute();

    }

    private class getItemJSONS extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
        JSONArray jsonArray;

        @Override
        protected  void onPreExecute(){
            dialog = ProgressDialog.show(getContext(), "Retrieving items","Loading. Please wait...");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String username = "mikkelschmidt14"; //Get user name
            String data = Services.getAPI("Item.php?username=" + username);
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

            ItemCardAdapter cardAdapter = new ItemCardAdapter(getContext(), jsonArray);
            listView.setAdapter(cardAdapter);

            if(jsonArray.length() == 0){
                listView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_sentiment_dissatisfied_black_24dp));
            }

            dialog.dismiss();
        }

    }

}
