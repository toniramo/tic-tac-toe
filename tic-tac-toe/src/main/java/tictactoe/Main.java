package tictactoe;

import javafx.application.Application;
import tictactoe.ui.GUI;

/**
 * Main class to start the actual application.
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       
        Application.launch(GUI.class, args);
    }
    
}
