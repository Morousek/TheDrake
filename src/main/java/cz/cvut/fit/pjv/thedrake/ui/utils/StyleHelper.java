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

    /**
     * Add a style class to a node if it's not already present.
     *
     * @param node the target node
     * @param styleClass the style class to add
     */
    public static void addClass(Node node, String styleClass) {
        if (!node.getStyleClass().contains(styleClass)) {
            node.getStyleClass().add(styleClass);
        }
    }

    /**
     * Remove a style class from a node.
     *
     * @param node the target node
     * @param styleClass the style class to remove
     */
    public static void removeClass(Node node, String styleClass) {
        node.getStyleClass().remove(styleClass);
    }

    /**
     * Toggle a style class on a node.
     *
     * @param node the target node
     * @param styleClass the style class to toggle
     * @param add true to add, false to remove
     */
    public static void toggleClass(Node node, String styleClass, boolean add) {
        if (add) {
            addClass(node, styleClass);
        } else {
            removeClass(node, styleClass);
        }
    }

    /**
     * Replace one style class with another.
     *
     * @param node the target node
     * @param oldClass the class to remove
     * @param newClass the class to add
     */
    public static void replaceClass(Node node, String oldClass, String newClass) {
        removeClass(node, oldClass);
        addClass(node, newClass);
    }

    /**
     * Set exactly one class from a set of mutually exclusive classes.
     * Removes all other classes in the set and adds the specified one.
     *
     * @param node the target node
     * @param activeClass the class to set as active
     * @param allClasses all classes in the mutually exclusive set
     */
    public static void setExclusiveClass(Node node, String activeClass, String... allClasses) {
        for (String cls : allClasses) {
            if (cls.equals(activeClass)) {
                addClass(node, cls);
            } else {
                removeClass(node, cls);
            }
        }
    }

    /**
     * Remove multiple style classes at once.
     *
     * @param node the target node
     * @param styleClasses classes to remove
     */
    public static void removeClasses(Node node, String... styleClasses) {
        node.getStyleClass().removeAll(Arrays.asList(styleClasses));
    }

    /**
     * Remove multiple style classes at once.
     *
     * @param node the target node
     * @param styleClasses collection of classes to remove
     */
    public static void removeClasses(Node node, Collection<String> styleClasses) {
        node.getStyleClass().removeAll(styleClasses);
    }

    /**
     * Check if a node has a specific style class.
     *
     * @param node the target node
     * @param styleClass the style class to check
     * @return true if the node has the style class
     */
    public static boolean hasClass(Node node, String styleClass) {
        return node.getStyleClass().contains(styleClass);
    }
}
