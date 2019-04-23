package com.example.schmi.bachelor.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.schmi.bachelor.Fragments.SubFragments.ItemsFragment;
import com.example.schmi.bachelor.R;
import com.example.schmi.bachelor.Fragments.SubFragments.RentedFragment;
import com.example.schmi.bachelor.Fragments.SubFragments.RentersFragment;


public class ViewFragment extends Fragment {

    Button viewRentedBtn, viewRentersBtn, viewItemsBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        viewItemsBtn = view.findViewById(R.id.addDeviceButton);
        viewRentedBtn = view.findViewById(R.id.viewRentedButton);
        viewRentersBtn = view.findViewById(R.id.addRenterButton);

        viewItemsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().replace(R.id.fragmentChild, new ItemsFragment()).commit();
                viewItemsBtn.setTextColor(getResources().getColor(R.color.colorAccent));
                viewRentersBtn.setTextColor(getResources().getColor(R.color.inactiveButton));
                viewRentedBtn.setTextColor(getResources().getColor(R.color.inactiveButton));
            }
        });

        viewRentersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().replace(R.id.fragmentChild, new RentersFragment()).commit();
                viewItemsBtn.setTextColor(getResources().getColor(R.color.inactiveButton));
                viewRentersBtn.setTextColor(getResources().getColor(R.color.colorAccent));
                viewRentedBtn.setTextColor(getResources().getColor(R.color.inactiveButton));
            }
        });

        viewRentedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().replace(R.id.fragmentChild, new RentedFragment()).commit();
                viewItemsBtn.setTextColor(getResources().getColor(R.color.inactiveButton));
                viewRentersBtn.setTextColor(getResources().getColor(R.color.inactiveButton));
                viewRentedBtn.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        });
    }
}
