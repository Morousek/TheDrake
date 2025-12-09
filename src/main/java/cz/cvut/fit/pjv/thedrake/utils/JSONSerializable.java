package cz.cvut.fit.pjv.thedrake.utils;

import java.io.PrintWriter;

public interface JSONSerializable {
    public void toJSON(PrintWriter writer);
}
