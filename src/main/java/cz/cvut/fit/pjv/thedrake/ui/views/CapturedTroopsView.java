package cz.cvut.fit.pjv.thedrake.ui.views;

import cz.cvut.fit.pjv.thedrake.troops.PlayingSide;
import cz.cvut.fit.pjv.thedrake.troops.Troop;
import cz.cvut.fit.pjv.thedrake.troops.TroopFace;
import cz.cvut.fit.pjv.thedrake.ui.utils.TroopImageSet;
import cz.cvut.fit.pjv.thedrake.ui.services.GameContext;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * View component displaying captured troops for a player.
 * Shows all enemy troops that this player has captured.
 */
public class CapturedTroopsView extends VBox {
    
    private final PlayingSide side;
    private final GameContext context;
    private final FlowPane capturedContainer;

    public CapturedTroopsView(PlayingSide side, GameContext context) {
        this.side = side;
        this.context = context;
        
        // Style
        getStyleClass().add("captured-troops-view");
        setSpacing(5);
        setPadding(new Insets(5));
        
        // Title
        Label titleLabel = new Label("Zajaté");
        titleLabel.getStyleClass().add("captured-title");
        
        // Container for captured troop icons
        capturedContainer = new FlowPane();
        capturedContainer.setHgap(3);
        capturedContainer.setVgap(3);
        capturedContainer.getStyleClass().add("captured-container");
        
        getChildren().addAll(titleLabel, capturedContainer);
        
        // Initial update
        update();
    }

    /**
     * Update the captured troops view to reflect current game state.
     */
    public void update() {
        capturedContainer.getChildren().clear();
        
        List<Troop> captured = context.getGameState().army(side).captured();
        
        for (Troop troop : captured) {
            // Get opponent's side for the image (captured troops belong to opponent)
            PlayingSide opponentSide = (side == PlayingSide.BLUE) ? PlayingSide.ORANGE : PlayingSide.BLUE;
            
            // Create small icon for captured troop
            TroopImageSet imageSet = new TroopImageSet(troop.name());
            ImageView imageView = new ImageView(imageSet.get(opponentSide, TroopFace.AVERS));
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);
            imageView.setPreserveRatio(true);
            imageView.getStyleClass().add("captured-troop-icon");
            
            capturedContainer.getChildren().add(imageView);
        }
        
        // Show placeholder if no captured troops
        if (captured.isEmpty()) {
            Label emptyLabel = new Label("žádné");
            emptyLabel.getStyleClass().add("captured-empty");
            capturedContainer.getChildren().add(emptyLabel);
        }
    }

    public PlayingSide getSide() {
        return side;
    }
}
