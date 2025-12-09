package cz.cvut.fit.pjv.thedrake.game;

import cz.cvut.fit.pjv.thedrake.utils.JSONSerializable;

import java.io.PrintWriter;

public enum GameResult implements JSONSerializable {
    VICTORY, DRAW, IN_PLAY;

    @Override
    public void toJSON(PrintWriter writer) {
        writer.write("\"" + this.toString() + "\"" );
    }
}
