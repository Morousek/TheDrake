package cz.cvut.fit.pjv.thedrake.actions;

import cz.cvut.fit.pjv.thedrake.board.BoardPos;
import cz.cvut.fit.pjv.thedrake.game.BoardMove;
import cz.cvut.fit.pjv.thedrake.game.GameState;

public class StepOnly extends BoardMove {

    public StepOnly(BoardPos origin, BoardPos target) {
        super(origin, target);
    }

    @Override
    public GameState execute(GameState originState) {
        return originState.stepOnly(origin(), target());
    }

}
