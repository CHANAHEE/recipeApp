package com.cha.myrecipeapp;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.cha.myrecipeapp.R;

import java.util.ArrayList;

public class AllRecipeFragment extends Fragment {

    ArrayList<Item> items = new ArrayList<>();
    ArrayList<Item> search_items;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    SQLiteDatabase database;
    Context context;
    MainActivity mainActivity;
    EditText et;
    TextView tv;
    Spannable span;

    public AllRecipeFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        new Thread(){
            @Override
            public void run() {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        database = SQLiteDatabase.openDatabase("/data/data/com.cha.myrecipeapp/databases/recipe.db",null,SQLiteDatabase.OPEN_READONLY);
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context= context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_allrecipe,container,false);
        Toolbar toolbar = view.findViewById(R.id.allrecipe_toolbar);
        mainActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = mainActivity.getSupportActionBar();
        actionBar.setTitle("");
        setHasOptionsMenu(true);
        return view;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.allrecipe_toolbarmenu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.search && tv.getTag().equals("visible")){
            tv.setTag("gone");
            tv.setVisibility(View.GONE);
            et.setVisibility(View.VISIBLE);
            et.requestFocus();
            InputMethodManager manager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);

            Log.i("edit","서치 아이템 선택");
            et.addTextChangedListener(watcher);
            et.setText("");

        } else if(item.getItemId() == R.id.search && tv.getTag().equals("gone")){
            tv.setTag("visible");
            tv.setVisibility(View.VISIBLE);
            et.setVisibility(View.GONE);
        }

        return super.onOptionsItemSelected(item);
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.i("edit",s.toString());
            search_items = new ArrayList<>();
            if(s.toString().equals("")){
                adapter = new RecyclerAdapter(getActivity(),items);
            }else{
                for(int i=0;i<items.size();i++){
                    if(items.get(i).title.contains(s.toString())){

                        search_items.add(items.get(i));
                    }
                }
                adapter = new RecyclerAdapter(getActivity(),search_items);

            }
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.i("edit"," 와쳐 내부 2");

        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("edit","리사이클러뷰 뿌리기");
        recyclerView = view.findViewById(R.id.recyclerview_allrecipe);
        adapter = new RecyclerAdapter(getActivity(),items);
        Log.i("adapter","All Recipe Adapter");
        recyclerView.setAdapter(adapter);


        et = view.findViewById(R.id.et);
        tv = view.findViewById(R.id.tv);
        tv.setTag("visible");
        view.findViewById(R.id.fab).setOnClickListener(v -> {
            recyclerView.scrollToPosition(RecyclerView.SCROLLBAR_POSITION_DEFAULT);
        });





    }
}
