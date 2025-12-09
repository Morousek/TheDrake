package cz.cvut.fit.pjv.thedrake.actions;

import cz.cvut.fit.pjv.thedrake.utils.Offset2D;
import cz.cvut.fit.pjv.thedrake.board.BoardPos;
import cz.cvut.fit.pjv.thedrake.game.GameState;
import cz.cvut.fit.pjv.thedrake.game.Move;
import cz.cvut.fit.pjv.thedrake.troops.PlayingSide;

import java.util.List;

public abstract class TroopAction {
    private final Offset2D offset;

    protected TroopAction(int offsetX, int offsetY) {
        this(new Offset2D(offsetX, offsetY));
    }

    public TroopAction(Offset2D offset) {
        this.offset = offset;
    }

    public Offset2D offset() {
        return offset;
    }

    public abstract List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state);
}
