package cz.cvut.fit.pjv.thedrake.ui.services;

import cz.cvut.fit.pjv.thedrake.game.GameState;
import cz.cvut.fit.pjv.thedrake.game.Move;
import cz.cvut.fit.pjv.thedrake.troops.PlayingSide;
import cz.cvut.fit.pjv.thedrake.ui.views.TileView;

/**
 * Context interface for coordinating game UI components.
 * Implemented by GameController to provide a single point of communication
 * between BoardView, PlayerPanelView, and other UI components.
 */
public interface GameContext {

    // === Tile selection (from board) ===
    
    /**
     * Called when a tile on the board is selected.
     * @param tileView the selected tile view
     */
    void tileViewSelected(TileView tileView);

    /**
     * Execute a move (from board or from stack placement).
     * @param move the move to execute
     */
    void executeMove(Move move);

    // === Stack selection ===
    
    /**
     * Called when a troop from the stack is selected for placement.
     * @param side the side whose stack troop was selected
     */
    void stackTroopSelected(PlayingSide side);

    /**
     * Clear any current selection (tile or stack).
     */
    void clearSelection();

    // === State queries ===
    
    /**
     * Get the current game state.
     * @return current GameState
     */
    GameState getGameState();

    /**
     * Check if the given side is currently on turn.
     * @param side the side to check
     * @return true if this side is on turn
     */
    boolean isOnTurn(PlayingSide side);

    /**
     * Check if a stack troop is currently selected.
     * @return true if a stack troop is selected for placement
     */
    boolean isStackSelected();
}
