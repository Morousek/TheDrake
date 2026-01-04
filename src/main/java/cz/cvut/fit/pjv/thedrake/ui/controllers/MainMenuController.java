package cz.cvut.fit.pjv.thedrake.ui.controllers;

import cz.cvut.fit.pjv.thedrake.ui.utils.SceneNavigator;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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
     * Opens game setup screen for local multiplayer.
     */
    @FXML
    private void onMultiplayer() {
        System.out.println("Opening game setup...");
        SceneNavigator.navigateTo(buttonMultiplayer, SceneNavigator.GAME_SETUP, "The Drake - Nastavení hry");
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
