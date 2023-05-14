package com.cha.myrecipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.cha.myrecipeapp.R;

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
                        String selectedItem = getIntent().getStringExtra("selectedItem");
                        Log.i("adfbvcsc",selectedItem);
                        database = SQLiteDatabase.openDatabase("/data/data/com.cha.myrecipeapp/databases/recipe.db",null,SQLiteDatabase.OPEN_READONLY);
                        Log.i("adfbvcsc",selectedItem);
                        Cursor cursor = database.rawQuery("SELECT * FROM recipe WHERE title LIKE '%" + selectedItem +"%'",null);

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

                        Log.i("adapter213","All Recipe Adapter 생성자");
                        adapter = new RecyclerAdapter(SecondActivity.this,items);
                        recyclerView.setAdapter(adapter);
                        Log.i("adapter213",items.size() + "");
                    }
                });
            }
        }.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        recyclerView = findViewById(R.id.recyclerview_useablerecipe);
    }
}