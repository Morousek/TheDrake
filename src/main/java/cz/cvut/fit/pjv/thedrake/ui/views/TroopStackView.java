package cz.cvut.fit.pjv.thedrake.ui.views;

import cz.cvut.fit.pjv.thedrake.troops.PlayingSide;
import cz.cvut.fit.pjv.thedrake.troops.Troop;
import cz.cvut.fit.pjv.thedrake.ui.services.GameContext;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * View component displaying the troop stack for a player.
 * Shows all troops that can still be placed on the board.
 */
public class TroopStackView extends VBox {
    
    private final PlayingSide side;
    private final GameContext context;
    private final FlowPane troopsContainer;
    private final ScrollPane scrollPane;
    private final List<TroopStackItemView> itemViews;
    private TroopStackItemView selectedItem;

    public TroopStackView(PlayingSide side, GameContext context) {
        this.side = side;
        this.context = context;
        this.itemViews = new ArrayList<>();
        
        // Style
        getStyleClass().add("troop-stack-view");
        setSpacing(5);
        setPadding(new Insets(5));
        
        // Title
        Label titleLabel = new Label("Zásobník");
        titleLabel.getStyleClass().add("stack-title");
        
        // Container for troop items
        troopsContainer = new FlowPane();
        troopsContainer.setHgap(5);
        troopsContainer.setVgap(5);
        troopsContainer.getStyleClass().add("troops-container");
        troopsContainer.setPrefWrapLength(150); // Allow 2 items per row
        
        // ScrollPane for scrolling - responsive height
        scrollPane = new ScrollPane(troopsContainer);
        scrollPane.getStyleClass().add("stack-scroll-pane");
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setMinHeight(100);
        
        // Bind height to scene - responsive (30% of window height, min 100, max 400)
        sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                scrollPane.prefHeightProperty().bind(
                    Bindings.min(800,
                        Bindings.max(100, newScene.heightProperty().multiply(0.40))
                    )
                );
                scrollPane.maxHeightProperty().bind(
                    Bindings.min(800,
                        Bindings.max(100, newScene.heightProperty().multiply(0.40))
                    )
                );
            }
        });
        
        getChildren().addAll(titleLabel, scrollPane);
        
        // Initial update
        update();
    }

    /**
     * Update the stack view to reflect current game state.
     */
    public void update() {
        troopsContainer.getChildren().clear();
        itemViews.clear();
        selectedItem = null;
        
        List<Troop> stack = context.getGameState().army(side).stack();
        
        for (int i = 0; i < stack.size(); i++) {
            Troop troop = stack.get(i);
            boolean isTop = (i == 0); // First item is the top of the stack
            TroopStackItemView itemView = new TroopStackItemView(troop, side, this, isTop);
            itemViews.add(itemView);
            troopsContainer.getChildren().add(itemView);
        }
    }

    /**
     * Called when a stack item is clicked.
     */
    void itemSelected(TroopStackItemView item) {
        if (!context.isOnTurn(side)) {
            return; // Can't select if not on turn
        }
        
        // Deselect previous
        if (selectedItem != null && selectedItem != item) {
            selectedItem.setSelected(false);
        }
        
        selectedItem = item;
        selectedItem.setSelected(true);
        
        // Notify context
        context.stackTroopSelected(side);
    }

    /**
     * Clear selection.
     */
    public void clearSelection() {
        if (selectedItem != null) {
            selectedItem.setSelected(false);
            selectedItem = null;
        }
    }

    /**
     * Check if this stack has a selected item.
     */
    public boolean hasSelection() {
        return selectedItem != null;
    }

    public PlayingSide getSide() {
        return side;
    }
}
