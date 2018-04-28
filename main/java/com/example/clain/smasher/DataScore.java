package com.example.clain.smasher;

public class DataScore {
    private int id;
    private int score;
    private String pseudo;


    public DataScore(int id, int score, String pseudo) {
        this.id = id;
        this.score = score;
        this.pseudo = pseudo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
}


