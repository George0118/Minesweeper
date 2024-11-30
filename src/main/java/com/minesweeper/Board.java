package com.minesweeper;

import java.util.ArrayList;
import java.util.Collections;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

import java.io.File; 
import java.io.IOException;
import java.io.FileWriter;

public class Board {
    private static int tile_size;   //size of each tile
    private static int numoftiles;  //number of tiles on each dimension
    private static final int dim = 720;  //dimension of board
    private static tile[] board;   //board of tiles
    private Pane gameinterface = new Pane();    //the interactive interface of the game 
    private int numofmarkedtiles = 0;       //number of tiles the player has marked as mines
    private int numofmines;         //number of mines
    private boolean end = false;    //boolean variable that shows if the game has ended
    private int attempts = 0;       //attempts of marking tiles as mine (if < 4 and you mark the supermine then it reveals the tiles in the same column and line)
    private boolean superopen;      //boolean variable that indicates whether the open procedure that is happening is from a supermine or not 
    private int successful_opens = 0;   //number of successful opens from the player

    public Board(int level, int mines, int time, int supermine){
        if(level == 1){
            numoftiles = 9;         //Set the number of tiles on each side according to the level
        }
        else{
            numoftiles = 16;
        }
        tile_size = dim/numoftiles;     //calculate tile size

        numofmines = mines;

        board = new tile[numoftiles*numoftiles];    // initialize our board

        for(int i=0; i < Math.pow(numoftiles,2); i++){
            int line = i/numoftiles;      //get correct line to write
            int column = i%numoftiles;    //get correct column to write
            board[i] = new tile(false, false, line, column);      // set everything as normal tiles
        }
        
        try {                                                       //create file to store info about the mines
            File myObj = new File("mines.txt");
            myObj.createNewFile();
          } catch (IOException e) {
            System.out.println("An error occurred when creating file.");
            e.printStackTrace();
          }

        ArrayList<Integer> list = new ArrayList<Integer>();         //create list of all possible positions for mines
        for (int i = 0; i < Math.pow(numoftiles,2); i++) list.add(i);
        Collections.shuffle(list);          //shuffle list to get random positions


        try {
            FileWriter myWriter = new FileWriter("mines.txt");      //ready to write to file

        for(int i=0; i < mines; i++){
            int pos = list.get(i);                  //get the first #ofmines numbers from the list to place mines there
            int line = pos/numoftiles;      //get correct line to write
            int column = pos%numoftiles;    //get correct column to write

            if(i==0 && supermine == 1){
                board[pos] = new tile(true,true, line, column);  //the first random number gets to be supermine
            }
            else{
                board[pos] = new tile(true,false, line, column);  //the rest are regular mines
            }

            int issupermine = (board[pos].isSuperMine()) ? 1 : 0;   //check if it is super mine

            //Write in the file
            myWriter.write(String.valueOf(line) + ", " + String.valueOf(column) + ", " + String.valueOf(issupermine) + "\n");

        }

        myWriter.close(); //close the writer
        }catch (IOException e) {
            System.out.println("An error occurred when writing.");
            e.printStackTrace();
        }

        for(int i=0; i < Math.pow(numoftiles,2); i++){      //set the number of neigbouring mines for each tile
            board[i].setNeighbours(countNeighbours(i));
        }

        gameinterface.setPrefSize(dim, dim);
        for(int i=0; i < Math.pow(numoftiles,2); i++){
            //System.out.println(i);
            gameinterface.getChildren().add(board[i]);      //add the tiles to the game interface
        }

    }

    public Pane getContent(){        //return the interface for the user to see and use
        return gameinterface;
    }

    public static int countNeighbours(int i){       //function that lets us count the mines around each tile
        int count = 0;
        int line = i/numoftiles;
        int column = i%numoftiles;
        //System.out.println(String.valueOf(line) + "   " + String.valueOf(column));

        if(line == 0 && column == 0){
            if(board[i+1].isMine()) count++;
            if(board[i+numoftiles+1].isMine()) count++;
            if(board[i+numoftiles].isMine()) count++;
        }
        else if(line == 0 && column == numoftiles-1){
            if(board[i-1].isMine()) count++;
            if(board[i+numoftiles-1].isMine()) count++;
            if(board[i+numoftiles].isMine()) count++;
        }
        else if(line == numoftiles-1 && column == 0){
            if(board[i+1].isMine()) count++;
            if(board[i-numoftiles+1].isMine()) count++;
            if(board[i-numoftiles].isMine()) count++;
        }
        else if(line == numoftiles-1 && column == numoftiles-1){
            if(board[i-1].isMine()) count++;
            if(board[i-numoftiles-1].isMine()) count++;
            if(board[i-numoftiles].isMine()) count++;
        }
        else if(line == 0){
            if(board[i-1].isMine()) count++;
            if(board[i+numoftiles-1].isMine()) count++;
            if(board[i+numoftiles].isMine()) count++;
            if(board[i+1].isMine()) count++;
            if(board[i+numoftiles+1].isMine()) count++;
        }
        else if(line == numoftiles - 1){
            if(board[i-1].isMine()) count++;
            if(board[i-numoftiles-1].isMine()) count++;
            if(board[i-numoftiles].isMine()) count++;
            if(board[i+1].isMine()) count++;
            if(board[i-numoftiles+1].isMine()) count++;
        }
        else if(column == 0){
            if(board[i+1].isMine()) count++;
            if(board[i-numoftiles+1].isMine()) count++;
            if(board[i+numoftiles].isMine()) count++;
            if(board[i-numoftiles].isMine()) count++;
            if(board[i+numoftiles+1].isMine()) count++;
        }
        else if(column == numoftiles-1){
            if(board[i-1].isMine()) count++;
            if(board[i-numoftiles-1].isMine()) count++;
            if(board[i+numoftiles].isMine()) count++;
            if(board[i-numoftiles].isMine()) count++;
            if(board[i+numoftiles-1].isMine()) count++;
        }
        else{
            if(board[i-1].isMine()) count++;
            if(board[i-numoftiles-1].isMine()) count++;
            if(board[i-numoftiles].isMine()) count++;
            if(board[i+1].isMine()) count++;
            if(board[i+numoftiles+1].isMine()) count++;
            if(board[i+numoftiles].isMine()) count++;
            if(board[i-numoftiles+1].isMine()) count++;
            if(board[i+numoftiles-1].isMine()) count++;
        }

        return count;
    }

    public int numofMarkedTiles(){    //check if it is a mine
        return numofmarkedtiles;
    }

    public void reveal(){           //function that reveals all mines
        for(int i=0; i < Math.pow(numoftiles,2); i++){
            if(board[i].isMine()){
                board[i].open();
            }
        }
        end=true;       //after revealing all the mines the game ends
    }

    public boolean getGameEnd(){     //returns if the game has ended or not
        return end;
    }

    public boolean gameVictory(){   //checks the winning condition for the player
        for(int i=0; i < Math.pow(numoftiles,2); i++){
            if(!board[i].isMine() && !board[i].isOpen()){
                return false;
            }
        }
        end=true;       //after winning the game the game ends
        return true;
    }

    public void superMineReveal(int line, int column){      //function that handles when we have marked successfully the supermine in the first 4 attempts
        superopen = true;   //set the superopen = true to not force recursive opening of tiles
        for(int i=0; i < Math.pow(numoftiles,2); i++){
            int x = i/numoftiles;
            int y = i%numoftiles;
            if(x == line || y == column){       //if tile has the same column or line as the supermine
                if(board[i].isMine()){          //if it is a mine
                    if(board[i].isMarked == 1){    //if is already marked, unmark it
                        numofmarkedtiles--;
                    }
                    board[i].smark();           // and then supermark it
                }
                else{                           //else if it is a normal tile just open it (superopen = true)
                    if(!board[i].isOpen){
                        board[i].open();
                    }
                }
            }
        }
        superopen = false;      //reset superopen to false
    }

    public int getAttempts(){       //return the successful open actions a player has done
        return successful_opens;
    }


    public class tile extends StackPane{        //tile class that extends Stack Pane to be able to add to a Stack Pane
        protected boolean mine;     //indicates if tile is a mine or not
        protected boolean supermine;    //indicates if tile is a supermine or not
        protected int neighbours;       //number of mines around
        protected int x;        //line of the tile
        protected int y;        //column of the tile
        protected boolean isOpen = false;       //indicates if tile is open or not
        protected int isMarked = -1;            //indicates if tile is marked or not
        protected int isSuperMarked = -1;       //indicates if the tile is supermarked or not

        protected Text text = new Text();       //text that appears
        protected Rectangle border = new Rectangle(tile_size-2, tile_size-2);   //border of the tile

        public tile(boolean mine, boolean supermine, int line, int column){   //constructor of a tile
            this.mine = mine;           //set the private variables of our tile
            this.supermine = supermine;
            this.x = line;
            this.y = column;
            this.border.setStroke(Color.AQUAMARINE);
            
            if(!isOpen){        //if tile is not open then we can perform two actions
                setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        MouseButton button = event.getButton();
                        if(button==MouseButton.PRIMARY && end == false && isSuperMarked == -1 && !isOpen){  //if left click and is not open and is not supermarked and the game has not ended then open
                            open();
                            successful_opens++;
                        }else if(button==MouseButton.SECONDARY && end == false && isSuperMarked == -1){     //if right click and the game has not ended and is not supermarked the mark
                            mark();
                        }
                    }
                });
            }
        }

        public void smark(){    //super mark tile
            if(!isOpen()){                  //if it is not open
                getChildren().add(smarkt());    //create supermarked tile and add to the interface
                isMarked = 1;           //and set it as marked and supermarked from now on
                isSuperMarked = 1;
            }
        }

        public supermarkedTile smarkt(){    //create a supermarked tile
            supermarkedTile smtile = new supermarkedTile(true, isSuperMine(), x, y);
            return smtile;
        }

        public void mark(){     //mark tile
            if(!isOpen && (numofmarkedtiles < numofmines || isMarked == 1)){        //you can mark tiles only if they are not open and you haven't marked more tiles than the mines existing
                if(isSuperMine() && attempts < 5){
                    superMineReveal(x, y);      //if attempts for marking supermine are less than 5 and you mark a supermine then the corresponding tiles are reveal
                }
                else{
                    getChildren().add(markt());     //else just mark this tile
                    isMarked = -isMarked;           
                }
            }  
        }

        public markedTile markt(){      //create a marked tile
            markedTile mTile = new markedTile(mine, supermine, x, y, neighbours, isMarked);
            return mTile;
        }

        public boolean isOpen(){        //returns whether the tile is open or not
            return isOpen;
        }

        public void open(){     //operation that happens when trying to open a tile
            if(!isOpen){
                getChildren().add(opent()); //if is not open the create the open tile
                isOpen = true;                 //and set the tile as open
                if(neighbours == 0 && mine == false && superopen == false){
                    openNeighbours(x,y);    //if this tile has no mines around, it is not a mine and it is not a supermine open then open it's neighbours too
                }
                if(mine==true){     //if you open a mine the reveal the rest of the mines and end the game
                    reveal();
                }
            }    
        }

        public openTile opent(){        //create an open tile
            openTile oTile = new openTile(mine, supermine, x, y, neighbours);
            return oTile;
        }

        public boolean isMine(){    //check if it is a mine
            return this.mine;
        }

        public boolean isSuperMine(){   //check if it is a super mine
            return this.supermine;
        }

        public void setNeighbours(int count){       //function that sets the text for each tile
            this.neighbours = count;
            text.setText(mine ? "X" : String.valueOf(this.neighbours)); //if mine set to X
            text.setVisible(false);     //at first the text shouldn't be visible

            text.setFill(Color.AQUAMARINE);

            getChildren().addAll(border,text);     

            setTranslateX(y * tile_size);       //set the tile in the correct place on the interface
            setTranslateY(x * tile_size);
        }

        // public void printTile(){
        //     if(this.isMine() && this.isSuperMine()){
        //         System.out.print(2);
        //     }
        //     else if(this.isMine() && !this.isSuperMine()){
        //         System.out.print(1);
        //     }
        //     else{
        //         System.out.print(0);
        //     }
        // }

        public void printNeighbours(){
            System.out.print(neighbours);
        }

        public void openNeighbours(int x, int y){       //operation that happens when we need to open the neighbours of a tile that has no mines around

            int line = x;
            int column = y;
            int i = x*numoftiles + y;
            //System.out.println(String.valueOf(line) + "   " + String.valueOf(column));
    
            if(line == 0 && column == 0){
                board[i+1].open();
                board[i+numoftiles+1].open();
                board[i+numoftiles].open();
            }
            else if(line == 0 && column == numoftiles-1){
                board[i-1].open();
                board[i+numoftiles-1].open();
                board[i+numoftiles].open();
            }
            else if(line == numoftiles-1 && column == 0){
                board[i+1].open();
                board[i-numoftiles+1].open();
                board[i-numoftiles].open();
            }
            else if(line == numoftiles-1 && column == numoftiles-1){
                board[i-1].open();
                board[i-numoftiles-1].open();
                board[i-numoftiles].open();
            }
            else if(line == 0){
                board[i-1].open();
                board[i+numoftiles-1].open();
                board[i+numoftiles].open();
                board[i+1].open();
                board[i+numoftiles+1].open();
            }
            else if(line == numoftiles - 1){
                board[i-1].open();
                board[i-numoftiles-1].open();
                board[i-numoftiles].open();
                board[i+1].open();
                board[i-numoftiles+1].open();
            }
            else if(column == 0){
                board[i+1].open();
                board[i+numoftiles+1].open();
                board[i+numoftiles].open();
                board[i-numoftiles].open();
                board[i-numoftiles+1].open();
            }
            else if(column == numoftiles-1){
                board[i-1].open();
                board[i-numoftiles-1].open();
                board[i-numoftiles].open();
                board[i+numoftiles].open();
                board[i+numoftiles-1].open();
            }
            else{
                board[i-1].open();
                board[i-numoftiles-1].open();
                board[i-numoftiles].open();
                board[i+1].open();
                board[i+numoftiles+1].open();
                board[i+numoftiles].open();
                board[i-numoftiles+1].open();
                board[i+numoftiles-1].open();
            }
    
        }

    }

    public class openTile extends tile{     //open tile class

        public openTile(boolean mine, boolean supermine, int line, int column, int neighbours){   //constructor of an open tile
            
            super(mine, supermine, line, column);       //construct a tile first
            this.isOpen = true;         //since it is an open tile the isOpen flag is true

            this.neighbours = neighbours;       //set neighbours = mines around
            this.text.setText(mine ? "X" : String.valueOf(this.neighbours));    //if it is a mine the the text = X else text = neighbours
            this.border = new Rectangle(tile_size-2, tile_size-2);
            this.border.setStroke(Color.BLACK);
            this.border.setFill(Color.AQUAMARINE);

            if(mine){
                text.setFill(Color.RED);
            }

            if(supermine){
                text.setFill(Color.DARKRED);
            }

            if(neighbours == 0 && mine == false){       //if there are no mines around and it is not a mine then dont show anything as text
                text.setVisible(false);
            }

            getChildren().addAll(border,text);      //add to interface
        }

        public boolean isMine(){    //check if it is a mine
            return this.mine;
        }

        public boolean isSuperMine(){   //check if it is a super mine
            return this.supermine;
        }

        
    }

    public class markedTile extends tile{       //marked tile

        public markedTile(boolean mine, boolean supermine, int line, int column, int neighbours, int isMarked){   //constructor of a marked tile
            
            super(mine, supermine, line, column);       //first create a tile
            this.isMarked = isMarked;                   //set flag isMarked to what it was before the click
            this.neighbours = neighbours;

            this.text.setText("X");                 //set text to X (not red) to indicate that a tile is marked to the user
            this.text.setFill(Color.AQUAMARINE);
            this.border = new Rectangle(tile_size-2, tile_size-2);
            this.border.setStroke(Color.AQUAMARINE);

            if(isMarked == 1){
                this.text.setVisible(false);        //if it was marked then hide the text
            }
            else if(isMarked == -1){
                this.text.setVisible(true);         //if it was not marked then show text (X)
            }

            if(isMarked == 1){              //if it was marked then number of marked tiles is lowered by 1
                numofmarkedtiles--;
            }
            else if(isMarked == -1){        //if it was not marked the number of marked tiles is increased by 1
                attempts++;                 // as well as attempts at marking
                numofmarkedtiles++;
            }
            
            this.isMarked = 0;              //after set the isMarked flag to 0 to not interfere with future markings of the same tile

            getChildren().addAll(border,text);      //add to the interface
        }
        
    }

    public class supermarkedTile extends tile{      //supermarked tile

        public supermarkedTile(boolean mine, boolean supermine, int line, int column){   //constructor of a supermarked tile
            
            super(mine, supermine, line, column);       //first create a tile
            isMarked = 1;                               //set it as marked and supermarked
            isSuperMarked = 1;

            this.text.setText("X");                 //set text as X (green = safe)
            this.text.setFill(Color.GREEN);
            this.border = new Rectangle(tile_size-2, tile_size-2);
            this.border.setStroke(Color.GREEN);

            this.text.setVisible(true);     //show to user

            attempts++;             //attempts at marking and number of marked tiles are both incremented by 1
            numofmarkedtiles++;

            getChildren().addAll(border,text);      //add to interface for the user to see
        }
        
    }

}
