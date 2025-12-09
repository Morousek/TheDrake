package cz.cvut.fit.pjv.thedrake.troops;

import cz.cvut.fit.pjv.thedrake.utils.JSONSerializable;

import java.io.PrintWriter;

/**
 * Represents the two faces of a troop in the game.
 * A troop can be either on its AVERS (front) side or REVERS (back) side.
 */
public enum TroopFace implements JSONSerializable {
    AVERS,
    REVERS;

    @Override
    public void toJSON(PrintWriter writer) {
        writer.write("\"" + this.toString() + "\"");
    }
}
