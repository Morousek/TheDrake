package cz.cvut.fit.pjv.thedrake.board;

import cz.cvut.fit.pjv.thedrake.game.GameState;
import cz.cvut.fit.pjv.thedrake.utils.JSONSerializable;
import cz.cvut.fit.pjv.thedrake.game.Move;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

public interface BoardTile extends Tile, JSONSerializable {
    BoardTile EMPTY = new BoardTile() {

        @Override
        public void toJSON(PrintWriter writer) {
            writer.write("\"" + this.toString() + "\"");
        }

        @Override
        public boolean canStepOn() {
            return true;
        }

        @Override
        public boolean hasTroop() {
            return false;
        }

        @Override
        public List<Move> movesFrom(BoardPos pos, GameState state) {
            return Collections.emptyList();
        }

        @Override
        public String toString() {
            return "empty";
        }
    };

    BoardTile MOUNTAIN = new BoardTile() {
        @Override
        public void toJSON(PrintWriter writer) {
            writer.write("\"" + this.toString() + "\"");
        }

        @Override
        public boolean canStepOn() {
            return false;
        }

        @Override
        public boolean hasTroop() {
            return false;
        }

        @Override
        public List<Move> movesFrom(BoardPos pos, GameState state) {
            return Collections.emptyList();
        }

        @Override
        public String toString() {
            return "mountain";
        }
    };
}
