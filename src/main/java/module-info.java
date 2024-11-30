module com.minesweeper {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.minesweeper to javafx.fxml;
    exports com.minesweeper;
}
