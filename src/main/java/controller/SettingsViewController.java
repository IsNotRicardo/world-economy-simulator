package controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.simulation.SimulationConfig;

public class SettingsViewController {
    private static final int TAB_OFFSET_VALUE = 68;
    private static final int TAB_HEIGHT = 48;

    @FXML
    private TabPane tabPane;
    @FXML
    private VBox simulationSettingsVBox;
    @FXML
    private TextField simulationTimeField;
    @FXML
    private TextField simulationDelayField;
    @FXML
    private TextField countrySupplySampleField;
    @FXML
    private TextField populationSegmentSizeField;

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
        tabPane.tabMinHeightProperty().set(TAB_HEIGHT);
        tabPane.tabMaxHeightProperty().set(TAB_HEIGHT);

        Insets margin = new Insets(30, 30, 0, 30);
        simulationSettingsVBox.getChildren().forEach(child -> {
            if (child instanceof HBox) {
                VBox.setMargin(child, margin);
            }
        });

        simulationTimeField.setText(Integer.toString(SimulationConfig.getSimulationTime()));
        simulationDelayField.setText(Integer.toString(SimulationConfig.getSimulationDelay()));
        countrySupplySampleField.setText(Integer.toString(SimulationConfig.getSupplyArchiveTime()));
        populationSegmentSizeField.setText(Integer.toString(SimulationConfig.getPopulationSegmentSize()));
    }
}
