module cz.cvut.fit.pjv.thedrake {
    requires javafx.controls;
    requires javafx.fxml;


    opens cz.cvut.fit.pjv.thedrake to javafx.fxml;
    exports cz.cvut.fit.pjv.thedrake;
}