package cz.cvut.fit.pjv.thedrake.troops;

import cz.cvut.fit.pjv.thedrake.utils.JSONSerializable;
import cz.cvut.fit.pjv.thedrake.actions.TroopAction;
import cz.cvut.fit.pjv.thedrake.board.BoardPos;
import cz.cvut.fit.pjv.thedrake.board.Tile;
import cz.cvut.fit.pjv.thedrake.game.GameState;
import cz.cvut.fit.pjv.thedrake.game.Move;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a tile occupied by a troop in the game.
 * Each TroopTile has a Troop, a PlayingSide, and a TroopFace indicating its orientation.
 */
public class TroopTile implements Tile, JSONSerializable {
    private Troop troop;
    private PlayingSide side;
    private TroopFace face;

    public TroopTile(Troop troop, PlayingSide side, TroopFace face) {
        this.troop = troop;
        this.side = side;
        this.face = face;
    }

    public PlayingSide side() {
        return side;
    }

    // Vrací stranu, na kterou je jednotka otočena
    public TroopFace face() {
        return face;
    }

    // Jednotka, která stojí na této dlaždici
    public Troop troop() {
        return troop;
    }

    // Vrací False, protože na dlaždici s jednotkou se nedá vstoupit
    public boolean canStepOn() {
        return false;
    }

    // Vrací True
    public boolean hasTroop() {
        return true;
    }

    // Vytvoří novou dlaždici, s jednotkou otočenou na opačnou stranu
    // (z rubu na líc nebo z líce na rub)
    public TroopTile flipped() {
        TroopFace flippedFace = face == TroopFace.AVERS ? TroopFace.REVERS : TroopFace.AVERS;
        return new TroopTile(troop, side, flippedFace);
    }

    @Override
    public List<Move> movesFrom(BoardPos pos, GameState state) {
        List<Move> result = new ArrayList<>();

        for (TroopAction action : troop.actions(face)) {
            result.addAll(action.movesFrom(pos, side, state));
        }

        return result;
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.write("{");

        writer.write("\"troop\":");
        troop.toJSON(writer);
        writer.write(",");

        writer.write("\"side\":");
        side.toJSON(writer);
        writer.write(",");

        writer.write("\"face\":");
        face.toJSON(writer);
        writer.write("}");
    }
}
