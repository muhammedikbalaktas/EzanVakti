package com.example.ezanvakti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.ezanvakti.classes.AppStatus;
import com.example.ezanvakti.classes.AyetData;
import com.example.ezanvakti.classes.MyGsonManager;
import com.example.ezanvakti.databinding.ActivityMainBinding;
import com.example.ezanvakti.fragments.ayet_fragment;
import com.example.ezanvakti.fragments.home_fragment;
import com.example.ezanvakti.fragments.settings_fragment;
import com.example.ezanvakti.fragments.zikir_fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    MyGsonManager myGsonManager=new MyGsonManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isStatusFileCreated();


        replaceFragment(new home_fragment());
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.saatler);
        initIntent();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.menu_zikir) replaceFragment(new zikir_fragment());
                else if(item.getItemId()==R.id.saatler) replaceFragment(new home_fragment());
                else if(item.getItemId()==R.id.ayet) replaceFragment(new ayet_fragment());
                else if(item.getItemId()==R.id.seçenekler) replaceFragment(new settings_fragment());


                return true;
            }
        });


    }
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();

    }

    public void initIntent(){
        Intent intent=getIntent();
        if(intent.hasExtra("hasValue")){

            String path=intent.getStringExtra("hasValue");

            if(path.equals("zikir")){
                replaceFragment(new zikir_fragment());
                BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation_view);
                bottomNavigationView.setSelectedItemId(R.id.menu_zikir);
            }
            else {
                replaceFragment(new ayet_fragment());
                BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation_view);
                bottomNavigationView.setSelectedItemId(R.id.ayet);
            }

        }

    }



    @Override
    protected void onResume() {
        super.onResume();
        isStatusFileCreated();


    }
    public void isStatusFileCreated(){

        File file=new File("/data/user/0/com.example.ezanvakti/files/appStatus.json");
        if(file.exists()){


        }
        else{

            AppStatus appStatus=new AppStatus();
            appStatus.setCitySelected(false);
            myGsonManager.setAppStatus(getApplicationContext(),appStatus);
        }
        isCitySelected();
    }
    public void isCitySelected(){
        try {
            JSONObject jsonObject= myGsonManager.getAppStatus(getApplicationContext());
            boolean stat=jsonObject.getBoolean("citySelected");
            if(stat==false){

                Intent intent=new Intent(getApplicationContext(),SelectCityName.class);
                startActivity(intent);

            }


        } catch (JSONException e) {
            Log.d("myError->Main->isCitySelected",e.toString());
        }

    }





}