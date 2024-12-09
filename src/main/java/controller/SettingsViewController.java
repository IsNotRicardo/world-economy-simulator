package controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

public class SettingsViewController {
    private static final int TAB_OFFSET_VALUE = 55;

    @FXML
    private TabPane tabPane;

    @FXML
    public void initialize() {
        tabPane.tabMinWidthProperty().bind(Bindings.createDoubleBinding(
                () -> (tabPane.getWidth() - TAB_OFFSET_VALUE) / tabPane.getTabs().size(),
                tabPane.widthProperty(), tabPane.getTabs()
        ));
        tabPane.tabMaxWidthProperty().bind(Bindings.createDoubleBinding(
                () -> (tabPane.getWidth() - TAB_OFFSET_VALUE) / tabPane.getTabs().size(),
                tabPane.widthProperty(), tabPane.getTabs()
        ));
    }
}
