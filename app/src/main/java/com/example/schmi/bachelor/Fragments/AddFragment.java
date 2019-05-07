package com.example.schmi.bachelor.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.schmi.bachelor.CreateDeviceActivity;
import com.example.schmi.bachelor.CreateItemActivity;
import com.example.schmi.bachelor.CreateRenterActivity;
import com.example.schmi.bachelor.ItemActivity;
import com.example.schmi.bachelor.R;

import org.json.JSONException;


public class AddFragment extends Fragment {

    Button addItemBtn, addRenterBtn, addDeviceBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        addItemBtn = view.findViewById(R.id.addItemButton);
        addRenterBtn = view.findViewById(R.id.addRenterButton);
        addDeviceBtn = view.findViewById(R.id.addDeviceButton);

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent productIntent = new Intent(getContext(), CreateItemActivity.class);

                Pair p1 = Pair.create(addItemBtn, ViewCompat.getTransitionName(addItemBtn));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) getContext(), p1);

                getContext().startActivity(productIntent, options.toBundle());
            }
        });

        addRenterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent productIntent = new Intent(getContext(), CreateRenterActivity.class);

                Pair p1 = Pair.create(addRenterBtn, ViewCompat.getTransitionName(addRenterBtn));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) getContext(), p1);

                getContext().startActivity(productIntent, options.toBundle());
            }
        });

        addDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent productIntent = new Intent(getContext(), CreateDeviceActivity.class);

                Pair p1 = Pair.create(addDeviceBtn, ViewCompat.getTransitionName(addDeviceBtn));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) getContext(), p1);

                getContext().startActivity(productIntent, options.toBundle());
            }
        });

    }

}
