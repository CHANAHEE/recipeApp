package com.cha.myrecipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cha.myrecipeapp.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    ArrayList<Item> items = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerGridLayoutAdapter adapter;
    ImageButton imgBtn;
    ExtendedFloatingActionButton fab;
    String[] ingreNames = {"토마토","가지","피망","브로콜리","양배추","당근","버섯","고추","오이","연근"
                            ,"마늘","생강","레몬","양파","오렌지","블루베리","파인애플","감자","호박","무"
                            ,"콩","시금치","대파","딸기","고구마","애호박","소고기","양고기","돼지고기","닭고기"
                            ,"오리고기","생선","햄","소세지","빵가루","옥수수","밀가루","부침가루","밥","누룽지"};

    int[] ingreIcons = {R.drawable.tomato,R.drawable.aubergine,R.drawable.bellpepper,R.drawable.broccoli,R.drawable.cabbage
                        ,R.drawable.carrot,R.drawable.champignon,R.drawable.chilipepper,R.drawable.cucumber,R.drawable.courgette
                        ,R.drawable.garlic,R.drawable.ginger,R.drawable.lemon,R.drawable.onion,R.drawable.orange
                        ,R.drawable.berry,R.drawable.pineapple,R.drawable.potato,R.drawable.pumpkin,R.drawable.radish
                        ,R.drawable.soybean,R.drawable.spinach,R.drawable.springonion,R.drawable.strawberry,R.drawable.sweetpotato
                        ,R.drawable.zucchini,R.drawable.beef,R.drawable.lamb,R.drawable.pork,R.drawable.chicken
                        ,R.drawable.duck,R.drawable.fish,R.drawable.ham,R.drawable.sausage,R.drawable.breadpowder
                        ,R.drawable.corn,R.drawable.flour,R.drawable.pancakepowder,R.drawable.rice,R.drawable.ricechip};
    public HomeFragment() {
        Log.i("icon","아이콘1");
        for(int i=0;i<ingreNames.length;i++){
            items.add(new Item(ingreNames[i],ingreIcons[i]));
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab = view.findViewById(R.id.fab);
        recyclerView = view.findViewById(R.id.recyclerview_home);
        adapter = new RecyclerGridLayoutAdapter(getActivity(),items,fab);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(view1 -> clickFab());
    }
    void clickFab(){
        Intent intent = new Intent(getActivity(), SecondActivity.class);
        startActivity(intent);
    }

}
