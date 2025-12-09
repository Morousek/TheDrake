package cz.cvut.fit.pjv.thedrake.troops;

import cz.cvut.fit.pjv.thedrake.utils.JSONSerializable;
import cz.cvut.fit.pjv.thedrake.utils.Offset2D;
import cz.cvut.fit.pjv.thedrake.actions.TroopAction;

import java.io.PrintWriter;
import java.util.List;

public class Troop implements JSONSerializable {
    private final String name;
    private final Offset2D aversePivot;
    private final Offset2D reversePivot;
    private final List<TroopAction> aversActions;
    private final List<TroopAction> reversActions;

    public Troop(String name, Offset2D aversePivot, Offset2D reversePivot, List<TroopAction> aversActions, List<TroopAction> reversActions) {
        this.name = name;
        this.aversePivot = aversePivot;
        this.reversePivot = reversePivot;
        this.aversActions = aversActions;
        this.reversActions = reversActions;
    }

    public Troop(String name, Offset2D pivot, List<TroopAction> aversActions, List<TroopAction> reversActions) {
        this(name, pivot, pivot, aversActions, reversActions);
    }

    public Troop(String name, List<TroopAction> aversActions, List<TroopAction> reversActions) {
        this(name,  new Offset2D(1, 1), new Offset2D(1, 1), aversActions, reversActions);
    }

    public String name() {
        return name;
    }

    public Offset2D pivot(TroopFace face) {
        return face == TroopFace.AVERS ? aversePivot : reversePivot;
    }

    public List<TroopAction> actions(TroopFace face) {
        return face == TroopFace.AVERS ? aversActions : reversActions;
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.write("\"" + name + "\"");
    }
}
