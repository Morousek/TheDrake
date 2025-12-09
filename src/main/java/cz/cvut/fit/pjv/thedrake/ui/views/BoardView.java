package cz.cvut.fit.pjv.thedrake.ui.views;

import cz.cvut.fit.pjv.thedrake.board.BoardPos;
import cz.cvut.fit.pjv.thedrake.game.GameState;
import cz.cvut.fit.pjv.thedrake.game.Move;
import cz.cvut.fit.pjv.thedrake.ui.ValidMoves;
import cz.cvut.fit.pjv.thedrake.utils.PositionFactory;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.List;

public class BoardView extends GridPane implements TileViewContext {

    private GameState gameState;

    private TileView selectedTileView;

    private ValidMoves validMoves;

    public BoardView(GameState gameState) {
        this.gameState = gameState;
        validMoves = new ValidMoves(gameState);

        // Add CSS style class
        getStyleClass().add("board-view");

        // Set grid alignment to center
        setAlignment(Pos.CENTER);

        // Set gaps between tiles
        setHgap(2);
        setVgap(2);

        // Create grid of tiles
        PositionFactory positionFactory = gameState.board().positionFactory();
        int dimension = positionFactory.dimension();
        for(int y = 0; y < dimension; y++) {
            for(int x = 0; x < dimension; x++) {
                BoardPos boardPos = positionFactory.pos(x, dimension - y - 1);
                add(new TileView(gameState.tileAt(boardPos), boardPos, this), x, y);
            }
        }
    }

    private void updateTiles() {
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.setTile(gameState.tileAt(tileView.position()));
        }
    }

    @Override
    public void tileViewSelected(TileView tileView) {
        if(selectedTileView != null && selectedTileView != tileView){
            selectedTileView.unselect();
        }
        selectedTileView = tileView;

        hideMoves();
        showMoves(validMoves.boardMoves(tileView.position()));
    }

    @Override
    public void executeMove(Move move) {
        selectedTileView.unselect();
        selectedTileView = null;
        hideMoves();
        gameState = move.execute(gameState);
        validMoves = new ValidMoves(gameState);
        updateTiles();
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
        int index = (gameState.board().dimension() - 1 - target.j()) * 4 + target.i();
        return (TileView) getChildren().get(index);
    }
}
