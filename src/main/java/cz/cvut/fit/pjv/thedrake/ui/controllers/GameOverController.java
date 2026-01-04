package cz.cvut.fit.pjv.thedrake.ui.controllers;

import cz.cvut.fit.pjv.thedrake.game.GameResult;
import cz.cvut.fit.pjv.thedrake.troops.PlayingSide;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller for the game over overlay.
 */
public class GameOverController {
    
    @FXML
    private Label winnerLabel;
    
    @FXML
    private Label reasonLabel;
    
    private Runnable onNewGameCallback;
    private Runnable onMainMenuCallback;

    /**
     * Initialize the game over view with result data.
     * 
     * @param result the game result (VICTORY or DRAW)
     * @param winner the winning side (null if draw)
     * @param reason description of how the game ended
     * @param onNewGame callback for starting a new game
     * @param onMainMenu callback for returning to main menu
     */
    public void initialize(GameResult result, PlayingSide winner, String reason,
                          Runnable onNewGame, Runnable onMainMenu) {
        this.onNewGameCallback = onNewGame;
        this.onMainMenuCallback = onMainMenu;
        
        // Set winner text
        if (result == GameResult.VICTORY) {
            String winnerText = winner == PlayingSide.BLUE ? "Modrý hráč" : "Oranžový hráč";
            winnerLabel.setText("Vítěz: " + winnerText);
            winnerLabel.getStyleClass().add(winner == PlayingSide.BLUE ? "winner-blue" : "winner-orange");
        } else if (result == GameResult.DRAW) {
            winnerLabel.setText("Remíza");
        }
        
        // Set reason
        reasonLabel.setText(reason);
    }
    
    @FXML
    private void onNewGame() {
        if (onNewGameCallback != null) {
            onNewGameCallback.run();
        }
    }
    
    @FXML
    private void onMainMenu() {
        if (onMainMenuCallback != null) {
            onMainMenuCallback.run();
        }
    }
}
