package cz.cvut.fit.pjv.thedrake.game;

import cz.cvut.fit.pjv.thedrake.board.*;
import cz.cvut.fit.pjv.thedrake.troops.Army;
import cz.cvut.fit.pjv.thedrake.troops.PlayingSide;
import cz.cvut.fit.pjv.thedrake.troops.Troop;
import cz.cvut.fit.pjv.thedrake.utils.JSONSerializable;

import java.io.PrintWriter;

public class GameState implements JSONSerializable {
    private final Board board;
    private final PlayingSide sideOnTurn;
    private final Army blueArmy;
    private final Army orangeArmy;
    private final GameResult result;

    public GameState(
            Board board,
            Army blueArmy,
            Army orangeArmy) {
        this(board, blueArmy, orangeArmy, PlayingSide.BLUE, GameResult.IN_PLAY);
    }

    public GameState(
            Board board,
            Army blueArmy,
            Army orangeArmy,
            PlayingSide sideOnTurn,
            GameResult result) {
        this.board = board;
        this.sideOnTurn = sideOnTurn;
        this.blueArmy = blueArmy;
        this.orangeArmy = orangeArmy;
        this.result = result;
    }

    public Board board() {
        return board;
    }

    public PlayingSide sideOnTurn() {
        return sideOnTurn;
    }

    public GameResult result() {
        return result;
    }

    public Army army(PlayingSide side) {
        if (side == PlayingSide.BLUE) {
            return blueArmy;
        }

        return orangeArmy;
    }

    public Army armyOnTurn() {
        return army(sideOnTurn);
    }

    public Army armyNotOnTurn() {
        if (sideOnTurn == PlayingSide.BLUE)
            return orangeArmy;

        return blueArmy;
    }

    public Tile tileAt(TilePos pos) {
        BoardTroops blueTroops = blueArmy.boardTroops();
        BoardTroops orangeTroops = orangeArmy.boardTroops();
        if (blueTroops.at(pos).isPresent()) {
            return blueTroops.at(pos).get();
        } else if (orangeTroops.at(pos).isPresent()) {
            return orangeTroops.at(pos).get();
        } else {
            return board.at(pos);
        }
    }

    private boolean canStepFrom(TilePos origin) {
        if (result != GameResult.IN_PLAY || origin == TilePos.OFF_BOARD || !tileAt(origin).hasTroop()) {
            return false;
        }
        if (armyNotOnTurn().boardTroops().troopPositions().contains(origin)) {
            return false;
        }
        // If guards are being placed, no troop movement allowed
        if (armyOnTurn().boardTroops().isPlacingGuards()) {
            return false;
        }
        return true;
    }

    private boolean canStepTo(TilePos target) {
        if (result != GameResult.IN_PLAY || target == TilePos.OFF_BOARD || !tileAt(target).canStepOn()) {
            return false;
        }
        return true;
    }

    private boolean canCaptureOn(TilePos target) {
        if (result != GameResult.IN_PLAY || target == TilePos.OFF_BOARD || !tileAt(target).hasTroop() ) {
            return false;
        }
        if (!armyNotOnTurn().boardTroops().troopPositions().contains(target)) {
            return false;
        }
        return true;
    }

    public boolean canStep(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canStepTo(target);
    }

    public boolean canCapture(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canCaptureOn(target);
    }

    public boolean canPlaceFromStack(TilePos target) {
        if (result != GameResult.IN_PLAY ) {
            return false;
        }
        if (armyOnTurn().stack().isEmpty()) {
            return false;
        }
        if (!canStepTo(target)) {
            return false;
        }
        // Start phase - leader placing
        if (!armyOnTurn().boardTroops().isLeaderPlaced()) {
            // Blue player can place leader only on the first row, orange on the last
            int requiredRow = (sideOnTurn == PlayingSide.BLUE) ? 1 : board.dimension();
            return target.row() == requiredRow;
        }

        // Guard placing phase
        if (armyOnTurn().boardTroops().isPlacingGuards()) {
            TilePos leaderPos = armyOnTurn().boardTroops().leaderPosition();
            // Guards must be placed next to the leader
            return target.isNextTo(leaderPos);
        }

        // Middlegame phase
        // Troops must be placed next to any friendly troop
        for (BoardPos friendlyPos : armyOnTurn().boardTroops().troopPositions()) {
            if (target.isNextTo(friendlyPos)) {
                return true;
            }
        }
        return false;
    }

    public GameState stepOnly(BoardPos origin, BoardPos target) {
        if (canStep(origin, target))
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY);

        throw new IllegalArgumentException();
    }

    public GameState stepAndCapture(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopStep(origin, target).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState captureOnly(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopFlip(origin).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState placeFromStack(BoardPos target) {
        if (canPlaceFromStack(target)) {
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().placeFromStack(target),
                    GameResult.IN_PLAY);
        }

        throw new IllegalArgumentException();
    }

    public GameState resign() {
        return createNewGameState(
                armyNotOnTurn(),
                armyOnTurn(),
                GameResult.VICTORY);
    }

    public GameState draw() {
        return createNewGameState(
                armyOnTurn(),
                armyNotOnTurn(),
                GameResult.DRAW);
    }

    private GameState createNewGameState(Army armyOnTurn, Army armyNotOnTurn, GameResult result) {
        if (armyOnTurn.side() == PlayingSide.BLUE) {
            return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
        }

        return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.write("{");
        writer.write("\"result\":");
        result.toJSON(writer);
        writer.write(",");
        writer.write("\"board\":");
        board.toJSON(writer);
        writer.write(",");
        writer.write("\"blueArmy\":");
        blueArmy.toJSON(writer);
        writer.write(",");
        writer.write("\"orangeArmy\":");
        orangeArmy.toJSON(writer);

        writer.write("}");
    }
}
