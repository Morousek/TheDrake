package cz.cvut.fit.pjv.thedrake.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TheDrakeApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TheDrakeApplication.class.getResource("/cz/cvut/fit/pjv/thedrake/fxml/main-menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        // Load main CSS (which imports all other stylesheets)
        scene.getStylesheets().add(getClass().getResource("/cz/cvut/fit/pjv/thedrake/css/main.css").toExternalForm());

        // Set window properties
        stage.setTitle("The Drake");
        stage.setMinWidth(1024);
        stage.setMinHeight(768);
        stage.setScene(scene);
        stage.show();
    }
}
