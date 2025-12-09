package cz.cvut.fit.pjv.thedrake.board;

import cz.cvut.fit.pjv.thedrake.troops.PlayingSide;
import cz.cvut.fit.pjv.thedrake.troops.Troop;
import cz.cvut.fit.pjv.thedrake.troops.TroopFace;
import cz.cvut.fit.pjv.thedrake.troops.TroopTile;
import cz.cvut.fit.pjv.thedrake.utils.JSONSerializable;

import java.io.PrintWriter;
import java.util.*;

public class BoardTroops  implements JSONSerializable {
    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final TilePos leaderPosition;
    private final int guards;

    public BoardTroops(PlayingSide playingSide) {
        this(
                playingSide,
                Collections.emptyMap(),
                TilePos.OFF_BOARD,
                0);
    }

    public BoardTroops(
            PlayingSide playingSide,
            Map<BoardPos, TroopTile> troopMap,
            TilePos leaderPosition,
            int guards) {
        this.playingSide = playingSide;
        this.troopMap = troopMap;
        this.leaderPosition = leaderPosition;
        this.guards = guards;
    }

    public Optional<TroopTile> at(TilePos pos) {
        return Optional.ofNullable(troopMap.get(pos));
    }

    public PlayingSide playingSide() {
        return playingSide;
    }

    public TilePos leaderPosition() {
        return leaderPosition;
    }

    public int guards() {
        return guards;
    }

    public boolean isLeaderPlaced() {
        return leaderPosition != TilePos.OFF_BOARD;
    }

    public boolean isPlacingGuards() {
        if (isLeaderPlaced()) {
            return guards < 2;
        }
        return false;
    }

    public Set<BoardPos> troopPositions() {
        return Collections.unmodifiableSet(troopMap.keySet());
    }

    public BoardTroops placeTroop(Troop troop, BoardPos target) {
        if (at(target).isPresent()) {
            throw new IllegalArgumentException(
                    "Cannot place troop on an occupied position.");
        }
        TroopTile tile = new TroopTile(troop, playingSide, TroopFace.AVERS);
        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        newTroops.put(target, tile);

        if (!isLeaderPlaced()) {
            return new BoardTroops(playingSide(), newTroops, target, guards);
        } else if (isPlacingGuards()) {
            return new BoardTroops(playingSide(), newTroops, leaderPosition, guards + 1);
        } else {
            return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
        }
    }

    public BoardTroops troopStep(BoardPos origin, BoardPos target) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }
        if (at(origin).isEmpty() || at(target).isPresent()) {
            throw new IllegalArgumentException(
                    "Invalid troop move: origin must have a troop and target must be empty.");
        }
        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(target, tile.flipped());
        if (origin.equals(leaderPosition)) {
            return new BoardTroops(playingSide(), newTroops, target, guards);
        } else {
            return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
        }
    }

    public BoardTroops troopFlip(BoardPos origin) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (!at(origin).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(origin, tile.flipped());

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
    }

    public BoardTroops removeTroop(BoardPos target) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }
        if (at(target).isEmpty()) {
            throw new IllegalArgumentException(
                    "Invalid troop removal: target position must have a troop.");
        }
        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        newTroops.remove(target);
        if (target.equals(leaderPosition)) {
            return new BoardTroops(playingSide(), newTroops, TilePos.OFF_BOARD, guards);
        }
        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.write("{");

        writer.write("\"side\":");
        playingSide.toJSON(writer);
        writer.write(",");

        writer.write("\"leaderPosition\":\"" + leaderPosition + "\",");
        writer.write("\"guards\":" + guards + ",");
        writer.write("\"troopMap\":{");
        // "troopMap": {
        //        "a1": {
        //          "troop": "Drake",
        //          "side": "BLUE",
        //          "face": "AVERS"
        //        },
        List<BoardPos> sortedTroopPositions = new ArrayList<>(troopMap.keySet());
        sortedTroopPositions.sort(Comparator.comparing(BoardPos::toString));

        for (BoardPos eachTroopPosition : sortedTroopPositions) {
            TroopTile tile = troopMap.get(eachTroopPosition);
            eachTroopPosition.toJSON(writer);
            writer.write(":");
            tile.toJSON(writer);
            if (eachTroopPosition != sortedTroopPositions.getLast()) {
                writer.write(",");
            }
        }
        writer.write("}");
        writer.write("}");
    }
}
