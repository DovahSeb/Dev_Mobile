package com.example.clain.smasher;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.app.Activity;
import android.util.Log;




public class GameView extends SurfaceView implements Runnable {

    // thread  du jeu
    Thread gameThread = null;

    // Surface du canvas
    SurfaceHolder surfaceHolder;


    // Etat du jeu en cours
    volatile boolean playing;

    // Initialement le jeu est en pause
    boolean paused = true;


    // Initialement
    boolean isGameOver = false;
    boolean isWin = false;

    //objet de dessin
    Paint paint;
    Canvas canvas;

    // frame rate par seconde
    long fps;

    // utiliser pour le  calcul du  frame rate par seconde
    private long timeThisFrame;

    // Taille de l'ecran en pixel
    int screenX;
    int screenY;

    // Platforme
    Platform platform;

    // balle
    Ball ball;

    // initialision
    Brick[] bricks = new Brick[200];

    //  initialisation du nombre de brick
    int numBricks = 0;


    int score=0;
    int highScore =0;




    // sauvegarder des elements
    SharedPreferences sharedPreferences;

    int level=1;

    int IndRow = 3;

    int nextLevel;

    boolean setVisible = true;




    // constructeur
    public GameView(Context context) {
        super(context);


        score = 0;


        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME",context.MODE_PRIVATE);

        highScore = sharedPreferences.getInt("Score1",0);



        // initialisation de la surface du canvas
        surfaceHolder = getHolder();
        surfaceHolder.setFixedSize(screenX, screenY/2);

        paint = new Paint();

        // Permet de recuperer la taille de l'ecran
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();


        // Load the resolution into a Point object
        Point size = new Point();
        display.getSize(size);

        // Taille de l'ecran
        screenX = size.x;
        screenY = size.y;


        // creation de la plateforme
        platform = new Platform(screenX, screenY);

        // Creation de la balle
        ball = new Ball(screenX, screenY);

        // lancement de la methode de creation  des bricks
        createBricksAndRestart();


    }

    public void createBricksAndRestart() {

        // position de la ball
        ball.reset(screenX, screenY/2);

        // taille de chaque briques
        int brickWidth = screenX / 8;
        int brickHeight = screenY / 10;


        numBricks = 0;

        // Creation de briques à chaque passage de niveau
        if(isWin){
            IndRow++;
            for (int column = 0; column < 8; column++) {
                for (int row = 1; row < IndRow; row++) {
                    bricks[numBricks] = new Brick(row, column, brickWidth, brickHeight);
                    numBricks++;

                }

            }
            System.out.println("Nombre de bricks: "+numBricks);

            // Permet de calculer le score pour le passage de niveau
            nextLevel = numBricks+score;

        }

        // Creation des première brique du jeu ( 1 er fois ou lance une partie)
        else{

            for (int column = 0; column < 8; column++) {
                for (int row = 1; row < IndRow; row++) {
                    bricks[numBricks] = new Brick(row, column, brickWidth, brickHeight);
                    numBricks++;
                }
            }

            // calcul le passage au niveau suivant

            nextLevel = numBricks;
        }


    }

    @Override
    public void run() {
        while (playing) {
            // temps en  milliseconds
            long startFrameTime = System.currentTimeMillis();


            if (!paused) {
                setVisible = false;

                // execution en boucle  lors de l'execution du jeu
                update();
            }
            // dessine les differents éléments du jeu
            draw();

            // Calcul du  fps
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }


    public void update() {




        // déplacement de la plateform si nécessaire
        platform.update(fps);
        // déplacement  de la ball
        ball.update(fps);

        // Vérification de la collision de la ball avec chaque brique
        for (int i = 0; i < numBricks; i++) {
            if (bricks[i].getVisibility()) {
                if (RectF.intersects(bricks[i].getRect(), ball.getRect())) {
                    System.out.println("BrickCollision");
                    bricks[i].setInvisible();
                    //  la ball change de trajectoire
                    ball.reverseYVelocity();
                    //  On augmente le score car colllsion avec la brique
                    score++;


                    // Si le score est egale au score requis  pour le prochain niveau
                    if(score == nextLevel){

                        System.out.println("Win !");
                        isWin = true;
                        level++;
                        //ball.yVelocity = ball.yVelocity + 200;
                        ball.xVelocity = ball.xVelocity + 150;

                        // On a 5 niveau au total
                        if(level > 5){


                            playing = false;
                            DrawVictoire();
                        }
                        else{
                            // creation de nouvelles brique
                            createBricksAndRestart();
                        }

                    }


                }
            }
        }

        //  Vérification de collision entre la ball et la plateforme
        if(RectF.intersects(platform.getRect(),ball.getRect())){
            System.out.println("Platform_Collision");
            ball.setRandomXVelocity();
            ball.reverseYVelocity();
            ball.clearObstacleY(platform.getRect().top);
        }




        //  Lorsque que la ball sort de l'ecran du bas
        if (ball.getRect().bottom > screenY) {
            //ball.reverseYVelocity();
            //ball.clearObstacleY(screenY - 2);
            System.out.println("GameOver");
            playing = false;
            isGameOver = true;


            // récupération du score
            highScore = score;
            SharedPreferences.Editor e = sharedPreferences.edit();
            e.putInt("score1",highScore);
            e.apply();




        }

        // Collision avec le haut de l'ecran
        if (ball.getRect().top < 0) {
            if (ball.y()<0){
                ball.reverseYVelocity();
                ball.clearObstacleY(10);
            }
        }

        // Collision avec le coté gauche de l'ecran
        boolean collison;
        if (ball.getRect().left < 0) {
            ball.reverseXVelocity();
            ball.clearObstacleX(2);

        }
        // Collision avec le coté droit de l'ecran
        if (ball.getRect().right > screenX - 10) {
            ball.reverseXVelocity();
            ball.clearObstacleX(screenX - 40);


        }

        // Si la plateforme sort de l'ecran
        if(platform.getRect().right > screenX){
            // on inverse le déplacement de la plateforme
            platform.setMovementState(platform.LEFT);

        }
        if(platform.getRect().left <0){
            platform.setMovementState(platform.RIGHT);

        }


    }




    public void draw() {


        // Vérification de la surface du canvas
        if (surfaceHolder.getSurface().isValid()) {
            // on verrouille le canvas afin de déssiner
            canvas = surfaceHolder.lockCanvas();

            // Couleur de fond
            canvas.drawColor(Color.argb(255, 0, 128, 192));

            // on choisit  la couleur
            paint.setColor(Color.argb(255, 255, 255, 255));

            // Dessiner la plateforme
            canvas.drawRect(platform.getRect(), paint);

            // on dessine la balle
            canvas.drawRect(ball.getRect(), paint);

            // Dessin  du texte
            paint.setColor(Color.argb(255, 0, 0, 0));
            paint.setTextSize(50);
            canvas.drawText("score: "+score,0,50,paint);

            paint.setColor(Color.argb(255, 0, 0, 0));
            paint.setTextSize(50);
            canvas.drawText("Level: "+level,screenX/2,50,paint);

            //  Affichage texte de lancement du jeu
            if(setVisible){
                paint.setColor(Color.argb(255, 0, 0, 0));
                paint.setTextSize(65);
                paint.setFakeBoldText(true);
                canvas.drawText("Tap to play ",screenX/4,screenY/2,paint);
            }




            // change la couleur de déssin
            paint.setColor(Color.argb(255, 0, 0, 0));


            // Dessin des bricks si elle sont visible
            for(int i = 0; i < numBricks; i++){
                if(bricks[i].getVisibility()) {
                    canvas.drawRect(bricks[i].getRect(), paint);
                }
            }

            // on déverouille le canvas
            surfaceHolder.unlockCanvasAndPost(canvas);

        }

        // si on perd
        if(isGameOver){
           DrawDefaite();
           //pause();


        }



    }


    // affichage element de victoire
    public void DrawVictoire() {

        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            paint.setColor(Color.argb(255, 0, 0, 0));
            paint.setTextSize(50);
            paint.setFakeBoldText(true);
            canvas.drawText("Bravo  ",screenX/3,screenY/2,paint);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }

    // affichage element de defaite
    public  void  DrawDefaite(){
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            paint.setColor(Color.argb(255, 0, 0, 0));
            paint.setTextSize(50);
            paint.setFakeBoldText(true);
            canvas.drawText("GameOver !",screenX/3,screenY/2,paint);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }


    // Arret des thread  et arret du jeu
    public void pause() {
        playing = false;
        System.out.println("Le jeu est en pause");
        paused = false;

        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }







    }


    //Lancement du thread
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();

    }

    //  Lorsque le joueur touche l'ecran
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            // Le joueur touche l'ecran

            case MotionEvent.ACTION_DOWN:
                paused = false;
                if (motionEvent.getX() > screenX / 2) {

                    platform.setMovementState(platform.RIGHT);
                } else

                {
                    platform.setMovementState(platform.LEFT);
                }

                break;

            // Lorsque le joueur enleve son doigt de l'ecran
            case MotionEvent.ACTION_UP:
                platform.setMovementState(platform.STOPPED);
                break;
        }

        return true;
    }

}