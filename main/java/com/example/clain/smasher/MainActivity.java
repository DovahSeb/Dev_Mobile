package com.example.clain.smasher;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Bouton play
    private Button buttonPlay;
    // Bouton Score
    private  Button buttonScore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Récupération des ID des  boutons
        buttonPlay = (Button) findViewById(R.id.buttonPlay);
        buttonScore=(Button) findViewById(R.id.buttonScore);



        //Ajout évenement sur les boutons
        buttonPlay.setOnClickListener(this);
        buttonScore.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.buttonPlay:
                //On lance le  game activity
                startActivity(new Intent(this, player.class));
                //startActivity(new Intent(this, GameActivity.class));

                break;

            case R.id.buttonScore:
                //debug
                System.out.println("Score");

                // On lance le score
                startActivity(new Intent(this, Score.class));

                break;

            default:
                break;

        }



}


}