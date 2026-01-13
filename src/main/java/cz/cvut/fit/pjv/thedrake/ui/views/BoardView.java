package cz.cvut.fit.pjv.thedrake.ui.views;

import cz.cvut.fit.pjv.thedrake.board.BoardPos;
import cz.cvut.fit.pjv.thedrake.game.GameState;
import cz.cvut.fit.pjv.thedrake.game.Move;
import cz.cvut.fit.pjv.thedrake.ui.services.GameContext;
import cz.cvut.fit.pjv.thedrake.ui.services.ValidMoves;
import cz.cvut.fit.pjv.thedrake.utils.PositionFactory;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.List;

public class BoardView extends GridPane {

    private GameContext context;

    private TileView selectedTileView;

    private ValidMoves validMoves;

    public BoardView(GameContext context) {
        this.context = context;
        this.validMoves = new ValidMoves(context.getGameState());

        // Add CSS style class
        getStyleClass().add("board-view");

        // Set grid alignment to center
        setAlignment(Pos.CENTER);

        // Set gaps between tiles
        setHgap(2);
        setVgap(2);

        // Create grid of tiles
        GameState gameState = context.getGameState();
        PositionFactory positionFactory = gameState.board().positionFactory();
        int dimension = positionFactory.dimension();
        for(int y = 0; y < dimension; y++) {
            for(int x = 0; x < dimension; x++) {
                BoardPos boardPos = positionFactory.pos(x, dimension - y - 1);
                add(new TileView(gameState.tileAt(boardPos), boardPos, context), x, y);
            }
        }
    }


    public void update() {
        GameState gameState = context.getGameState();
        validMoves = new ValidMoves(gameState);
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.setTile(gameState.tileAt(tileView.position()));
        }
    }


    public void tileViewSelected(TileView tileView) {
        if(selectedTileView != null && selectedTileView != tileView){
            selectedTileView.unselect();
        }
        selectedTileView = tileView;

        hideMoves();
        showMoves(validMoves.boardMoves(tileView.position()));
    }


    public void clearSelection() {
        if (selectedTileView != null) {
            selectedTileView.unselect();
            selectedTileView = null;
        }
        hideMoves();
    }


    public void showStackMoves() {
        hideMoves();
        showMoves(validMoves.movesFromStack());
    }

    private void showMoves(List<Move> moves) {
        for(Move move : moves) {
            tileViewAt(move.target()).setMove(move);
        }
    }

    private void hideMoves() {
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.clearMove();
        }
    }

    private TileView tileViewAt(BoardPos target) {
        GameState gameState = context.getGameState();
        int index = (gameState.board().dimension() - 1 - target.j()) * gameState.board().dimension() + target.i();
        return (TileView) getChildren().get(index);
    }
}
