package cz.cvut.fit.pjv.thedrake.ui.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Utility class for navigating between scenes in the application.
 * Centralizes scene switching logic to avoid code duplication across controllers.
 */
public final class SceneNavigator {

    // FXML paths as constants
    public static final String MAIN_MENU = "/cz/cvut/fit/pjv/thedrake/fxml/main-menu-view.fxml";
    public static final String GAME_VIEW = "/cz/cvut/fit/pjv/thedrake/fxml/game-view.fxml";
    public static final String GAME_SETUP = "/cz/cvut/fit/pjv/thedrake/fxml/game-setup-view.fxml";
    public static final String GAME_OVER = "/cz/cvut/fit/pjv/thedrake/fxml/game-over-view.fxml";

    private SceneNavigator() {
        // Prevent instantiation
    }


    public static <T> T navigateTo(Node sourceNode, String fxmlPath, String title) {
        return navigateTo(sourceNode, fxmlPath, title, null);
    }


    public static <T> T navigateTo(Node sourceNode, String fxmlPath, String title,
                                    Consumer<T> controllerInitializer) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxmlPath));
            Parent root = loader.load();
            
            T controller = loader.getController();
            
            // Initialize controller if callback provided
            if (controllerInitializer != null && controller != null) {
                controllerInitializer.accept(controller);
            }

            Scene scene = sourceNode.getScene();
            Stage stage = (Stage) scene.getWindow();

            // Preserve window size
            double width = stage.getWidth();
            double height = stage.getHeight();

            scene.setRoot(root);

            stage.setWidth(width);
            stage.setHeight(height);
            
            if (title != null) {
                stage.setTitle(title);
            }

            return controller;
        } catch (IOException e) {
            System.err.println("Failed to load FXML: " + fxmlPath);
            e.printStackTrace();
            return null;
        }
    }


    public static <T> LoadResult<T> loadFxml(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxmlPath));
            Parent root = loader.load();
            T controller = loader.getController();
            return new LoadResult<>(root, controller);
        } catch (IOException e) {
            System.err.println("Failed to load FXML: " + fxmlPath);
            e.printStackTrace();
            return null;
        }
    }


    public static class LoadResult<T> {
        private final Parent root;
        private final T controller;

        public LoadResult(Parent root, T controller) {
            this.root = root;
            this.controller = controller;
        }

        public Parent getRoot() {
            return root;
        }

        public T getController() {
            return controller;
        }
    }
}
