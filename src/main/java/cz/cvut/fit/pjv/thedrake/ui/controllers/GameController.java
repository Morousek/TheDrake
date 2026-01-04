package cz.cvut.fit.pjv.thedrake.ui.controllers;

import cz.cvut.fit.pjv.thedrake.board.Board;
import cz.cvut.fit.pjv.thedrake.board.BoardPos;
import cz.cvut.fit.pjv.thedrake.board.BoardTile;
import cz.cvut.fit.pjv.thedrake.game.GameResult;
import cz.cvut.fit.pjv.thedrake.game.GameState;
import cz.cvut.fit.pjv.thedrake.game.Move;
import cz.cvut.fit.pjv.thedrake.troops.PlayingSide;
import cz.cvut.fit.pjv.thedrake.ui.services.GameOverManager;
import cz.cvut.fit.pjv.thedrake.ui.services.ValidMoves;
import cz.cvut.fit.pjv.thedrake.ui.utils.SceneNavigator;
import cz.cvut.fit.pjv.thedrake.utils.PositionFactory;
import cz.cvut.fit.pjv.thedrake.utils.StandardDrakeSetup;
import cz.cvut.fit.pjv.thedrake.ui.views.BoardView;
import cz.cvut.fit.pjv.thedrake.ui.services.GameContext;
import cz.cvut.fit.pjv.thedrake.ui.views.PlayerPanelView;
import cz.cvut.fit.pjv.thedrake.ui.views.TileView;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Controller for the game screen.
 * Implements GameContext to coordinate all UI components.
 */
public class GameController implements GameContext {

    @FXML
    private BorderPane gameContainer;

    private GameState gameState;
    private int boardSize = 5;
    private int mountainCount = 1;
    
    private BoardView boardView;
    private PlayerPanelView bluePanelView;
    private PlayerPanelView orangePanelView;
    private Label gamePhaseLabel;
    private GameOverManager gameOverManager;
    
    private boolean stackSelected = false;

    /**
     * Initialize method called after FXML is loaded.
     * Creates a game state starting with empty board (no troops placed).
     */
    @FXML
    private void initialize() {
        // Create default game state
        this.gameState = createGameState(boardSize, mountainCount);

        // Initialize game over manager
        this.gameOverManager = new GameOverManager(gameContainer, this::startNewGame, this::goToMainMenu);
        
        // Create game phase label
        this.gamePhaseLabel = new Label();
        gamePhaseLabel.getStyleClass().add("game-phase-label");
        updateGamePhaseLabel();
        
        VBox topContainer = new VBox(15);
        topContainer.setAlignment(Pos.CENTER);
        topContainer.getChildren().add(gamePhaseLabel);
        gameContainer.setTop(topContainer);

        // Create BoardView
        this.boardView = new BoardView(this);
        gameContainer.setCenter(boardView);
        
        // Create PlayerPanelViews
        this.bluePanelView = new PlayerPanelView(PlayingSide.BLUE, this);
        this.orangePanelView = new PlayerPanelView(PlayingSide.ORANGE, this);
        
        gameContainer.setLeft(bluePanelView);
        gameContainer.setRight(orangePanelView);

        System.out.println("GameController initialized - game starts with placing leaders");
        
        // Show initial stack moves for blue player (who starts)
        boardView.showStackMoves();
        
        // Check if first player can make a move (edge case)
        checkForNoMoves();
    }



    // ========== GameContext Implementation ==========

    @Override
    public void tileViewSelected(TileView tileView) {
        if (gameOverManager.isGameOver()) return;
        
        stackSelected = false;
        bluePanelView.clearSelection();
        orangePanelView.clearSelection();
        boardView.tileViewSelected(tileView);
    }

    @Override
    public void executeMove(Move move) {
        if (gameOverManager.isGameOver()) return;
        
        // Clear selections
        stackSelected = false;
        boardView.clearSelection();
        bluePanelView.clearSelection();
        orangePanelView.clearSelection();
        
        // Remember who made the move (they are the potential winner)
        PlayingSide movingSide = gameState.sideOnTurn();
        
        // Execute the move
        gameState = move.execute(gameState);
        
        // Update all views
        boardView.update();
        bluePanelView.update();
        orangePanelView.update();
        updateGamePhaseLabel();
        
        // Check for victory (leader captured)
        if (gameState.result() == GameResult.VICTORY) {
            gameOverManager.showGameOver(GameResult.VICTORY, movingSide, "Vůdce protivníka byl zajat!");
            updateGamePhaseLabel();
            return;
        }
        
        // Check if current player can make any move
        if (checkForNoMoves()) {
            return;
        }
        
        // If still placing troops (leader or guards), show stack moves
        if (gameState.armyOnTurn().boardTroops().isPlacingGuards() || 
            !gameState.armyOnTurn().boardTroops().isLeaderPlaced()) {
            boardView.showStackMoves();
        }
        
        System.out.println("Move executed. Turn: " + gameState.sideOnTurn());
    }

    @Override
    public void stackTroopSelected(PlayingSide side) {
        if (gameOverManager.isGameOver()) return;
        
        if (!isOnTurn(side)) {
            return; // Can't select stack if not on turn
        }
        
        stackSelected = true;
        boardView.clearSelection();
        
        // Clear selection on the other panel
        if (side == PlayingSide.BLUE) {
            orangePanelView.clearSelection();
        } else {
            bluePanelView.clearSelection();
        }
        
        // Show possible placement positions
        boardView.showStackMoves();
    }

    @Override
    public void clearSelection() {
        stackSelected = false;
        boardView.clearSelection();
        bluePanelView.clearSelection();
        orangePanelView.clearSelection();
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public boolean isOnTurn(PlayingSide side) {
        return gameState.sideOnTurn() == side;
    }

    @Override
    public boolean isStackSelected() {
        return stackSelected;
    }

    /**
     * Update the game phase label based on current game state.
     */
    private void updateGamePhaseLabel() {
        String phaseText;
        String sideText = gameState.sideOnTurn() == PlayingSide.BLUE ? "Modrý" : "Oranžový";
        
        if (gameOverManager.isGameOver()) {
            phaseText = "Hra skončila";
        } else if (!gameState.armyOnTurn().boardTroops().isLeaderPlaced()) {
            phaseText = "Fáze: Nasazení vůdce (" + sideText + ")";
        } else if (gameState.armyOnTurn().boardTroops().isPlacingGuards()) {
            phaseText = "Fáze: Nasazení stráží (" + sideText + ")";
        } else {
            phaseText = "Fáze: Hra (" + sideText + " na tahu)";
        }
        
        gamePhaseLabel.setText(phaseText);
    }

    /**
     * Check if current player has any valid moves. If not, they lose.
     * @return true if game ended due to no moves
     */
    private boolean checkForNoMoves() {
        if (gameState.result() != GameResult.IN_PLAY) {
            return false;
        }
        
        ValidMoves validMoves = new ValidMoves(gameState);
        if (validMoves.allMoves().isEmpty()) {
            // Current player has no moves - they lose
            PlayingSide loser = gameState.sideOnTurn();
            PlayingSide winner = (loser == PlayingSide.BLUE) ? PlayingSide.ORANGE : PlayingSide.BLUE;
            
            String reason;
            if (!gameState.armyOnTurn().boardTroops().isLeaderPlaced()) {
                reason = "Hráč nemůže nasadit vůdce!";
            } else if (gameState.armyOnTurn().boardTroops().isPlacingGuards()) {
                reason = "Hráč nemůže nasadit stráže vedle vůdce!";
            } else {
                reason = "Hráč nemá žádný platný tah!";
            }
            
            gameOverManager.showGameOver(GameResult.VICTORY, winner, reason);
            updateGamePhaseLabel();
            return true;
        }
        return false;
    }

    /**
     * Start a new game with the same settings.
     */
    private void startNewGame() {
        gameOverManager.reset();
        gameState = createGameState(boardSize, mountainCount);
        
        // Recreate board view
        boardView = new BoardView(this);
        gameContainer.setCenter(boardView);
        
        // Update panels
        bluePanelView.update();
        orangePanelView.update();
        updateGamePhaseLabel();
        
        boardView.showStackMoves();
    }

    /**
     * Return to main menu.
     */
    private void goToMainMenu() {
        SceneNavigator.navigateTo(gameContainer, SceneNavigator.MAIN_MENU, "The Drake");
    }

    /**
     * Sets the game state with board configuration.
     */
    public void setGameState(GameState gameState, int boardSize, int mountainCount) {
        this.gameState = gameState;
        this.boardSize = boardSize;
        this.mountainCount = mountainCount;

        if (boardView != null) {
            this.boardView = new BoardView(this);
            gameContainer.setCenter(boardView);
        }
        if (bluePanelView != null) {
            bluePanelView.update();
        }
        if (orangePanelView != null) {
            orangePanelView.update();
        }
    }

    /**
     * Gets the current game state.
     *
     * @return current GameState
     */
    public GameState gameState() {
        return gameState;
    }

    /**
     * Create a new game state with random mountain placement.
     */
    public static GameState createGameState(int boardSize, int mountainCount) {
        Board board = new Board(boardSize);
        
        if (mountainCount > 0) {
            PositionFactory pf = board.positionFactory();
            List<BoardPos> validPositions = new ArrayList<>();
            for (int y = 1; y < boardSize - 1; y++) {
                for (int x = 0; x < boardSize; x++) {
                    validPositions.add(pf.pos(x, y));
                }
            }
            Collections.shuffle(validPositions, new Random());

            List<Board.TileAt> mountains = new ArrayList<>();
            for (int i = 0; i < Math.min(mountainCount, validPositions.size()); i++) {
                mountains.add(new Board.TileAt(validPositions.get(i), BoardTile.MOUNTAIN));
            }

            if (!mountains.isEmpty()) {
                board = board.withTiles(mountains.toArray(new Board.TileAt[0]));
            }
        }

        return new StandardDrakeSetup().startState(board);
    }
}
