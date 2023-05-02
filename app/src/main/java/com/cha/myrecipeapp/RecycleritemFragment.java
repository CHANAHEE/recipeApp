package com.cha.myrecipeapp;

import static android.database.sqlite.SQLiteDatabase.openDatabase;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.example.myrecipeapp.R;
import com.bumptech.glide.Glide;

public class RecycleritemFragment extends Fragment {

    Toolbar toolbar;
    MainActivity mainActivity;
    Context context;
    int idx = 0;
    Bundle bundle;
    SQLiteDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        idx = getArguments().getInt("index") - 1;

        return inflater.inflate(R.layout.fragment_recycleritem,container,false);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == 16908332){

            mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,mainActivity.allRecipeFragment).addToBackStack(null).commit();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        mainActivity = (MainActivity) getActivity();
        mainActivity.setSupportActionBar(toolbar);

        ActionBar actionBar = mainActivity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        actionBar.setTitle("");

        ImageButton bookmark_btn = view.findViewById(R.id.bookmark_btn);

        bookmark_btn.setOnClickListener(v -> {
            if(v.isSelected()) {
                v.setSelected(false);
                deleteSelectedItem();
            }
            else {
                v.setSelected(true);
                saveSelectedItem();
            }
        });


        /*
        *
        * 프래그먼트 뷰에 레시피 정보 뿌리기.
        *
        * */
        database = SQLiteDatabase.openDatabase("/data/data/com.example.myrecipeapp/databases/recipe.db",null,SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = database.rawQuery("SELECT * FROM recipe",null);
        cursor.moveToPosition(idx);

        ImageView rcv_img = view.findViewById(R.id.img_recycleritemview);
        TextView rcv_title = view.findViewById(R.id.rcv_item_title);
        TextView rcv_ingre = view.findViewById(R.id.rcv_item_ingre);
        TextView rcv_howto = view.findViewById(R.id.rcv_item_howto);

        String title = cursor.getString(1);
        String img = cursor.getString(2);
        String ingre = cursor.getString(4);
        String howto = cursor.getString(5);
        howto = sortHowto(howto);
        boolean isSelect = seletedItem();

        Glide.with(context).load(img).into(rcv_img);
        rcv_title.setText(title);
        rcv_ingre.setText(ingre);
        rcv_howto.setText(howto);
        bookmark_btn.setSelected(isSelect);
    }

    boolean seletedItem(){
        database = openDatabase("/data/data/com.example.myrecipeapp/databases/selecteditem.db",null,SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = database.rawQuery("SELECT idx FROM selecteditem",null);
        cursor.moveToFirst();
        int row = cursor.getCount();
        Log.i("rowCount",row+"");
        for(int i=0;i<row;i++){
            if(cursor.getInt(0) == idx) return true;
            cursor.moveToNext();
        }

        return false;
    }
    void saveSelectedItem(){
        database = openDatabase("/data/data/com.example.myrecipeapp/databases/selecteditem.db",null,SQLiteDatabase.OPEN_READWRITE);
        database.execSQL("INSERT INTO selecteditem (idx) VALUES (?)",new String[]{ idx+"" });
        Log.i("cursor",idx +"리사이클러뷰 아이템 인덱스");
        // INSERT 수행

    }

    void deleteSelectedItem(){
        database = openDatabase("/data/data/com.example.myrecipeapp/databases/selecteditem.db",null,SQLiteDatabase.OPEN_READWRITE);
        database.execSQL("DELETE FROM selecteditem WHERE idx="+idx);
    }

    String sortHowto(String howto){
        int arrIdx = 0;
        char index = 1;
        String[] result = howto.split("[.]");
        Log.i("split","문자열분류");
        Log.i("split",result.length+"");
        for(int i=0;i<result.length;i++){
            Log.i("split",result[i]);
        }

        String newResult = "";
        for(int i =0;i<result.length;i++){
            for(int k = 0;k<20;k++){
                if(result[i].equals(k+"")){
                    newResult += result[i]+" "+result[i+1]+"\n\n";

                }
            }
        }
        return newResult;
    }
}

