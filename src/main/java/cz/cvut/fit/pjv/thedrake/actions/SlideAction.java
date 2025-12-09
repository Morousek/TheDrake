package cz.cvut.fit.pjv.thedrake.actions;

import cz.cvut.fit.pjv.thedrake.utils.Offset2D;
import cz.cvut.fit.pjv.thedrake.board.BoardPos;
import cz.cvut.fit.pjv.thedrake.board.TilePos;
import cz.cvut.fit.pjv.thedrake.game.GameState;
import cz.cvut.fit.pjv.thedrake.game.Move;
import cz.cvut.fit.pjv.thedrake.troops.PlayingSide;

import java.util.ArrayList;
import java.util.List;

public class SlideAction extends TroopAction {
    public SlideAction(Offset2D offset) {
        super(offset);
    }

    public SlideAction(int offsetX, int offsetY) {
        super(offsetX, offsetY);
    }

    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
        List<Move> result = new ArrayList<>();
        int i = 1;

        while (true) {
            Offset2D scaledOffset = new Offset2D(offset().x * i, offset().y * i);
            TilePos target = origin.stepByPlayingSide(scaledOffset, side);

            if (target == TilePos.OFF_BOARD) {
                break;
            }

            if (state.canStep(origin, target)) {
                result.add(new StepOnly(origin, (BoardPos) target));
            } else if (state.canCapture(origin, target)) {
                result.add(new StepAndCapture(origin, (BoardPos) target));
                break;  // After capture, we can't slide anymore'
            } else {
                break;  // Barrier cant move
            }

            i++;
        }
        return result;
    }
}
