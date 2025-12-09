package cz.cvut.fit.pjv.thedrake.board;

import cz.cvut.fit.pjv.thedrake.utils.JSONSerializable;
import cz.cvut.fit.pjv.thedrake.utils.PositionFactory;

import java.io.PrintWriter;

public class  Board implements JSONSerializable {
    private final BoardTile[][] tiles;
    private final int dimension;

    // Constructor. Creates square game board of given size (dimension = width = height), with all places empty (containing BoardTile.EMPTY)
    public Board(int dimension) {
        this.tiles = new BoardTile[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.tiles[i][j] = BoardTile.EMPTY;
            }
        }
        this.dimension = dimension;
    }

    // Size of the board
    public int dimension() {
        return dimension;
    }

    // Returns a tile on a provided position
    public BoardTile at(TilePos pos) {
        return tiles[pos.i()][pos.j()];
    }

    // Creates new board with new tiles provided by the ats parameter. All the other tiles stay the same
    public Board withTiles(TileAt... ats) {
        Board newBoard = new Board(this.dimension);
        for (int i = 0; i < this.dimension; i++) {
            newBoard.tiles[i] = this.tiles[i].clone();
        }
        for (TileAt at : ats) {
            newBoard.tiles[at.pos.i()][at.pos.j()] = at.tile;
        }
        return newBoard;
    }

    // Creates an instance of PositionFactory class for simpler creation of new position objects for this board
    public PositionFactory positionFactory() {
        return new PositionFactory(this.dimension);
    }

    public static class TileAt {
        public final BoardPos pos;
        public final BoardTile tile;

        public TileAt(BoardPos pos, BoardTile tile) {
            this.pos = pos;
            this.tile = tile;
        }
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.write('{');
        writer.write("\"dimension\":" + dimension + ",");
        writer.write("\"tiles\":[");
        for (int j = 0; j < dimension; j++) {
            for (int i = 0; i < dimension; i++) {
                tiles[i][j].toJSON(writer);
                if (i < dimension - 1 || j < dimension - 1) {
                    writer.write(",");
                }
            }
        }
        writer.write(']');
        writer.write("}");
    }
}

