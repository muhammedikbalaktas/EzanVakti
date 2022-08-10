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
import android.view.textclassifier.TextLinks;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ezanvakti.MainActivity;
import com.example.ezanvakti.R;
import com.example.ezanvakti.classes.AyetData;
import com.example.ezanvakti.classes.CityData;
import com.example.ezanvakti.classes.MyGsonManager;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ayet_fragment extends Fragment {

    MyGsonManager myGsonManager=new MyGsonManager();
    /*TextView tv_arabic=view.findViewById(R.id.tv_arabicText);
        TextView tv_english=view.findViewById(R.id.tv_englishText);

        AyetData ayetData=myGsonManager.getAyetData(getContext());

        tv_arabic.setText(ayetData.getContent());

        tv_english.setText(ayetData.getTranslationEN());*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_ayet_fragment, container, false);




        isAyetFileCreated(view);






        return view;
    }

    public void getData(View view){
        ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.show();
        int ayet=getRandomNumber();
        OkHttpClient okHttpClient=new OkHttpClient();
        String url="https://al-quran1.p.rapidapi.com/2/"+ayet;

        Request request= new Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Key", "fe969f7600msh66b179c9495cefep1a0024jsn1d378f194d3d")
                .addHeader("X-RapidAPI-Host", "al-quran1.p.rapidapi.com")
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("myLog->Ayet",e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String data=response.body().string();
                                parseDataToJson(data,view);
                            } catch (IOException | JSONException e) {
                                Log.d("myLog->Ayet",e.toString());
                            }
                            progressDialog.dismiss();

                        }
                    });
            }
        });


        progressDialog.dismiss();
    }

    public void parseDataToJson(String data,View view) throws JSONException {
        JSONObject jsonObject=new JSONObject(data);

        AyetData ayetData=new AyetData();
        LocalDate localDate=LocalDate.now();

        ayetData.setDate(localDate.toString());
        ayetData.setId(""+jsonObject.getDouble("id"));

        ayetData.setContent(jsonObject.getString("content"));
        ayetData.setTranslationEN(jsonObject.getString("translation_eng"));

        myGsonManager.setAyetData(getContext(),ayetData);

        TextView tv_arabic=view.findViewById(R.id.tv_arabicText);
        TextView tv_english=view.findViewById(R.id.tv_englishText);
        tv_arabic.setText(ayetData.getContent());
        tv_english.setText(ayetData.getTranslationEN());


        Log.d("myLog->Ayet","data parsed to json file succesfully");


    }
    public void isAyetFileCreated(View view){
        File file=new File("/data/user/0/com.example.ezanvakti/files/AyetData.json");

        if(!file.exists()){
            try {
                file.createNewFile();
                getData(view);
                Log.d("myLog"," ayet File created");
            } catch (IOException e) {
                Log.d("myLog",e.toString());
            }


        }
        else {
            if (!isUpToDate()) getData(view);
            else {
                AyetData ayetData= myGsonManager.getAyetData(getContext());
                TextView tv_arabic=view.findViewById(R.id.tv_arabicText);
                TextView tv_english=view.findViewById(R.id.tv_englishText);
                tv_arabic.setText(ayetData.getContent());
                tv_english.setText(ayetData.getTranslationEN());
            }
        }


    }


    public boolean isUpToDate(){
        AyetData ayetData=myGsonManager.getAyetData(getContext());

        LocalDate date=LocalDate.parse(ayetData.getDate());
        LocalDate today=LocalDate.now();
        if(today.isAfter(date)){
            Toast.makeText(getContext(), "data is not up to date", Toast.LENGTH_LONG).show();
            Log.d("myLog->ayet","data is not up to date");
            return false;

        }
        Log.d("myLog->ayet","data is up to date");
        return true;

    }

    public int getRandomNumber(){
        Random random=new Random();
        int result=random.nextInt(286);
        result++;

        return result;
    }
}