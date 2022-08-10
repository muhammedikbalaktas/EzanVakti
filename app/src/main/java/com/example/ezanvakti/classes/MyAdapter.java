package com.example.ezanvakti.classes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezanvakti.R;
import com.example.ezanvakti.ZikirCekActivity;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    ArrayList<ZikirData> data;
    Context context;
    protected MyGsonManager myGsonManager=new MyGsonManager();

    public MyAdapter(ArrayList<ZikirData> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        int pos=position;
        holder.check_finished.setChecked(data.get(pos).isChecked());
        if(holder.check_finished.isChecked()){

            holder.img_delete.setVisibility(View.VISIBLE);
        }
        else{
            holder.img_delete.setVisibility(View.INVISIBLE);
        }
        if(data.get(pos).isExpanded()){
            holder.expandable.setVisibility(View.VISIBLE);
        }
        else {
            holder.expandable.setVisibility(View.GONE);
        }
        String hedef="Hedef: "+data.get(pos).getHedef();
        String tamamlanan="Tamam: "+data.get(pos).getTamamlanan();
        holder.tv_hedef_zikir.setText(hedef);
        holder.tv_kalan_zikir.setText(tamamlanan);
        holder.tv_title_zikir.setText(data.get(pos).getIsim());
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(pos);
            }
        });

        holder.tv_title_zikir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startZikirCek(pos);
            }
        });

        changeExpandable(holder,pos);
        changeIsSelected(holder,pos);

    }
    public void addItem(ZikirData zikirData){
        data.add(zikirData);
        notifyItemInserted(data.size());
        saveData();
        data= myGsonManager.getZikirData(context);
        notifyItemRangeChanged(0,data.size());
    }
    public void startZikirCek(int pos){



        Intent intent=new Intent(context, ZikirCekActivity.class);
        intent.putExtra("pos",pos);

        context.startActivity(intent);

    }
    public void deleteItem(int pos){
        data.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(0,data.size());
        saveData();

    }
    protected void changeIsSelected(MyViewHolder holder,int pos){
        holder.check_finished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.check_finished.isChecked()){
                    data.get(pos).setChecked(true);
                    holder.img_delete.setVisibility(View.VISIBLE);
                    saveData();
                }
                else {
                    data.get(pos).setChecked(false);
                    holder.img_delete.setVisibility(View.INVISIBLE);
                    saveData();
                }
            }
        });

    }
    protected void changeExpandable(MyViewHolder holder,int pos){

        holder.img_expand_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.expandable.setVisibility(View.VISIBLE);
                holder.img_expand_more.setVisibility(View.INVISIBLE);
                holder.img_expand_less.setVisibility(View.VISIBLE);
                data.get(pos).setExpanded(true);
            }
        });
        holder.img_expand_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.expandable.setVisibility(View.GONE);
                holder.img_expand_less.setVisibility(View.INVISIBLE);
                holder.img_expand_more.setVisibility(View.VISIBLE);
                data.get(pos).setExpanded(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title_zikir,tv_hedef_zikir,tv_kalan_zikir;
        ConstraintLayout expandable;
        CheckBox check_finished;
        ImageButton img_delete,img_expand_more, img_expand_less;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title_zikir=itemView.findViewById(R.id.tv_zikir_title);
            tv_hedef_zikir=itemView.findViewById(R.id.tv_hedef_zikir);
            tv_kalan_zikir=itemView.findViewById(R.id.tv_kalan_zikir);
            check_finished=itemView.findViewById(R.id.btn_check_is_finished);
            img_delete=itemView.findViewById(R.id.btn_delete_row);
            img_expand_more=itemView.findViewById(R.id.btn_expand_more);
            img_expand_less =itemView.findViewById(R.id.btn_expand_less);
            expandable=itemView.findViewById(R.id.expandable_constraint_layout);

        }
    }
    protected void saveData(){
        myGsonManager.setZikirData(context,data);
    }
}
