package cz.cvut.fit.pjv.thedrake.ui.views;

import cz.cvut.fit.pjv.thedrake.troops.PlayingSide;
import cz.cvut.fit.pjv.thedrake.ui.services.GameContext;
import cz.cvut.fit.pjv.thedrake.ui.utils.StyleHelper;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Panel displaying player information including:
 * - Player name/side indicator
 * - Turn indicator
 * - Troop stack (troops available to place)
 * - Captured troops
 */
public class PlayerPanelView extends VBox {
    
    private final PlayingSide side;
    private final GameContext context;

    private final Label playerLabel;
    private final Label turnIndicator;
    private final TroopStackView stackView;
    private final CapturedTroopsView capturedView;

    public PlayerPanelView(PlayingSide side, GameContext context) {
        this.side = side;
        this.context = context;
        
        // Style
        getStyleClass().add("player-panel");
        getStyleClass().add(side == PlayingSide.BLUE ? "player-panel-blue" : "player-panel-orange");
        setSpacing(15);
        setPadding(new Insets(15));
        setPrefWidth(180);
        
        // Player name label
        playerLabel = new Label(side == PlayingSide.BLUE ? "Modrý hráč" : "Oranžový hráč");
        playerLabel.getStyleClass().add("player-name");
        
        // Turn indicator
        turnIndicator = new Label();
        turnIndicator.getStyleClass().add("turn-indicator");
        updateTurnIndicator();
        
        // Stack view
        stackView = new TroopStackView(side, context);
        
        // Captured troops view
        capturedView = new CapturedTroopsView(side, context);
        
        getChildren().addAll(playerLabel, turnIndicator, stackView, capturedView);
    }

    /**
     * Update all child views to reflect current game state.
     */
    public void update() {
        updateTurnIndicator();
        stackView.update();
        capturedView.update();
    }

    /**
     * Update the turn indicator label.
     */
    private void updateTurnIndicator() {
        boolean onTurn = context.isOnTurn(side);
        turnIndicator.setText(onTurn ? "● Na tahu" : "○ Čeká");
        StyleHelper.setExclusiveClass(turnIndicator, 
            onTurn ? "turn-indicator-active" : "turn-indicator-inactive",
            "turn-indicator-active", "turn-indicator-inactive");
    }

    /**
     * Clear selection in the stack view.
     */
    public void clearSelection() {
        stackView.clearSelection();
    }

    /**
     * Check if stack has selection.
     */
    public boolean hasStackSelection() {
        return stackView.hasSelection();
    }

    public PlayingSide getSide() {
        return side;
    }
}
