package com.minesweeper;

import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class ErrorPopup{        //class for Error popups
    private String message;

    public ErrorPopup (String message) {
        this.message = message;
    }

    public void showError(Stage s){     //function that shows the error in a popup message
        System.out.println(message);
        Popup errPopup = new Popup();
        Label errLabel = new Label(this.message);      //add message as label
        Button OKButton = new Button("OK");           //and a button to close the error popup

        VBox vbError = new VBox(10);
        vbError.setStyle("-fx-background-color: aquamarine; -fx-padding: 13px;");
        vbError.getChildren().addAll(errLabel, OKButton);   //add everything to a VBox

        errPopup.getContent().add(vbError);     //add the VBox to our error popup
        errPopup.show(s);

        //action that happens when we click OK Button on the Error Popup
        EventHandler<ActionEvent> clickonOK = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                errPopup.hide();    //close the popup
            }
        }; 

        OKButton.setOnAction(clickonOK);
    }

}

