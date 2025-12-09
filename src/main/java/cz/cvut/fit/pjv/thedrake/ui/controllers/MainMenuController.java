package cz.cvut.fit.pjv.thedrake.ui.controllers;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the main menu screen.
 * Handles user interactions with menu buttons.
 */
public class MainMenuController {

    @FXML
    private Button buttonSingleplayer;

    @FXML
    private Button buttonMultiplayer;

    @FXML
    private Button buttonOnline;

    @FXML
    private Button buttonExit;

    /**
     * Initialize method - sets up responsive button widths.
     */
    @FXML
    private void initialize() {
        // Make buttons responsive - width scales with window size
        // Formula: 35% of scene width, clamped between 200-400px
        setupResponsiveButton(buttonSingleplayer);
        setupResponsiveButton(buttonMultiplayer);
        setupResponsiveButton(buttonOnline);
        setupResponsiveButton(buttonExit);
    }

    /**
     * Sets up responsive width for a button based on scene width.
     */
    private void setupResponsiveButton(Button button) {
        button.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                button.prefWidthProperty().bind(
                    Bindings.min(400,
                        Bindings.max(200, newScene.widthProperty().multiply(0.35))
                    )
                );
            }
        });
    }

    /**
     * Handles single player game button click.
     * Currently disabled - will be implemented later.
     */
    @FXML
    private void onSinglePlayer() {
        System.out.println("Single player game - Not implemented yet");
        // TODO: Implement single player game logic
    }

    /**
     * Handles multiplayer game button click.
     * Starts a local multiplayer game (two players on one computer).
     */
    @FXML
    private void onMultiplayer() {
        System.out.println("Starting multiplayer game...");
        try {
            // Load game view FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/cvut/fit/pjv/thedrake/fxml/game-view.fxml"));
            Parent root = loader.load();

            // Get the controller (GameState is created automatically in initialize())
            GameController controller = loader.getController();

            // Switch to game scene
            Stage stage = (Stage) buttonMultiplayer.getScene().getWindow();
            Scene gameScene = new Scene(root);

            // Load CSS
            gameScene.getStylesheets().add(getClass().getResource("/cz/cvut/fit/pjv/thedrake/css/main.css").toExternalForm());

            stage.setScene(gameScene);
            stage.setTitle("The Drake - Game");
        } catch (IOException e) {
            System.err.println("Failed to load game view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles online game button click.
     * Currently disabled - will be implemented later.
     */
    @FXML
    private void onOnline() {
        System.out.println("Online game - Not implemented yet");
        // TODO: Implement online game logic
    }

    /**
     * Handles exit button click.
     * Closes the application.
     */
    @FXML
    private void onExit() {
        System.out.println("Exiting application...");
        Platform.exit();
    }
}
