package cz.cvut.fit.pjv.thedrake.troops;

import cz.cvut.fit.pjv.thedrake.utils.JSONSerializable;

import java.io.PrintWriter;

/**
 * Represents the two playing sides in the game.
 * The sides are ORANGE and BLUE.
 */
public enum PlayingSide implements JSONSerializable {
    ORANGE,
    BLUE;

    @Override
    public void toJSON(PrintWriter writer) {
        writer.write("\"" + this.toString() + "\"");
    }
}
