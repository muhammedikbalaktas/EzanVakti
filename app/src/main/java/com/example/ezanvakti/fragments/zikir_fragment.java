package com.example.ezanvakti.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ezanvakti.R;
import com.example.ezanvakti.classes.MyAdapter;
import com.example.ezanvakti.classes.MyGsonManager;
import com.example.ezanvakti.classes.ZikirData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class zikir_fragment extends Fragment {

    MyGsonManager myGsonManager=new MyGsonManager();
    RecyclerView recyclerView;
    ArrayList<ZikirData> data;
    MyAdapter myAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_zikir_fragment, container, false);

        Button btn_cancel=view.findViewById(R.id.btn_cancelData);
        Button btn_addData=view.findViewById(R.id.btn_addData);
        ImageButton btn_add=view.findViewById(R.id.img_addZikir);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showExpandable();
            }
        });

        btn_addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addData();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    cancelData();
            }
        });


        initRecycler(view);

        isZikirFileCreated();

        return view;
    }
    void initRecycler(View view){
        recyclerView=view.findViewById(R.id.my_recyclerView);
        data=myGsonManager.getZikirData(getContext());
        myAdapter=new MyAdapter(data,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getParent()));
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("myLog","data updated on fragment");
        initRecycler(getView());
    }

    void addData(){
            EditText et_zikirTitle=getView().findViewById(R.id.et_zikirName);
            EditText et_zikirGoal=getView().findViewById(R.id.et_zikirGoal);
            if(et_zikirTitle.getText().toString().isEmpty()||et_zikirGoal.getText().toString().isEmpty()){
                Toast.makeText(getContext(), "some places are empty", Toast.LENGTH_LONG).show();
            }
            else {
                String name=et_zikirTitle.getText().toString();
                int goal=Integer.parseInt(et_zikirGoal.getText().toString());
                myAdapter.addItem(new ZikirData(name,goal));
                hideExpandable();

            }


    }
    public void cancelData(){
            hideExpandable();

    }
    public void showExpandable(){
        ConstraintLayout constraintLayout=getView().findViewById(R.id.zikir_expandable);
        constraintLayout.setVisibility(View.VISIBLE);
    }
    public void hideExpandable(){
        EditText et_zikirTitle=getView().findViewById(R.id.et_zikirName);
        EditText et_zikirGoal=getView().findViewById(R.id.et_zikirGoal);
        et_zikirTitle.setText("");
        et_zikirGoal.setText("");
        ConstraintLayout constraintLayout=getView().findViewById(R.id.zikir_expandable);
        constraintLayout.setVisibility(View.GONE);
        hideKeyboardFrom(getContext(),getView());

    }

    public void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }




    public void isZikirFileCreated(){

        File file=new File("/data/user/0/com.example.ezanvakti/files/Zikirler.json");
        if(file.exists()){

            Log.d("myLog->Zikir","Zikirler File exist");

        }
        else{
            Log.d("myLog->Zikir","File Created");
            try {
                if(file.createNewFile()){
                    Log.d("myLog","File created");
                    ArrayList<ZikirData> data=new ArrayList<>();
                    /*data.add(new ZikirData("Example1",1000));
                    data.add((new ZikirData("Example2",500)));
                    data.add(new ZikirData("Example3",33));*/
                    myGsonManager.setZikirData(getContext(),data);

                }
            } catch (IOException e) {
                Log.d("myLog",e.toString());
            }
        }

    }



}