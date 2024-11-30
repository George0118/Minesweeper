package com.minesweeper;

public class Gamelog {          //this is a class that helps us store games that are already completed
    private String mines;       //number of mines in the game
    private String attempts;    //number of attempts made by the user in the game
    private String time;        //time the user had
    private String winner;      //winner of the game

    public Gamelog(String[] attr) {     //we create a Gamelog from a String [] given
        this.mines = attr[0];
        this.attempts = attr[1];
        this.time = attr[2];
        this.winner = attr[3];
    }

    public String getMines() {      // getter for mines
        return mines;
    }

    public String getAttempts() {   //getter for attempts
        return attempts;
    }

    public String getTime() {       //getter for time
        return time;
    }

    public String getWinner() {     //getter for winner
        return winner;
    }
}
