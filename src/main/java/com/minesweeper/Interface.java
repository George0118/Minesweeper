package com.minesweeper;

import java.io.File;  
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.scene.layout.*;
import java.util.Arrays;

public class Interface {

    private static int[] settings = {-1, -1, -1, -1};       //array of the 4 settings of our game (as integers)
    private static String[] settings_string = {"-1", "-1", "-1", "-1"};     //array of the 4 settings of our game (as strings) to check for valid description
    private Pane gameinterface = new Pane();    //our game interface

    /**
     * This is a method that creates a preview of the game interface for the user that is not interactive.
     * This is being done via creating an item of class Preview as in Preview.java. To create this item we need
     * to get the settings for our game from the parameter: input, which is a file in the folder medialab. 
     * If the file of the input does not exist the we throw a FileNotFoundException.
     * We read the settings from there and we check if we have all the settings we need and if the settings
     * are correct. If they are not we throw InvalidDescriptionException or InvalidValueException accordingly.
     * If everything is okay we are free to create our preview of the board for the game.
     * @param input the file to read from
     * @throws FileNotFoundException if input file not found
     * @throws InvalidDescriptionException if some settings are not defined
     * @throws InvalidValueException if some settings have wrong values (see table in minesweeper description)
     */

    public Interface(String input) throws FileNotFoundException, InvalidDescriptionException, InvalidValueException {

            File myObj = new File(".\\medialab\\" + input);     //open the given scenario file 
            Scanner myReader = new Scanner(myObj);
            int i = 0;
            while (myReader.hasNextLine()) {                //read each line and give it to the string settings
              String data = myReader.nextLine();
              settings_string[i] = data;
              i++;
            }
            myReader.close();
            checkNumofParameters();             //check if the number of parameters is okay

            for(i=0; i < 4; i++){           //if it is then transform to integers and give to the int settings
                settings[i] = Integer.valueOf(settings_string[i]);
            }

            checkParameters();      //check for the value of the parameters

            Preview preview = new Preview(settings[0]);     //create a preview for the user (before start button)
            gameinterface = preview.getContent();           //add to the interface for the user to see
          
    }

    /**
     * This is a getter method that let's us return to our main App the interface we created to be able
     * to show the user in our stage.
     * @return Pane This returns the game's non interactive interface so we can show the user.
     */

    public Pane getContent(){
        return gameinterface;           //return the interface
    }

    /**
     * This is a getter method that let's us return to our main App the settings we read from the input file.
     * This is useful in order to be able to remember the settings for when the user wants to press Start again
     * to replay this Scenario ID.
     * @return  int[] the Array of the settings we read from the file input.
     */

    public int[] getSettings() {
        return Arrays.copyOf(settings, settings.length);        //return the settings
    }
 
    public static class InvalidDescriptionException extends Exception {     //custom Exception InvalidDescriptionException that happens when there are not enough parameters
        private String message;
        private ErrorPopup popup;

        public InvalidDescriptionException() {
            super("Did not receive the correct number of parameters");  //set the message
            this.message = "Did not receive the correct number of parameters";
            this.popup = new ErrorPopup(this.message);      //create popup to show
        }

        public ErrorPopup getPopup() {
            return this.popup;
        }

        
  }

    public static class InvalidValueException extends Exception { //custom Exception InvalidValueException that happens when some of the settings do not have correct values
        private String message;
        private ErrorPopup popup;

        public InvalidValueException() {
            super("Wrong values of parameters, please check again the instructions");   //set the message
            this.message = "Wrong values of parameters, please check again the instructions";
            this.popup = new ErrorPopup(this.message);      //create popup to show
        } 

        public ErrorPopup getPopup() {
            return this.popup;
        }

    
  }

    private static void checkParameters() throws InvalidValueException {     //function that checks if the parameteres have correct values, if not throws invalid values exception
        InvalidValueException e = new InvalidValueException();
        if(settings[0] != 1 && settings [0] != 2) throw e;
        if(settings[0] == 1 && (settings[1] < 9 || settings[1] > 11)) throw e;
        if(settings[0] == 1 && (settings[2] < 120 || settings[2] > 180)) throw e;
        if(settings[0] == 1 && settings[3] != 0) throw e;
        if(settings[0] == 2 && (settings[1] < 35 || settings[1] > 45)) throw e;
        if(settings[0] == 2 && (settings[2] < 240 || settings[2] > 360)) throw e;
        if(settings[0] == 2 && settings[3] != 0 && settings[3] != 1) throw e;
    }

    private static void checkNumofParameters() throws InvalidDescriptionException {  //function that checks if the number of parameters is correct, if not throws invalid description exception
        InvalidDescriptionException e = new InvalidDescriptionException();
        if(settings_string[0] == "" || settings_string[1] == "" || settings_string[2] == "" || settings_string[3] == "") throw e;
    }
}
