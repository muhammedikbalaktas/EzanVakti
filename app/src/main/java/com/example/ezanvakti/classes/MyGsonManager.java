package com.example.ezanvakti.classes;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyGsonManager {







    public void setCityData(Context context,CityData cityData){
        try {

            GsonBuilder gsonBuilder=new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            Gson gson=gsonBuilder.create();

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("cityData.json", Context.MODE_PRIVATE));

            outputStreamWriter.write(gson.toJson(cityData));
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Toast.makeText(context, "error "+e, Toast.LENGTH_LONG).show();


        }

    }

    public JSONObject getCityData(Context context) throws JSONException {
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("cityData.json");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (IOException e) {
            Toast.makeText( context, "error "+e, Toast.LENGTH_LONG).show();


        }
        return new JSONObject(ret);

    }
    public JSONObject getAppStatus(Context context) throws JSONException {
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("appStatus.json");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (IOException e) {
            Log.d("myError->myGson->getAppStatus",e.toString());



        }
        return new JSONObject(ret);

    }

    public void setAppStatus(Context context,AppStatus appStatus){
        try {

            GsonBuilder gsonBuilder=new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            Gson gson=gsonBuilder.create();

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("appStatus.json", Context.MODE_PRIVATE));

            outputStreamWriter.write(gson.toJson(appStatus));
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.d("myError->MyGson",e.toString());


        }

    }

    public void setZikirData(Context context, ArrayList<ZikirData> data){
        try {

            GsonBuilder gsonBuilder=new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            Gson gson=gsonBuilder.create();

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Zikirler.json", Context.MODE_PRIVATE));


                outputStreamWriter.write(gson.toJson(data));
                outputStreamWriter.close();

        }
        catch (IOException e) {
            Log.d("myError->MyGson",e.toString());


        }

    }
    public ArrayList<ZikirData> getZikirData(Context context){
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("Zikirler.json");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (IOException e) {
            Log.d("myLog","error arraylist");


        }
        Gson gson=new Gson();

        Type listType = new TypeToken<ArrayList<ZikirData>>(){}.getType();

        ArrayList<ZikirData> list = gson.fromJson(ret, listType);

        return list;

    }

    public void setAyetData(Context context,AyetData data){

        try {

            GsonBuilder gsonBuilder=new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            Gson gson=gsonBuilder.create();

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("AyetData.json", Context.MODE_PRIVATE));


            outputStreamWriter.write(gson.toJson(data));
            outputStreamWriter.close();

        }
        catch (IOException e) {
            Log.d("myError->MyGson",e.toString());


        }

    }
    public AyetData getAyetData(Context context){
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("AyetData.json");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (IOException e) {
            Log.d("myLog","error arraylist");


        }

        Gson gson=new Gson();

        AyetData ayetData=gson.fromJson(ret,AyetData.class);


        return ayetData;

    }







}
