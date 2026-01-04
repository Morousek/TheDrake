module cz.cvut.fit.pjv.thedrake {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens cz.cvut.fit.pjv.thedrake.ui to javafx.fxml;
    exports cz.cvut.fit.pjv.thedrake.ui;
    exports cz.cvut.fit.pjv.thedrake.ui.controllers;
    opens cz.cvut.fit.pjv.thedrake.ui.controllers to javafx.fxml;
    exports cz.cvut.fit.pjv.thedrake.ui.services;
    opens cz.cvut.fit.pjv.thedrake.ui.services to javafx.fxml;
    exports cz.cvut.fit.pjv.thedrake.ui.utils;
    opens cz.cvut.fit.pjv.thedrake.ui.utils to javafx.fxml;
}