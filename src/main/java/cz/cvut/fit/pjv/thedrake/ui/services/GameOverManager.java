package cz.cvut.fit.pjv.thedrake.ui.services;

import cz.cvut.fit.pjv.thedrake.game.GameResult;
import cz.cvut.fit.pjv.thedrake.troops.PlayingSide;
import cz.cvut.fit.pjv.thedrake.ui.controllers.GameOverController;
import cz.cvut.fit.pjv.thedrake.ui.utils.SceneNavigator;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

/**
 * Manages game over state and overlay display.
 * Handles showing/hiding the game over dialog and navigation callbacks.
 */
public class GameOverManager {

    private final Parent gameContainer;
    private final Runnable onNewGame;
    private final Runnable onMainMenu;
    private boolean gameOver = false;

    /**
     * Creates a new GameOverManager.
     *
     * @param gameContainer the main game container node
     * @param onNewGame callback when "New Game" is clicked
     * @param onMainMenu callback when "Main Menu" is clicked
     */
    public GameOverManager(Parent gameContainer, Runnable onNewGame, Runnable onMainMenu) {
        this.gameContainer = gameContainer;
        this.onNewGame = onNewGame;
        this.onMainMenu = onMainMenu;
    }

    /**
     * Check if the game is over.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Reset the game over state (for new game).
     */
    public void reset() {
        gameOver = false;
        removeOverlay();
    }

    /**
     * Show the game over overlay.
     *
     * @param result the game result
     * @param winner the winning side
     * @param reason description of how the game ended
     */
    public void showGameOver(GameResult result, PlayingSide winner, String reason) {
        gameOver = true;

        SceneNavigator.LoadResult<GameOverController> loadResult =
            SceneNavigator.loadFxml(SceneNavigator.GAME_OVER);
        
        if (loadResult == null) {
            return;
        }

        Parent gameOverView = loadResult.getRoot();
        GameOverController controller = loadResult.getController();
        
        controller.initialize(result, winner, reason, onNewGame, onMainMenu);

        // Add overlay to scene
        Scene scene = gameContainer.getScene();
        if (scene != null) {
            Parent currentRoot = scene.getRoot();
            if (!(currentRoot instanceof StackPane)) {
                StackPane stackPane = new StackPane();
                stackPane.getChildren().addAll(currentRoot, gameOverView);
                scene.setRoot(stackPane);
            } else {
                ((StackPane) currentRoot).getChildren().add(gameOverView);
            }
        }
    }

    /**
     * Remove the game over overlay from the scene.
     */
    private void removeOverlay() {
        Scene scene = gameContainer.getScene();
        if (scene != null) {
            Parent root = scene.getRoot();
            if (root instanceof StackPane) {
                StackPane stackPane = (StackPane) root;
                // Keep only the first child (the original game container)
                while (stackPane.getChildren().size() > 1) {
                    stackPane.getChildren().remove(stackPane.getChildren().size() - 1);
                }
            }
        }
    }
}
