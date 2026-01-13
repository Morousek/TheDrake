package cz.cvut.fit.pjv.thedrake.ui.views;

import cz.cvut.fit.pjv.thedrake.troops.PlayingSide;
import cz.cvut.fit.pjv.thedrake.troops.Troop;
import cz.cvut.fit.pjv.thedrake.troops.TroopFace;
import cz.cvut.fit.pjv.thedrake.ui.utils.TroopImageSet;
import cz.cvut.fit.pjv.thedrake.ui.utils.StyleHelper;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class TroopStackItemView extends Pane {
    
    private final Troop troop;
    private final PlayingSide side;
    private final TroopStackView stackView;
    private final ImageView imageView;
    private final boolean isTop;
    private boolean selected = false;

    public TroopStackItemView(Troop troop, PlayingSide side, TroopStackView stackView, boolean isTop) {
        this.troop = troop;
        this.side = side;
        this.stackView = stackView;
        this.isTop = isTop;

        // Size - bigger for better visibility
        setPrefSize(70, 70);
        setMinSize(70, 70);
        
        // Style
        getStyleClass().add("troop-stack-item");
        
        // Non-top items are dimmed and not clickable
        if (!isTop) {
            getStyleClass().add("troop-stack-item-inactive");
        } else {
            getStyleClass().add("troop-stack-item-top");
        }
        
        // Load and display troop image (always AVERS in stack)
        TroopImageSet imageSet = new TroopImageSet(troop.name());
        imageView = new ImageView(imageSet.get(side, TroopFace.AVERS));
        imageView.setFitWidth(68);
        imageView.setFitHeight(68);
        imageView.setPreserveRatio(true);
        
        getChildren().add(imageView);
        
        // Click handler - only for top item
        if (isTop) {
            setOnMouseClicked(e -> onClicked());
        }
    }

    private void onClicked() {
        stackView.itemSelected(this);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        StyleHelper.toggleClass(this, "troop-stack-item-selected", selected);
    }

    public boolean isSelected() {
        return selected;
    }

    public Troop getTroop() {
        return troop;
    }

    public PlayingSide getSide() {
        return side;
    }
}
