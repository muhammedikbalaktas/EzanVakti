package com.example.ezanvakti;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.ezanvakti.classes.MyGsonManager;
import com.example.ezanvakti.classes.ZikirData;

import java.util.ArrayList;

public class ZikirCekActivity extends AppCompatActivity {
    int okunan;
    int pos;
    MyGsonManager myGsonManager=new MyGsonManager();
    ArrayList<ZikirData> data;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zikir_cek);
        TextView tv_info=findViewById(R.id.tv_info);
        tv_info.setVisibility(View.VISIBLE);
        Vibrator vibrator=(Vibrator) getSystemService(MainActivity.VIBRATOR_SERVICE);

        VibrationEffect tik=VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK);
        VibrationEffect tiiiik=VibrationEffect.createOneShot(1300,1);
        data= myGsonManager.getZikirData(getApplicationContext());
        initIntent(data);

        TextView tv_tamamlanan=findViewById(R.id.tv_okunan);

        ConstraintLayout constraintLayout=findViewById(R.id.constraint_click);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vibrator.cancel();
                vibrator.vibrate(tik);
                TextView tv_info=findViewById(R.id.tv_info);
                tv_info.setVisibility(View.INVISIBLE);
                okunan++;
                tv_tamamlanan.setText(""+okunan);


            }
        });
        constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                vibrator.cancel();
                vibrator.vibrate(tiiiik);
                Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("hasValue","zikir");
                startActivity(intent);
                return true;
            }
        });


    }
    public void initIntent(ArrayList<ZikirData> data){
        Intent intent=getIntent();


        if(intent.hasExtra("pos")){

            pos=intent.getIntExtra("pos",0);
            okunan=data.get(pos).getTamamlanan();

            TextView tv_title=findViewById(R.id.tv_titleZikirCek);
            tv_title.setText(data.get(pos).getIsim());

            TextView tv_hedef=findViewById(R.id.tv_hedef);
            tv_hedef.setText("Hedef: "+data.get(pos).getHedef());

            TextView tv_tamamlanan=findViewById(R.id.tv_okunan);
            tv_tamamlanan.setText(""+okunan);




        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
        Log.d("myLog","data saved while stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveData();

        Log.d("myLog","data saved while destroyed");

    }
    public void saveData(){
        data.get(pos).setTamamlanan(okunan);
        myGsonManager.setZikirData(getApplicationContext(),data);
    }
}