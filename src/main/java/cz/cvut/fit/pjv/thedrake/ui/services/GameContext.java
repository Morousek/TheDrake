package cz.cvut.fit.pjv.thedrake.ui.services;

import cz.cvut.fit.pjv.thedrake.game.GameState;
import cz.cvut.fit.pjv.thedrake.game.Move;
import cz.cvut.fit.pjv.thedrake.troops.PlayingSide;
import cz.cvut.fit.pjv.thedrake.ui.views.TileView;


public interface GameContext {

    void tileViewSelected(TileView tileView);


    void executeMove(Move move);


    void stackTroopSelected(PlayingSide side);


    void clearSelection();


    GameState getGameState();


    boolean isOnTurn(PlayingSide side);

    boolean isStackSelected();
}
