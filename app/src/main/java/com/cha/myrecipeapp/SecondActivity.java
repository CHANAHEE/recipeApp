package com.cha.myrecipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.example.myrecipeapp.R;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    ArrayList<Item> items = new ArrayList<>();
    SQLiteDatabase database;

    public SecondActivity(){
        new Thread(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        database = SQLiteDatabase.openDatabase("/data/data/com.example.myrecipeapp/databases/recipe.db",null,SQLiteDatabase.OPEN_READONLY);
                        Cursor cursor = database.rawQuery("SELECT * FROM recipe",null);

                        if(cursor == null) return;

                        int row = cursor.getCount();
                        cursor.moveToFirst();

                        for(int i=0;i<row;i++){
                            Item item = new Item();
                            item.index = cursor.getInt(0);
                            Log.i("index",item.index+"");
                            item.title = cursor.getString(1);
                            item.mainImg = cursor.getString(2);
                            item.hash = cursor.getString(3);

                            items.add(item);
                            cursor.moveToNext();
                        }

                        Log.i("adapter","All Recipe Adapter 생성자");
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
}