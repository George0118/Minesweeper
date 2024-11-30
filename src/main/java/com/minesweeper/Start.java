package com.minesweeper;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class Start {
  private Pane gameinterface = new Pane();      //interactive game interface
  private StackPane fullinterface = new StackPane();    //the full interface (including labels: mines, time, marked tiles)
  private Label labelMines;     //label that has the number of mines
  private Label labelTime;      //label that has the number of seconds left
  private Label labelMarkedTiles;   //label that has the number of tiles the user has marked
  private Label labelTitle = new Label("MINESWEEPER");    //label that has the title
  private int mines;    //number of mines
  private int time;     //time left
  private int markedtiles = 0;    //number of marked tiles
  private Board board;        //our board
  boolean end = false;        //flag that shows if the game has ended
  boolean victory = false;    //flag that shows if the player has won
  boolean solution = false;   //flag that shows if the user has clicked the "show solution"
  Timeline timeline;          //timeline for the timer
  Timeline timelineMines;     //timeline for the marked tiles
  Timeline timelineEnd;       //timeline for checking if the game has ended

  public Start(int[] settings, Stage s) {

    labelTitle.setTranslateX(280);    //initialize our labels
    labelTitle.setStyle("-fx-background-color: aquamarine; -fx-font-size: 25; -fx-font-weight: bold;");
    labelMines = new Label("Mines: " + settings[1]); 
    labelMines.setTranslateX(20);
    labelMines.setStyle("-fx-background-color: aquamarine; -fx-font-size: 25;");

    this.board = new Board(settings[0], settings[1], settings[2], settings[3]); //create our interactive board
    this.mines = settings[1];   //initialize our variables
    this.time = settings[2];

    gameinterface = board.getContent();   //get the interface of the board

    Pane head = new Pane();   //create head Pane for the labels
    
    //initialize the rest of the labels
    labelMarkedTiles = new Label("Marked Tiles: " + markedtiles); 
    labelMarkedTiles.setTranslateX(500);
    labelMarkedTiles.setStyle("-fx-background-color: aquamarine; -fx-font-size: 25;");
    
    labelTime = new Label("Time: " + time);
    labelTime.setTranslateX(150);
    labelTime.setStyle("-fx-background-color: aquamarine; -fx-font-size: 25;");
    head.getChildren().addAll(labelMines, labelTitle, labelTime, labelMarkedTiles); //add all the labels to head
    gameinterface.setTranslateY(40);

    timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {   //timeline for the timer
      if(end==false){                         //every one second if the game has not ended do time-- and 
        time--;                               // update the label
        labelTime.setText("Time: " + time);
        if(time==0){                          //if time runs out do TimeRunOut
          timeRunOut(s, settings);
        }
      }
    }));
    timeline.setCycleCount(settings[2]);    //do for the max time the game can go on
    timeline.play();                        //start timeline

    timelineMines = new Timeline(new KeyFrame(Duration.seconds(0.01), ev -> {     //timeline for marked tiles
      labelMarkedTiles.setText("Marked Tiles: " + board.numofMarkedTiles());      //every 0.01 seconds update marked tiles label
    }));
    timelineMines.setCycleCount(100*settings[2]);   //do for the max time the game can go on
    timelineMines.play();           //start timeline

    timelineEnd = new Timeline(new KeyFrame(Duration.seconds(0.01), ev -> {     //timeline for if the game has ended
      end = board.getGameEnd();           //get if game has ended
      victory = board.gameVictory();      //get if user has won
      if(end){
        timeline.stop();                  //if game has ended end all timelines
        timelineMines.stop();             
        timelineEnd.stop();
        if(!solution){                    //if the user has not pressed the solution button the do EndGame
          EndGame(s, settings);
        }
      }
    }));
    timelineEnd.setCycleCount(100*settings[2]);     //do for the max time the game can go on
    timelineEnd.play();           //start timeline
         
    fullinterface.getChildren().addAll(head,gameinterface);     //add our game interface (board) and head to the full interface

  }

  public Pane getContent(){
    return fullinterface;       //return the full game interface
  }

  public void timeRunOut(Stage s, int[] settings){      //operation that happens when the time has run out
    Popup popupTRO = new Popup();     //create popup to notify user
    Label TROlabel = new Label("Time Run Out, Try Again you can do it!");   
    TROlabel.setStyle("-fx-text-color: aquamarine;");
    Button OKButton = new Button("OK");     //and button to close the popup and try again

    VBox vbTRO = new VBox(10);
    vbTRO.setStyle("-fx-background-color: lightgray; -fx-padding: 13px;");
    vbTRO.getChildren().addAll(TROlabel, OKButton);   //add everything to the VBox

    popupTRO.getContent().add(vbTRO);   //add VBox to the popup

    popupTRO.setAutoHide(true);

    popupTRO.show(s);   //show at the stage

    try {                                                       //create file to store info about the game
      File myObj = new File("games.txt");
      myObj.createNewFile();                                    
    } catch (IOException e) {            
      System.out.println("An error occurred when creating file.");
      e.printStackTrace();
    }

    try {
      FileWriter myWriter = new FileWriter("games.txt", true);      //ready to write to file

      myWriter.write(String.valueOf(settings[1]) + " / " + String.valueOf(board.getAttempts()) + " / " + String.valueOf(settings[2]) + " / " + "Computer@\r\n");    //if the time run out then the player lost
      
      myWriter.close(); //close the writer
    }catch (IOException e) {
      System.out.println("An error occurred when writing.");
      e.printStackTrace();
    }

    //action that happens when we click OK Button on the TimeRunOut Popup
    EventHandler<ActionEvent> clickOK = new EventHandler<ActionEvent>() {
      public void handle(ActionEvent e)
      {
              popupTRO.hide();    //close popup
      }
    };

    OKButton.setOnAction(clickOK);
  }

  public void EndGame(Stage s, int[] settings){   //operation that happens when the game has ended
    Popup popupEG = new Popup();    //create popup
    Label EGlabel = new Label("End Game Label");      //create label and button for the popup
    EGlabel.setStyle("-fx-text-color: aquamarine;");
    Button OKButton = new Button("OK");

    if(victory){
      EGlabel.setText("Congratulations, you have won!");      //if won
    }
    else{
      EGlabel.setText("Oops that was a mine");            //if lost
    }

    VBox vbEG = new VBox(10);
    vbEG.setStyle("-fx-background-color: lightgray; -fx-padding: 13px;");
    vbEG.getChildren().addAll(EGlabel, OKButton);   //add everything to a VBox

    popupEG.getContent().add(vbEG);     //add to Popup

    popupEG.setAutoHide(true);

    popupEG.show(s);      //show at the initial stages

    try {                                                       //create file to store info about the game
      File myObj = new File("games.txt");
      myObj.createNewFile();
    } catch (IOException e) {
      System.out.println("An error occurred when creating file.");
      e.printStackTrace();
    }

    try {
      FileWriter myWriter = new FileWriter("games.txt", true);      //ready to write to file
      if(victory){
        myWriter.write(String.valueOf(settings[1]) + " / " + String.valueOf(board.getAttempts()) + " / " + String.valueOf(settings[2]) + " / " + "Player@\r\n");      //if won the the player wins
      }
      else{
        myWriter.write(String.valueOf(settings[1]) + " / " + String.valueOf(board.getAttempts()) + " / " + String.valueOf(settings[2]) + " / " + "Computer@\r\n");    //if not the the computer wins
      }
      myWriter.close(); //close the writer
    }catch (IOException e) {
      System.out.println("An error occurred when writing.");
      e.printStackTrace();
    }

    //action that happens when we click on OK Button on the EndGame Popup
    EventHandler<ActionEvent> clickOK = new EventHandler<ActionEvent>() {
      public void handle(ActionEvent e)
      {
              popupEG.hide();   //close popup
      }
    };

    OKButton.setOnAction(clickOK);
  }


  public void changeMineLabel(boolean down){    //function that changes the marked tiles Label 
    if(down){                                   // WAS NOT USED AT THE END
      this.mines--;
    }
    else{
      this.mines++;
    }
    labelMines.setText("Mines: " + mines);
  }

  public void revealMines(int[] settings){      //reveal mines function reveals all the mines in our board
    board.reveal();             //reveal mines on our board
    end = true;                 //end game
    solution = true;            //this request comes from user clicking the solution on menu

    try {                                                       //create file to store info about the mines
      File myObj = new File("games.txt");
      myObj.createNewFile();
    } catch (IOException e) {
      System.out.println("An error occurred when creating file.");
      e.printStackTrace();
    }

    try {
      FileWriter myWriter = new FileWriter("games.txt", true);      //ready to write to file

      myWriter.write(String.valueOf(settings[1]) + " / " + String.valueOf(board.getAttempts()) + " / " + String.valueOf(settings[2]) + " / " + "Computer@\r\n");    //when clicking solution the game is saved as lost
      
      myWriter.close(); //close the writer
    }catch (IOException e) {
      System.out.println("An error occurred when writing.");
      e.printStackTrace();
    }
  }

}


