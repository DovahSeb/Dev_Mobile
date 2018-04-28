package com.example.clain.smasher;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;


public class Score extends AppCompatActivity {

    private  ListView ScoreList;
    private MyArrayAdapter adapter;


    private String pseudo;



    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        sharedPreferences = getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);


        ScoreList =(ListView) findViewById(R.id.listView);
        registerForContextMenu(ScoreList);
        ArrayList<DataScore> LScore = new ArrayList<DataScore>();


        pseudo = player.name;

        LScore.add(new DataScore(1,sharedPreferences.getInt("score1",0),pseudo));

        adapter = new MyArrayAdapter(this,LScore);

        ScoreList.setAdapter(adapter);



    }
}
