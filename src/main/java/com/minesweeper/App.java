package com.minesweeper;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileWriter;

/**
 * JavaFX App
 */
public class App extends Application {

    private int[] gamesettings; // the settings for each game loaded
    private Start game;
    private boolean loaded = false; // indicates weather a game has been loaded
    private boolean started = false; // indicates weather a game has started

    @Override
    public void start(Stage s) throws IOException {

        // set title for the stage
        s.setTitle("MediaLab - Minesweeper");

        // create the menus
        Menu m1 = new Menu("Application");
        Menu m2 = new Menu("Details");

        // create menu items
        MenuItem m11 = new MenuItem("Create");
        MenuItem m12 = new MenuItem("Load");
        MenuItem m13 = new MenuItem("Start");
        MenuItem m14 = new MenuItem("Exit");
        MenuItem m21 = new MenuItem("Rounds");
        MenuItem m22 = new MenuItem("Solution");

        // add menu items to menus
        m1.getItems().add(m11);
        m1.getItems().add(m12);
        m1.getItems().add(m13);
        m1.getItems().add(m14);
        m2.getItems().add(m21);
        m2.getItems().add(m22);

        // create a menubar
        MenuBar mb = new MenuBar();

        // add menus to menubar
        mb.getMenus().add(m1);
        mb.getMenus().add(m2);

        Popup popupCreate = new Popup(); // create popup to allow creation of a game from user
        Label scenarioIDLabel = new Label("SCENARIO - ID");
        Label difficultyLabel = new Label("Difficulty");
        Label minesLabel = new Label("Mines");
        Label supermineLabel = new Label("Supermine");
        Label timeLabel = new Label("Time");

        TextField tsID = new TextField("Set Scenario - ID"); // set text fields for the user to type in
        TextField tdiff = new TextField("Set Difficulty");
        TextField tmines = new TextField("Set # of mines");
        TextField tsmine = new TextField("Set super mine");
        TextField ttime = new TextField("Set Time");

        Button createButton = new Button("Create");

        VBox vbCreate = new VBox(10); // add everything to a VBox
        vbCreate.setStyle("-fx-background-color: aquamarine; -fx-padding: 13px;");
        vbCreate.getChildren().addAll(scenarioIDLabel, tsID, difficultyLabel, tdiff, minesLabel, tmines, supermineLabel,
                tsmine, timeLabel, ttime, createButton);

        popupCreate.getContent().add(vbCreate); // add the VBox to the popup to appear

        popupCreate.setAutoHide(true);

        Popup popupLoad = new Popup(); // create popup for user to load a specific scenarion from the ones created
        Label scenarioIDLabel1 = new Label("SCENARIO - ID");
        TextField tsID1 = new TextField("Set Scenario - ID");
        Button loadButton = new Button("Load");

        VBox vbLoad = new VBox(10); // add everything to a VBox
        vbLoad.setStyle("-fx-background-color: aquamarine; -fx-padding: 13px;");
        vbLoad.getChildren().addAll(scenarioIDLabel1, tsID1, loadButton);

        popupLoad.getContent().add(vbLoad); // add the VBox to the popup to appear

        popupLoad.setAutoHide(true);

        // create a VBox and add the menu bar to it
        VBox vb = new VBox(mb);

        // create a scene setting dimensions and adding the VBox with the menu bar
        Scene sc = new Scene(vb, 720, 800);

        // set the scene
        s.setScene(sc);

        // action event when clicking Create on the Menu Bar
        EventHandler<ActionEvent> clickonCreate = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (!popupCreate.isShowing()) // if popup isn't showing, show the popup
                    popupCreate.show(s);
            }
        };

        // action event when clicking Create Button on the Create Popup we created
        EventHandler<ActionEvent> clickonCreatePU = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                try { // create file for a scenario
                    File myObj = new File(".\\medialab\\" + tsID.getText());
                    myObj.createNewFile();
                } catch (IOException exc) {
                    System.out.println("An error occurred when creating file.");
                    exc.printStackTrace();
                }

                try {
                    FileWriter myWriter = new FileWriter(".\\medialab\\" + tsID.getText()); // ready to write to file

                    // Write in the file with input from the textfields
                    myWriter.write(tdiff.getText() + "\n");
                    myWriter.write(tmines.getText() + "\n");
                    myWriter.write(ttime.getText() + "\n");
                    myWriter.write(tsmine.getText() + "\n");

                    myWriter.close(); // close the writer
                    popupCreate.hide(); // close the popup
                } catch (IOException exc) {
                    System.out.println("An error occurred when writing.");
                    exc.printStackTrace();
                }

            }
        };

        // action when clicking Exit on the Menu Bar
        EventHandler<ActionEvent> clickonExit = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                s.close(); // close the stage-app
            }
        };

        // action when clicking Load on the Menu Bar
        EventHandler<ActionEvent> clickonLoad = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (!popupLoad.isShowing()) // if popup isn't showing, show the popup
                    popupLoad.show(s);
            }
        };

        // action when clicking Load Button on the Load Popup
        EventHandler<ActionEvent> clickonLoadPU = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                String input = tsID1.getText(); // get user's input from the text-field to know which scenario to open
                try {
                    Interface newinterface = new Interface(input); // create a new interface with the settings from the
                                                                   // file that was specified
                    int[] newsettings = newinterface.getSettings(); // get settings to save for new games without having
                                                                    // to load the same file again
                    gamesettings = newsettings;
                    Pane head = new Pane(); // create new Pane to construct the interface for the user to see
                    Label labelMines = new Label("Mines: " + newsettings[1]); // add labels to the interface for the
                                                                              // timer, title and mines
                    Label labelTime = new Label("Time: " + newsettings[2]);
                    Label labelTitle = new Label("MINESWEEPER");
                    labelMines.setTranslateX(20);
                    labelMines.setStyle("-fx-background-color: aquamarine; -fx-font-size: 25;");
                    labelTitle.setTranslateX(280);
                    labelTitle.setStyle("-fx-background-color: aquamarine; -fx-font-size: 25; -fx-font-weight: bold;");
                    labelTime.setTranslateX(150);
                    labelTime.setStyle("-fx-background-color: aquamarine; -fx-font-size: 25;");
                    head.getChildren().addAll(labelMines, labelTitle, labelTime);
                    head.setTranslateY(40);

                    Pane gameinterface = newinterface.getContent(); // get the content of the new interface to a new
                                                                    // pane
                    StackPane minesweeperdef = new StackPane(); // create a stack pane to merge the two panes
                    gameinterface.setTranslateY(80);
                    minesweeperdef.getChildren().addAll(vb, head, gameinterface); // add everything to the stack Pane
                    Scene minesweeperdefScene = new Scene(minesweeperdef, 720, 800); // and add it to the scene and set
                                                                                     // the scene on the stage
                    popupLoad.hide(); // close the popup
                    loaded = true;
                    s.setScene(minesweeperdefScene);
                    s.show();
                } catch (FileNotFoundException exc) { // exception if the scenario does not exist
                    System.out.println("Scenario with this ID does not exist.");
                    ErrorPopup errPopup = new ErrorPopup("Scenario with this ID does not exist."); // create popup for the error
                    
                    errPopup.showError(s); //show the error popup

                } catch (Interface.InvalidDescriptionException exc) { // exception if Description is not valid
                    exc.getPopup().showError(s); // show the error via popup (same structure as the previous error)
                } catch (Interface.InvalidValueException exc) { // exception if Values are not valid
                    exc.getPopup().showError(s); // show the error via popup (same structure as the previous error)
                }
            }
        };

        // action when clicking Start on the Menu Bar
        EventHandler<ActionEvent> clickonStart = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (loaded == true) { // if game is loaded
                    Start newStart = new Start(gamesettings, s); // start new game
                    game = newStart;
                    Pane gameinterface = newStart.getContent(); // get an interactive game interface to begin playing
                    StackPane minesweeperdef = new StackPane(); // get a stack pane to merge game interface with menu
                    gameinterface.setTranslateY(40);
                    minesweeperdef.getChildren().addAll(vb, gameinterface);
                    Scene minesweeperdefScene = new Scene(minesweeperdef, 720, 800); // get new scene with the new stack
                                                                                     // pane
                    started = true;
                    s.setScene(minesweeperdefScene); // set the new scene
                    s.show();
                }
            }
        };

        // action when clicking on Solution on the menu
        EventHandler<ActionEvent> clickonSolution = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (started) { // if a game has started
                    game.revealMines(gamesettings); // reveal the mines' positions
                }
            }
        };

        // action when clicking on Rounds on the menu
        EventHandler<ActionEvent> clickonRounds = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Popup popupRounds = new Popup(); // create popup to show the last 5 games results
                Button OKButton_rounds = new Button("OK"); // and a button to be able to close the popup

                String lastgames = "";
                TableView<Gamelog> table;
                try {
                    File myObj = new File("games.txt"); // open the games.txt file to get the results of the last 5
                                                        // games
                    Scanner myReader = new Scanner(myObj);

                    while (myReader.hasNextLine()) { // read all the lines
                        String data = myReader.nextLine();
                        lastgames = lastgames + data;
                    }
                    myReader.close();
                } catch (FileNotFoundException exc) { // exception if the games.txt does not exist which means no games
                                                      // have been played yet
                    System.out.println("No games yet.");
                    ErrorPopup errPopup = new ErrorPopup("No games yet."); // create popup for the error
                    
                    errPopup.showError(s);

                }

                table = getLast(lastgames, 5); // from all the games keep the 5 last creating a table

                VBox vbRounds = new VBox(10);
                vbRounds.setStyle("-fx-background-color: aquamarine; -fx-padding: 13px;");
                vbRounds.getChildren().addAll(table, OKButton_rounds); // add the button and the label to the
                                                                       // VBox

                popupRounds.getContent().add(vbRounds); // add the VBox to the popup

                popupRounds.setAutoHide(true);

                popupRounds.show(s);

                // action when clicking OK Button on the Rounds popup
                EventHandler<ActionEvent> clickonOK = new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        popupRounds.hide(); // close popup
                    }
                };

                OKButton_rounds.setOnAction(clickonOK);

            }
        };

        // add the actions for the menu buttons
        m11.setOnAction(clickonCreate);
        m12.setOnAction(clickonLoad);
        m13.setOnAction(clickonStart);
        m14.setOnAction(clickonExit);
        createButton.setOnAction(clickonCreatePU);
        loadButton.setOnAction(clickonLoadPU);
        m21.setOnAction(clickonRounds);
        m22.setOnAction(clickonSolution);

        s.show();
    }

    public static TableView<Gamelog> getLast(String s, int t) // function that lets us keep the last t games
    {

        TableView<Gamelog> table = new TableView<Gamelog>();

        TableColumn<Gamelog, String> column1 = new TableColumn<>("Mines");

        column1.setCellValueFactory(
                new PropertyValueFactory<>("Mines"));

        TableColumn<Gamelog, String> column2 = new TableColumn<>("Attempts");

        column2.setCellValueFactory(
                new PropertyValueFactory<>("Attempts"));

        TableColumn<Gamelog, String> column3 = new TableColumn<>("Time");

        column3.setCellValueFactory(
                new PropertyValueFactory<>("Time"));

        TableColumn<Gamelog, String> column4 = new TableColumn<>("Winner");

        column4.setCellValueFactory(
                new PropertyValueFactory<>("Winner"));

        table.getColumns().add(column1);
        table.getColumns().add(column2);
        table.getColumns().add(column3);
        table.getColumns().add(column4);

        // Vector to store individual strings.
        // Save all strings in the vector.
        String[] v = s.split("@");

        String result = new String("");

        if (v.length == 0) {
            return table;
        }

        // If the string has t lines
        if (v.length >= t) {
            for (int i = v.length - t; i < v.length; i++) {
                v[i] = v[i] + "\n";
                result = result + v[i];
            }
        } else {
            for (int i = 0; i < v.length; i++) {
                v[i] = v[i] + "\n";
                result = result + v[i];
            }
        }

        String[] split = result.split("\n");

        for(int i = 0; i < split.length; i++) {
            String[] attr = split[i].split("/");
            Gamelog e = new Gamelog(attr);
            table.getItems().add(e);
        }

        return table;
    }

    

    public static void main(String[] args) {
        launch(); // launch the app
    }

}
