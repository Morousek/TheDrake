package cz.cvut.fit.pjv.thedrake.ui.utils;

import javafx.scene.Node;

import java.util.Arrays;
import java.util.Collection;

/**
 * Utility class for CSS style class manipulation.
 * Provides convenient methods to add, remove, and toggle style classes.
 */
public final class StyleHelper {

    private StyleHelper() {
        // Prevent instantiation
    }

    public static void addClass(Node node, String styleClass) {
        if (!node.getStyleClass().contains(styleClass)) {
            node.getStyleClass().add(styleClass);
        }
    }


    public static void removeClass(Node node, String styleClass) {
        node.getStyleClass().remove(styleClass);
    }

    public static void toggleClass(Node node, String styleClass, boolean add) {
        if (add) {
            addClass(node, styleClass);
        } else {
            removeClass(node, styleClass);
        }
    }

    public static void replaceClass(Node node, String oldClass, String newClass) {
        removeClass(node, oldClass);
        addClass(node, newClass);
    }

    public static void setExclusiveClass(Node node, String activeClass, String... allClasses) {
        for (String cls : allClasses) {
            if (cls.equals(activeClass)) {
                addClass(node, cls);
            } else {
                removeClass(node, cls);
            }
        }
    }
}
