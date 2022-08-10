package com.example.ezanvakti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import com.example.ezanvakti.classes.AppStatus;
import com.example.ezanvakti.classes.CityData;
import com.example.ezanvakti.classes.MyGsonManager;
import com.example.ezanvakti.classes.PrayerTimes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SelectCityName extends AppCompatActivity {
    public String selectedCity;

    MyGsonManager myGsonManager=new MyGsonManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city_name);
        AppStatus appStatus=new AppStatus();
        appStatus.setCitySelected(false);
        myGsonManager.setAppStatus(getApplicationContext(),appStatus);


        String []arr={"Diyarbakır","Ankara","Istanbul"};

        Spinner spinner=findViewById(R.id.spn_selectCity);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this,   R.layout.text_for_spinner, arr);

        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCity=arr[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button btn_select=(Button) findViewById(R.id.btn_select_city);
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppStatus appStatus=new AppStatus();
                appStatus.setCitySelected(true);
                myGsonManager.setAppStatus(getApplicationContext(),appStatus);
                getDataFromApi(selectedCity);


            }
        });

    }
    public void getDataFromApi(String cityName){
        ProgressDialog progressDialog=new ProgressDialog(SelectCityName.this);
        progressDialog.show();
        LocalDate localDate=LocalDate.now();


        HttpUrl.Builder url=
                HttpUrl.parse
                        ("https://muslimsalat.p.rapidapi.com/(location)/(times)/(date)/(daylight)/(method).json").newBuilder();
        url.addQueryParameter("times","weekly");
        url.addQueryParameter("date","2022-07-28");
        url.addQueryParameter("location",cityName);
        String myUrl=url.build().toString();

        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url(myUrl)
                .get()
                .addHeader("X-RapidAPI-Key", "fe969f7600msh66b179c9495cefep1a0024jsn1d378f194d3d")
                .addHeader("X-RapidAPI-Host", "muslimsalat.p.rapidapi.com").build();


        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("myError->selectCity(okhttp)",e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                SelectCityName.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {




                        try {
                            String result = response.body().string();
                            myGsonManager.setCityData(getApplicationContext(),parseDataToJsonFile(result));


                        } catch (IOException | JSONException | ParseException e) {
                            Log.e("myError->selectCity",e.toString());
                        }

                        progressDialog.dismiss();
                        Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });



    }

    private CityData parseDataToJsonFile(String data) throws JSONException, ParseException {
        JSONObject jsonObject=new JSONObject(data);

        CityData cityData=new CityData();
        cityData.setName(selectedCity);
        PrayerTimes []prayerTimes=new PrayerTimes[7];
        JSONArray jsonArray=jsonObject.getJSONArray("items");

        for(int i=0;i<7;i++){

            JSONObject prayerData=jsonArray.getJSONObject(i);
            prayerTimes[i]=new PrayerTimes();
            String date=prayerData.getString("date_for");
            prayerTimes[i].setDate(convertDate(date));
            prayerTimes[i].setSabah(convertTo24(prayerData.getString("fajr")));
            prayerTimes[i].setGüneşDoğumu(convertTo24(prayerData.getString("shurooq")));
            prayerTimes[i].setÖğle(convertTo24(prayerData.getString("dhuhr")));
            prayerTimes[i].setIkindi(convertTo24(prayerData.getString("asr")));
            prayerTimes[i].setAkşam(convertTo24(prayerData.getString("maghrib")));
            prayerTimes[i].setYatsı(convertTo24(prayerData.getString("isha")));



        }
        cityData.setPrayerTimes(prayerTimes);


        return cityData;
    }

    public static String convertTo24(String input) throws ParseException {


        SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");


        return date24Format.format(date12Format.parse(input));
    }
    public String convertDate(String input){




        try {
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(input);
            String date1=dateFormat.format(date);
            return date1;
        } catch (ParseException e) {
            Log.d("myError->SelectCity",e.toString());
            return null;
        }


    }



}