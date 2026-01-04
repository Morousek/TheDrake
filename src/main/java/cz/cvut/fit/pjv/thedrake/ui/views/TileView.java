package cz.cvut.fit.pjv.thedrake.ui.views;

import cz.cvut.fit.pjv.thedrake.board.BoardPos;
import cz.cvut.fit.pjv.thedrake.board.Tile;
import cz.cvut.fit.pjv.thedrake.game.Move;
import cz.cvut.fit.pjv.thedrake.ui.utils.TileBackgrounds;
import cz.cvut.fit.pjv.thedrake.ui.services.GameContext;
import cz.cvut.fit.pjv.thedrake.ui.utils.StyleHelper;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;



public class TileView extends Pane {
    private Tile tile;

    private final TileBackgrounds backgrounds = new TileBackgrounds();

    private final BoardPos position;

    private final GameContext context;

    private final ImageView moveImage;
    private Move move;

    public TileView(Tile tile, BoardPos position, GameContext context) {
        this.tile = tile;
        this.position = position;
        this.context = context;

        setPrefSize(100, 100);
        setMinSize(80, 80);
        setMaxSize(120, 120);

        getStyleClass().add("tile-view");
        update();
        setOnMouseClicked(e -> onClicked());

        moveImage = new ImageView(getClass().getResource("/images/tiles/move.png").toString());
        moveImage.setVisible(false);
        getChildren().add(moveImage);
    }

    public void setTile(Tile tile) {
        this.tile = tile;
        update();
    }

    public BoardPos position() {
        return position;
    }

    public void update() {
        setBackground(backgrounds.get(tile));
    }

    private void onClicked() {
        if(move != null) {
            context.executeMove(move);
        } else if(tile.hasTroop()) {
            select();
        } else {
            // Clicked on empty tile - clear any selection
            context.clearSelection();
        }
    }

    private void select() {
        StyleHelper.addClass(this, "tile-view-selected");
        context.tileViewSelected(this);
    }

    public void unselect() {
        StyleHelper.removeClass(this, "tile-view-selected");
    }

    public void setMove(Move move) {
        this.move = move;
        moveImage.setVisible(true);
    }
    public void clearMove() {
        this.move = null;
        moveImage.setVisible(false);
    }
}
