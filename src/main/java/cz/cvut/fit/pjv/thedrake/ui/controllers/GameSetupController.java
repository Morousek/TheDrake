package cz.cvut.fit.pjv.thedrake.ui.controllers;

import cz.cvut.fit.pjv.thedrake.game.GameState;
import cz.cvut.fit.pjv.thedrake.ui.utils.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

/**
 * Controller for game setup screen.
 * Allows players to configure board size and mountain count.
 */
public class GameSetupController {

    @FXML
    private Slider boardSizeSlider;

    @FXML
    private Label boardSizeLabel;

    @FXML
    private Slider mountainsSlider;

    @FXML
    private Label mountainsLabel;

    @FXML
    private void initialize() {
        boardSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int size = newVal.intValue();
            boardSizeLabel.setText(size + "x" + size);
            int maxMountains = Math.max(0, (size * size - 10) / 3);
            mountainsSlider.setMax(Math.min(maxMountains, 10));
            if (mountainsSlider.getValue() > mountainsSlider.getMax()) {
                mountainsSlider.setValue(mountainsSlider.getMax());
            }
        });

        mountainsSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            mountainsLabel.setText(String.valueOf(newVal.intValue()));
        });
    }

    @FXML
    private void onBack() {
        SceneNavigator.navigateTo(boardSizeSlider, SceneNavigator.MAIN_MENU, "The Drake");
    }

    @FXML
    private void onStartGame() {
        int boardSize = (int) boardSizeSlider.getValue();
        int mountainCount = (int) mountainsSlider.getValue();

        GameState gameState = GameController.createGameState(boardSize, mountainCount);
        SceneNavigator.<GameController>navigateTo(
            boardSizeSlider,
            SceneNavigator.GAME_VIEW,
            "The Drake - Game",
            controller -> controller.setGameState(gameState, boardSize, mountainCount)
        );
    }
}
