module tetris.group__25 {
    // This module defines the Tetris game application.
    requires javafx.controls;
    requires javafx.fxml;
    requires java.prefs;

    opens tetris.group__25 to javafx.fxml;
    exports tetris.group__25;
}