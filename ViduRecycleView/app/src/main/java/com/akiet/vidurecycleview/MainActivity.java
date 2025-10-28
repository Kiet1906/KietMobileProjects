package com.akiet.vidurecycleview;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LandScapeAdapter landScapeAdapter;
    ArrayList<LandScape> recyclerViewDatas;
    RecyclerView recyclerViewLandScape;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //3
        recyclerViewDatas = getDataForRecyclerView();
        //4
        recyclerViewLandScape = findViewById(R.id.recyclerland);
        //5
//        RecyclerView.LayoutManager layoutLinear = new LinearLayoutManager(this);
//        recyclerViewLandScape.setLayoutManager(layoutLinear);
//        RecyclerView.LayoutManager layoutLinearHorizontal = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        recyclerViewLandScape.setLayoutManager(layoutLinearHorizontal);
        RecyclerView.LayoutManager layoutGrid = new GridLayoutManager(this, 2);
        recyclerViewLandScape.setLayoutManager(layoutGrid);
        //6
        landScapeAdapter = new LandScapeAdapter(this,recyclerViewDatas);
        //7
        recyclerViewLandScape.setAdapter(landScapeAdapter);
    }

    ArrayList<LandScape> getDataForRecyclerView () {
        ArrayList<LandScape> dsDuLieu = new ArrayList<LandScape>();
        LandScape landScape1 = new LandScape("flag_tower_of_hanoi", "Cột cờ Hà Nội");
        dsDuLieu.add(landScape1);
        dsDuLieu.add(new LandScape("effel_tower", "Tháp Effel"));
        dsDuLieu.add(new LandScape("buckingham", "Cung điện Buckingham"));
        dsDuLieu.add(new LandScape("statue_of_liberty", "Tượng Nữ thần Tự Do"));
        dsDuLieu.add(new LandScape("statue_of_liberty", "Tượng Nữ thần Tự Do"));
        dsDuLieu.add(new LandScape("statue_of_liberty", "Tượng Nữ thần Tự Do"));
        return dsDuLieu;
    }
}