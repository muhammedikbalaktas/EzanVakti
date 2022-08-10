package com.example.ezanvakti.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ezanvakti.MainActivity;
import com.example.ezanvakti.R;
import com.example.ezanvakti.SelectCityName;
import com.example.ezanvakti.classes.CityData;
import com.example.ezanvakti.classes.MyGsonManager;
import com.example.ezanvakti.classes.PrayerTimes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class home_fragment extends Fragment {
    MyGsonManager myGsonManager=new MyGsonManager();
    String selectedCity;
    JSONObject todayData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_home_fragment, container, false);
        parseData(view);
        arrangeTextViews(view);

        return view;
    }
    public void parseData(View view){

        try {

            JSONObject data=myGsonManager.getCityData(getContext());
            selectedCity=data.getString("name");
            if(upToDate(data)){
                parseDataToUI(data,view);

            }
            else {
                Log.d("myLog->decideForDate","data is not up to date");
                getDataFromApi(selectedCity);
                Intent intent=new Intent(getContext(),MainActivity.class);
                startActivity(intent);

            }



        } catch (JSONException e) {
            Log.d("myError->homeFragment",e.toString());
        }


    }

    public boolean upToDate(JSONObject cityData) throws JSONException {

        JSONArray jsonArray=cityData.getJSONArray("prayerTimes");
        LocalDate today=LocalDate.now();

        for (int i=0;i<jsonArray.length();i++){

            String jsonDate=jsonArray.getJSONObject(i).getString("date");
            LocalDate localDate=LocalDate.parse(jsonDate);

            if(today.compareTo(localDate)==0){

                todayData= jsonArray.getJSONObject(i);
                return true;
            }
        }
        todayData=jsonArray.getJSONObject(6);
        Log.d("myLog->decideForDate","data is not up to date");

        return false;
    }
    private void parseDataToUI(JSONObject cityData,View view) throws JSONException {
        TextView tv_title=view.findViewById(R.id.tv_City_Name);
        String cityName=cityData.getString("name");

        tv_title.setText(cityName);



        JSONObject jsonObject= todayData;
        TextView tv_date=view.findViewById(R.id.tv_date);
        tv_date.setText(jsonObject.getString("date"));

        TextView tv_sabah=view.findViewById(R.id.tv_sabah);
        tv_sabah.setText(jsonObject.getString("sabah"));

        TextView tv_güneş=view.findViewById(R.id.tv_güneş);
        tv_güneş.setText(jsonObject.getString("güneşDoğumu"));

        TextView tv_öğle=view.findViewById(R.id.tv_öğle);
        tv_öğle.setText(jsonObject.getString("öğle"));

        TextView tv_ikindi=view.findViewById(R.id.tv_ikindi);
        tv_ikindi.setText(jsonObject.getString("ikindi"));

        TextView tv_akşam=view.findViewById(R.id.tv_aksam);
        tv_akşam.setText(jsonObject.getString("akşam"));

        TextView tv_yatsı=view.findViewById(R.id.tv_yatsı);
        tv_yatsı.setText(jsonObject.getString("yatsı"));
    }

    public void arrangeTextViews(View view){

        LocalTime now=LocalTime.now();
        TextView tv_sabah=view.findViewById(R.id.tv_sabah);
        LocalTime sabah=LocalTime.parse(tv_sabah.getText());
        if(now.isAfter(sabah)){
            tv_sabah.setBackgroundResource(R.drawable.background_for_past_time);

        }

        TextView tv_güneş=view.findViewById(R.id.tv_güneş);
        LocalTime güneş=LocalTime.parse(tv_güneş.getText());
        if(now.isAfter(güneş)){
            tv_güneş.setBackgroundResource(R.drawable.background_for_past_time);
        }

        TextView tv_öğle=view.findViewById(R.id.tv_öğle);
        LocalTime öğle=LocalTime.parse(tv_öğle.getText());
        if(now.isAfter(öğle)){
            tv_öğle.setBackgroundResource(R.drawable.background_for_past_time);
        }

        TextView tv_ikindi=view.findViewById(R.id.tv_ikindi);
        LocalTime ikindi=LocalTime.parse(tv_ikindi.getText());
        if(now.isAfter(ikindi)){
            tv_ikindi.setBackgroundResource(R.drawable.background_for_past_time);
        }

        TextView tv_akşam=view.findViewById(R.id.tv_aksam);
        LocalTime akşam=LocalTime.parse(tv_akşam.getText());
        if(now.isAfter(akşam)){
            tv_akşam.setBackgroundResource(R.drawable.background_for_past_time);
        }

        TextView tv_yatsı=view.findViewById(R.id.tv_yatsı);
        LocalTime yatsı=LocalTime.parse(tv_yatsı.getText());
        if(now.isAfter(yatsı)){
            tv_yatsı.setBackgroundResource(R.drawable.background_for_past_time);
        }

    }

    public void getDataFromApi(String cityName){
        ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.show();
        LocalDate localDate=LocalDate.now();


        HttpUrl.Builder url=
                HttpUrl.parse
                        ("https://muslimsalat.p.rapidapi.com/(location)/(times)/(date)/(daylight)/(method).json").newBuilder();
        url.addQueryParameter("times","weekly");
        url.addQueryParameter("date",localDate.toString());
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

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {




                        try {
                            String result = response.body().string();
                            myGsonManager.setCityData(getContext(),parseDataToJsonFile(result));


                        } catch (IOException | JSONException | ParseException e) {
                            Log.e("myLog->home",e.toString());
                        }

                        progressDialog.dismiss();

                    }
                });

            }
        });



    }
    private CityData parseDataToJsonFile(String data) throws JSONException, ParseException {
        JSONObject jsonObject=new JSONObject(data);

        CityData cityData=new CityData();
        cityData.setName(selectedCity);
        PrayerTimes[]prayerTimes=new PrayerTimes[7];
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