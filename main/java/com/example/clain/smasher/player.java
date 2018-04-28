package com.example.clain.smasher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class player extends AppCompatActivity {


    private  Button buttonName;

    public static String name;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);



    }





    public void Ok (View v){

        EditText editName =(EditText)findViewById(R.id.edittext);

        // on recupere le pseudo du joueur
        name = editName.getText().toString();

        System.out.println(name);


        startActivity(new Intent(this, GameActivity.class));
    }






}