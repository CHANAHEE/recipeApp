package com.cha.myrecipeapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cha.myrecipeapp.R;

import java.util.ArrayList;

public class BookmarkFragment extends Fragment {

    ArrayList<Item> items = new ArrayList<>();
    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    SQLiteDatabase database,databaseSelect;
    Context context;
    TextView tv;
    MainActivity mainActivity;
    int idx;
    public BookmarkFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookmark,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv = view.findViewById(R.id.tv_noitem);
        recyclerView = view.findViewById(R.id.recyclerview_bookmark);
        adapter = new RecyclerAdapter(getActivity(),items);
        Log.i("adapter","Bookmark Adapter");



        new Thread(){
            @Override
            public void run() {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        databaseSelect = SQLiteDatabase.openDatabase("/data/data/com.cha.myrecipeapp/databases/selecteditem.db",null,SQLiteDatabase.OPEN_READONLY);
                        Cursor cursorSelected = databaseSelect.rawQuery("SELECT * FROM selecteditem",null);
                        recyclerView.setVisibility(View.VISIBLE);
                        tv.setVisibility(View.GONE);
                        if(cursorSelected.getCount() == 0){
                            recyclerView.setVisibility(View.GONE);
                            tv.setVisibility(View.VISIBLE);
                            return;
                        }

                        database = SQLiteDatabase.openDatabase("/data/data/com.cha.myrecipeapp/databases/recipe.db",null,SQLiteDatabase.OPEN_READONLY);
                        Cursor cursor = database.rawQuery("SELECT * FROM recipe",null);

                        Log.i("sel",cursorSelected.getCount()+"");


                        if(cursor == null || cursorSelected == null) return;

                        int row = cursorSelected.getCount();
                        cursorSelected.moveToFirst();

                        Log.i("cursor",cursorSelected.getInt(1) +"");
                        items.clear();
                        for(int i=0;i<row;i++){

                            cursor.moveToPosition(cursorSelected.getInt(1));
                            Item item = new Item();
                            item.index = cursor.getInt(0);
                            item.title = cursor.getString(1);
                            item.mainImg = cursor.getString(2);
                            item.hash = cursor.getString(3);



                            items.add(item);

                            Log.i("cursor",cursorSelected.getInt(1) +"");
                            cursorSelected.moveToNext();

                        }


                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();


        recyclerView.setAdapter(adapter);
    }
}
