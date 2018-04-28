package com.example.clain.smasher;



import android.graphics.RectF;

public class Platform {

    private RectF rect;

    // longueur et hauteur
    private float length;
    private float height;

    private float x;
    private float y;

    private float platformSpeed;

    // Direction des mouvements de la plateforme
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    private int platformMoving = STOPPED;

    public Platform(int screenX, int screenY){
        // largeur et hauteur
        length = 150;
        height = 30;

        // Position initiale de la plateforme
        x = screenX / 2;
        y = screenY - 60;

        rect = new RectF(x, y, x + length,y + height);

        // Vitesse plateforme
        platformSpeed = 500;
    }

    
    public RectF getRect() {
        return this.rect;
    }


    public void setMovementState(int state){
        platformMoving = state;
    }

    // Mouvement plateforme et changement de coordonnees
    public void update(long fps){
        if(platformMoving == LEFT){
            x = x - platformSpeed / fps;
        }

        if(platformMoving == RIGHT){
            x = x + platformSpeed / fps;
        }

        rect.left = x;
        rect.right = x + length;
    }

}