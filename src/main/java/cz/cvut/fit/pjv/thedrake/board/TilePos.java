package cz.cvut.fit.pjv.thedrake.board;

import cz.cvut.fit.pjv.thedrake.utils.JSONSerializable;
import cz.cvut.fit.pjv.thedrake.utils.Offset2D;
import cz.cvut.fit.pjv.thedrake.troops.PlayingSide;

import java.io.PrintWriter;
import java.util.List;

/**
 * Represents a position of tile on the game board.
 * A tile position is identified by its column (a character from 'A' to 'H')
 */
public interface TilePos extends JSONSerializable {
    public static final TilePos OFF_BOARD = new TilePos() {

        @Override
        public void toJSON(PrintWriter writer) {
            writer.write("\"" + this.toString() + "\"");
        }

        @Override
        public int i() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int j() {
            throw new UnsupportedOperationException();
        }

        @Override
        public char column() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int row() {
            throw new UnsupportedOperationException();
        }

        @Override
        public TilePos step(int columnStep, int rowStep) {
            throw new UnsupportedOperationException();
        }

        @Override
        public TilePos step(Offset2D step) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<TilePos> neighbours() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isNextTo(TilePos pos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public TilePos stepByPlayingSide(Offset2D dir, PlayingSide side) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equalsTo(int i, int j) {
            return false;
        }

        @Override
        public String toString() {
            return "off-board";
        }
    };

    public int i();

    public int j();

    public char column();

    public int row();

    /**
     * Creates new coordinate in a direction of the given steps.
     * @param columnStep
     * @param rowStep
     * @return
     */
    public TilePos step(int columnStep, int rowStep);

    public TilePos step(Offset2D step);

    public List<? extends TilePos> neighbours();

    public boolean isNextTo(TilePos pos);

    /**
     * Crates new coordinate in direction of the given offset,
     * taking into account the playing side.
     * If the side is ORANGE, the offset is done with its y coordinate flipped.
     * @param dir
     * @param side
     * @return
     */
    public TilePos stepByPlayingSide(Offset2D dir, PlayingSide side);

    public boolean equalsTo(int i, int j);
}
