package com.cha.myrecipeapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cha.myrecipeapp.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.VH> {

    Context context;
    ArrayList<Item> items;
    FragmentManager manager;
    Fragment recyclerItemFragment;
    Bundle bundle;
    int pos;
    public RecyclerAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(context).inflate(R.layout.recyclerview_itemview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {


        Item item = items.get(position);
        Glide.with(context).load(item.mainImg).into(holder.iv);
        holder.tv_title.setText(item.title);
        holder.tv_title.setTag(item.index);
        holder.tv_hash.setText(item.hash);
    }

    @Override
    public int getItemCount() {
        Log.i("size",items.size()+"");
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{

        ImageView iv;
        TextView tv_title,tv_hash;

        public VH(@NonNull View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.recyclerview_iv);
            tv_title = itemView.findViewById(R.id.recyclerview_tv_title);
            tv_hash = itemView.findViewById(R.id.recyclerview_tv_hashtag);


            itemView.setOnClickListener(view -> {

                Log.i("adapter1","bookmarkAdapter");
                bundle = new Bundle();
                bundle.putInt("index", (Integer) tv_title.getTag());

                recyclerItemFragment = new RecycleritemFragment();
                recyclerItemFragment.setArguments(bundle);

                manager = ((MainActivity)context).getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.container_fragment,recyclerItemFragment).addToBackStack(null).commit();

            });
        }


    }
}
