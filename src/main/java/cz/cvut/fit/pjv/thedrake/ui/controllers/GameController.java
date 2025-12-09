package cz.cvut.fit.pjv.thedrake.ui.controllers;

import cz.cvut.fit.pjv.thedrake.board.Board;
import cz.cvut.fit.pjv.thedrake.board.BoardTile;
import cz.cvut.fit.pjv.thedrake.game.GameState;
import cz.cvut.fit.pjv.thedrake.ui.views.BoardView;
import cz.cvut.fit.pjv.thedrake.utils.PositionFactory;
import cz.cvut.fit.pjv.thedrake.utils.StandardDrakeSetup;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

/**
 * Controller for the game screen.
 * Manages the game board and game state.
 */
public class GameController {

    @FXML
    private BorderPane gameContainer;

    private GameState gameState;
    private BoardView boardView;

    /**
     * Initialize method called after FXML is loaded.
     * Creates a sample game state and sets up the board view.
     */
    @FXML
    private void initialize() {
        // Create sample game state for testing
        this.gameState = createSampleGameState();

        // Create and add BoardView
        this.boardView = new BoardView(gameState);
        gameContainer.setCenter(boardView);

        System.out.println("GameController initialized with sample game state");
    }

    /**
     * Creates a sample game state for testing.
     * Sets up a 4x4 board with a mountain tile and places initial troops.
     *
     * @return configured GameState ready for play
     */
    private static GameState createSampleGameState() {
        Board board = new Board(4);
        PositionFactory positionFactory = board.positionFactory();

        // Add mountain tile at position (1, 1)
        board = board.withTiles(new Board.TileAt(positionFactory.pos(1, 1), BoardTile.MOUNTAIN));

        // Start game and place troops
        return new StandardDrakeSetup().startState(board)
            .placeFromStack(positionFactory.pos(0, 0))
            .placeFromStack(positionFactory.pos(3, 3))
            .placeFromStack(positionFactory.pos(0, 1))
            .placeFromStack(positionFactory.pos(3, 2))
            .placeFromStack(positionFactory.pos(1, 0))
            .placeFromStack(positionFactory.pos(2, 3));
    }

    /**
     * Sets the game state (for use when loading from MainMenuController).
     *
     * @param gameState the game state to set
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;

        // Update BoardView with new game state
        if (boardView != null) {
            this.boardView = new BoardView(gameState);
            gameContainer.setCenter(boardView);
        }
    }

    /**
     * Gets the current game state.
     *
     * @return current GameState
     */
    public GameState getGameState() {
        return gameState;
    }
}
