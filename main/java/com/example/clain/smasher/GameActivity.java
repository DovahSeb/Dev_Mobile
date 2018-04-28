package com.example.clain.smasher;


        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

public class GameActivity extends AppCompatActivity {

    //declarer gameview
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialisation de gameview
        gameView = new GameView(this);

        //ajout contentview
        setContentView(gameView);




    }

    // Méthode exécutée quand le joueur lance le jeu
    @Override
    protected void onResume() {
        super.onResume();

        // execution de la méthode resume de  gameView
        gameView.resume();


    }

    // Méthode exécuté quand le joueur quitte le jeu
    @Override
    protected void onPause() {
        super.onPause();
        // Exécution de la méthoode pause de gameview
        System.out.println("pause");
        gameView.pause();

    }





}