package com.minesweeper;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Preview {
    private static int tile_size;   //size of each tile
    private static int numoftiles;  //number of tiles on each dimension
    private static final int dim = 720;  //dimension of board
    private static static_tile[] preview;   //preview board of tiles
    private Pane gamepreview = new Pane();     //game preview interface

    public Preview(int diff){
        if(diff == 1){
            numoftiles = 9;         //Set the number of tiles on each side according to the level
        }
        else{
            numoftiles = 16;
        }
        tile_size = dim/numoftiles;     //calculate tile size

        preview = new static_tile[numoftiles*numoftiles];    // initialize our preview board

        for(int i=0; i < Math.pow(numoftiles,2); i++){
            int line = i/numoftiles;      //get correct line to write
            int column = i%numoftiles;    //get correct column to write
            preview[i] = new static_tile(line, column);      // set everything as non interactive tiles for the user to see preview
        }

        gamepreview.setPrefSize(dim, dim);
        for(int i=0; i < Math.pow(numoftiles,2); i++){
            gamepreview.getChildren().add(preview[i]);      //add everything to the preview
        }

    }
    public Pane getContent(){        //return the preview board for someone to see
        return gamepreview;
    }

    public class static_tile extends StackPane{     //static tile class that extends Stack Pane to be able to be added to interfaces

        int x;                  //coordinates
        int y;
        Rectangle border;       //border

        public static_tile(int line, int column){   //constructor of static tile

            this.x = line;      //set the coordinates
            this.y = column;
            
            this.border = new Rectangle(tile_size-2, tile_size-2);  //set border
            this.border.setStroke(Color.AQUAMARINE);

            getChildren().addAll(border);       //add border to the interface

            setTranslateX(y * tile_size);       //move to the correct position
            setTranslateY(x * tile_size);
        }
        
    }
}
