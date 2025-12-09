package cz.cvut.fit.pjv.thedrake.actions;

import cz.cvut.fit.pjv.thedrake.board.BoardPos;
import cz.cvut.fit.pjv.thedrake.game.GameState;
import cz.cvut.fit.pjv.thedrake.game.Move;

public class PlaceFromStack extends Move {

    public PlaceFromStack(BoardPos target) {
        super(target);
    }

    @Override
    public GameState execute(GameState originState) {
        return originState.placeFromStack(target());
    }

}
