package com.cha.myrecipeapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cha.myrecipeapp.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

public class RecyclerGridLayoutAdapter extends RecyclerView.Adapter<RecyclerGridLayoutAdapter.VH> {

    Context context;
    ArrayList<Item> items;
    Boolean isItemSelect = false;
    ExtendedFloatingActionButton fab;
    int checkNum = 0;
    String selectedItem = "";
    public RecyclerGridLayoutAdapter(Context context, ArrayList<Item> items,ExtendedFloatingActionButton fab) {
        this.context = context;
        this.items = items;
        this.fab = fab;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(context).inflate(R.layout.recyclerview_iconmenu,parent,false));
    }

    class SelectItemInfo{
        String name;
        int index;

        public SelectItemInfo(String name, int index) {
            this.name = name;
            this.index = index;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Item item = items.get(position);
        holder.imgBtn.setImageResource(item.icon);
        holder.tv.setText(item.name);
        holder.tv.setTag(new SelectItemInfo(item.name,position));

    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{

        ImageButton imgBtn;
        TextView tv;



        public VH(@NonNull View itemView) {
            super(itemView);
            imgBtn = itemView.findViewById(R.id.icon_imgbtn);
            tv = itemView.findViewById(R.id.icon_title);

            //imgBtn.setOnClickListener(listener);
            tv.setOnClickListener(listener);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("adfbvcsc",selectedItem);
                    context.startActivity(new Intent(context, SecondActivity.class).putExtra("selectedItem",selectedItem));
                }
            });
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectItemInfo info = (SelectItemInfo) v.getTag();
                Log.i("info",info.name);
                Log.i("info",info.index+"");


                if(v.isSelected()){
                    checkNum--;
                    tv.setTextColor(Color.parseColor("#000000"));
                    imgBtn.setAlpha(1f);
                    v.setSelected(false);
                }else{
                    checkNum++;
                    Log.i("adfbvcsc",selectedItem);
                    selectedItem += info.name;
                    Log.i("adfbvcsc",selectedItem);
                    v.setSelected(true);
                    imgBtn.setAlpha(0.1f);
                    tv.setTextColor(Color.parseColor("#D3840F"));
                }


                if(checkNum != 0){
                    fab.setVisibility(View.VISIBLE);
                }else{
                    fab.setVisibility(View.INVISIBLE);
                }

            }
        };
    }
}
