package com.example.ezanvakti.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ezanvakti.R;
import com.example.ezanvakti.SelectCityName;
import com.example.ezanvakti.classes.MyGsonManager;


public class settings_fragment extends Fragment {

    MyGsonManager myGsonManager=new MyGsonManager();





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_settings_fargment, container, false);
        Button btn_reset=view.findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), SelectCityName.class);
                startActivity(intent);
            }
        });
        return view;
    }
}