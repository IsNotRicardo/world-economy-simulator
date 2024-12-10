package controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import entity.CountryEntity;
import model.core.Country;
import model.core.Resource;
import model.core.ResourceNodeDTO;
import model.simulation.SimulationConfig;

import java.util.*;

public class SettingsViewController {
    private static final int TAB_OFFSET_VALUE = 68;
    private static final int TAB_HEIGHT = 48;

    private List<Resource> resourceList = new ArrayList<>();
    private List<CountryEntity> countryList = new ArrayList<>();
    private Map<CountryEntity, Map<Resource, ResourceNodeDTO>> countryResourceNodes = new HashMap<>();

    private boolean isResourceEditingMode;
    private boolean isCountryEditingMode;
    private boolean isResourceNodeEditingMode;

    private int currentResourceIndex;
    private int currentCountryIndex;
    private CountryEntity selectedCountry;
    private Resource selectedResource;

    @FXML
    private TabPane tabPane;
    @FXML
    private VBox simulationSettingsVBox;
    @FXML
    private TextField simulationTimeField;
    @FXML
    private Label simulationTimeErrorLabel;
    @FXML
    private TextField simulationDelayField;
    @FXML
    private Label simulationDelayErrorLabel;
    @FXML
    private TextField countrySupplySampleField;
    @FXML
    private Label countrySupplySampleErrorLabel;
    @FXML
    private TextField populationSegmentSizeField;
    @FXML
    private Label populationSegmentSizeErrorLabel;
    @FXML
    private ListView<String> resourceListView;
    @FXML
    private VBox resourceSettingsVBox;
    @FXML
    private Label resourceItemsLabel;
    @FXML
    private TextField resourceNameField;
    @FXML
    private TextField resourcePriorityField;
    @FXML
    private TextField resourceBaseCapacityField;
    @FXML
    private TextField resourceProductionCostField;
    @FXML
    private Label resourceNameErrorLabel;
    @FXML
    private Label resourcePriorityErrorLabel;
    @FXML
    private Label resourceBaseCapacityErrorLabel;
    @FXML
    private Label resourceProductionCostErrorLabel;
    @FXML
    private Button addResourceButton;
    @FXML
    private Button saveResourceButton;
    @FXML
    private Region resourceOptionalRegion;
    @FXML
    private Button deleteResourceButton;
    @FXML
    private ListView<String> countryListView;
    @FXML
    private VBox countrySettingsVBox;
    @FXML
    private Label countryItemsLabel;
    @FXML
    private TextField countryNameField;
    @FXML
    private TextField countryInitialMoneyField;
    @FXML
    private TextField countryInitialPopulationField;
    @FXML
    private Label countryNameErrorLabel;
    @FXML
    private Label countryInitialMoneyErrorLabel;
    @FXML
    private Label countryInitialPopulationErrorLabel;
    @FXML
    private Button addCountryButton;
    @FXML
    private Button saveCountryButton;
    @FXML
    private Region countryOptionalRegion;
    @FXML
    private Button deleteCountryButton;
    @FXML
    private ListView<String> resourceNodeListView;
    @FXML
    private VBox resourceNodeSettingsVBox;
    @FXML
    private Label resourceNodeItemsLabel;
    @FXML
    private ComboBox<String> countryComboBox;
    @FXML
    private ComboBox<String> resourceComboBox;
    @FXML
    private TextField resourceNodeTierField;
    @FXML
    private TextField resourceNodeBaseCapacityField;
    @FXML
    private TextField resourceNodeProductionCostField;
    @FXML
    private Label resourceNodeTierErrorLabel;
    @FXML
    private Label resourceNodeBaseCapacityErrorLabel;
    @FXML
    private Label resourceNodeProductionCostErrorLabel;
    @FXML
    private Button addResourceNodeButton;
    @FXML
    private Button saveResourceNodeButton;
    @FXML
    private Region resourceNodeOptionalRegion;
    @FXML
    private Button deleteResourceNodeButton;

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
        simulationSettingsVBox.getChildren().forEach(child -> VBox.setMargin(child, margin));

        simulationTimeField.setText(Integer.toString(SimulationConfig.getSimulationTime()));
        simulationDelayField.setText(Integer.toString(SimulationConfig.getSimulationDelay()));
        countrySupplySampleField.setText(Integer.toString(SimulationConfig.getSupplyArchiveTime()));
        populationSegmentSizeField.setText(Integer.toString(SimulationConfig.getPopulationSegmentSize()));

        simulationTimeField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateGeneralField(simulationTimeField, simulationTimeErrorLabel, SimulationConfig.getSimulationTime(), 1, "Simulation time must be positive");
            }
        });
        simulationDelayField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateGeneralField(simulationDelayField, simulationDelayErrorLabel, SimulationConfig.getSimulationDelay(), 1, "Simulation delay must be positive");
            }
        });
        countrySupplySampleField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateGeneralField(countrySupplySampleField, countrySupplySampleErrorLabel, SimulationConfig.getSupplyArchiveTime(), 2, "Supply archive sample must be greater than 1");
            }
        });
        populationSegmentSizeField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateGeneralField(populationSegmentSizeField, populationSegmentSizeErrorLabel, SimulationConfig.getPopulationSegmentSize(), 1, "Population segment size must be positive");
            }
        });

        Insets titleMargin = new Insets(0, 30, 0, 30);

        resourceSettingsVBox.getChildren().forEach(child -> {
            if (child instanceof Label) {
                VBox.setMargin(child, titleMargin);
            } else {
                VBox.setMargin(child, margin);
            }
        });
        changeResourceButtonVisibility(false);

        countrySettingsVBox.getChildren().forEach(child -> {
            if (child instanceof Label) {
                VBox.setMargin(child, titleMargin);
            } else {
                VBox.setMargin(child, margin);
            }
        });
        changeCountryButtonVisibility(false);

        resourceNodeSettingsVBox.getChildren().forEach(child -> {
            if (child instanceof Label) {
                VBox.setMargin(child, titleMargin);
            } else {
                VBox.setMargin(child, margin);
            }
        });
        changeResourceNodeButtonVisibility(false);

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab.getText().equals("Resource Nodes")) {
                populateCountryComboBox();
                resourceNodeListView.getItems().clear();
                resourceComboBox.getItems().clear();
                clearResourceNodeFields();
            }
        });

        resourceComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedResource = resourceList.stream()
                        .filter(resource -> resource.name().equals(newValue))
                        .findFirst()
                        .orElse(null);

                if (selectedCountry != null && selectedResource != null) {
                    Map<Resource, ResourceNodeDTO> resourceNodes = countryResourceNodes.get(selectedCountry);
                    if (resourceNodes != null && resourceNodes.containsKey(selectedResource)) {
                        changeResourceNodeButtonVisibility(false);
                    }
                }
            }
        });

        countryComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedCountry = countryList.stream()
                        .filter(country -> country.getName().equals(newValue))
                        .findFirst()
                        .orElse(null);
                populateResourceNodeListView();
                populateResourceComboBox();
                clearResourceNodeFields();
            }
        });
    }

    private void validateGeneralField(TextField textField, Label errorLabel, int defaultValue, int minValue, String errorMessage) {
        try {
            int value = Integer.parseInt(textField.getText());

            if (value < minValue) {
                textField.setText(Integer.toString(defaultValue));
                errorLabel.setText(errorMessage);
            } else {
                errorLabel.setText("");
            }
        } catch (NumberFormatException e) {
            textField.setText(Integer.toString(defaultValue));
            errorLabel.setText("Input must be an integer");
        }
    }

    private boolean validateIntField(TextField textField, Label errorLabel, int minValue, String errorMessage) {
        try {
            int value = Integer.parseInt(textField.getText());
            if (value < minValue) {
                errorLabel.setText(errorMessage);
                return false;
            } else {
                errorLabel.setText("");
                return true;
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Input must be an integer");
            return false;
        }
    }

    private boolean validateDoubleField(TextField textField, Label errorLabel, double minValue, double maxValue, String errorMessage) {
        try {
            double value = Double.parseDouble(textField.getText());
            if (value < minValue || value > maxValue) {
                errorLabel.setText(errorMessage);
                return false;
            } else {
                errorLabel.setText("");
                return true;
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Input must be a number");
            return false;
        }
    }

    private boolean validateStringField(TextField textField, Label errorLabel, String errorMessage) {
        if (textField.getText() == null || textField.getText().isEmpty()) {
            errorLabel.setText(errorMessage);
            return false;
        } else {
            errorLabel.setText("");
            return true;
        }
    }

    private boolean validateResourceFields() {
        boolean isNameValid = validateStringField(resourceNameField, resourceNameErrorLabel,
                "Name must not be empty");
        boolean isPriorityValid = validateDoubleField(resourcePriorityField, resourcePriorityErrorLabel,
                0.0, 1.0, "Priority must be between 0 and 1");
        boolean isBaseCapacityValid = validateIntField(resourceBaseCapacityField, resourceBaseCapacityErrorLabel,
                1, "Base capacity must be positive");
        boolean isProductionCostValid = validateDoubleField(resourceProductionCostField, resourceProductionCostErrorLabel,
                0.0, Double.MAX_VALUE, "Production cost cannot be negative");

        return isNameValid && isPriorityValid && isBaseCapacityValid && isProductionCostValid;
    }

    private boolean validateCountryFields() {
        boolean isNameValid = validateStringField(countryNameField, countryNameErrorLabel, "Name must not be empty");
        boolean isInitialMoneyValid = validateDoubleField(countryInitialMoneyField, countryInitialMoneyErrorLabel, 0.0, Double.MAX_VALUE, "Initial money cannot be negative");
        boolean isInitialPopulationValid = validateIntField(countryInitialPopulationField, countryInitialPopulationErrorLabel, 1, "Initial population must be positive");

        return isNameValid && isInitialMoneyValid && isInitialPopulationValid;
    }

    private boolean validateResourceNodeFields() {
        boolean isTierValid = validateIntField(resourceNodeTierField, resourceNodeTierErrorLabel, 0, "Tier cannot be negative");
        boolean isBaseCapacityValid = validateIntField(resourceNodeBaseCapacityField, resourceNodeBaseCapacityErrorLabel, 1, "Base capacity must be positive");
        boolean isProductionCostValid = validateDoubleField(resourceNodeProductionCostField, resourceNodeProductionCostErrorLabel, 0.0, Double.MAX_VALUE, "Production cost cannot be negative");

        return isTierValid && isBaseCapacityValid && isProductionCostValid;
    }

    @FXML
    public void newResource() {
        if (resolveIsResourceNotSaved()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("New Resource");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to create a new resource?\n" +
                    "Any unsaved changes will be lost.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.OK) {
                return;
            }
        }
        clearResourceFields();
        changeResourceButtonVisibility(false);
        resourceListView.getSelectionModel().clearSelection();
    }

    @FXML
    public void addResource() {
        if (validateResourceFields()) {
            String name = resourceNameField.getText();
            double priority = Double.parseDouble(resourcePriorityField.getText());
            int baseCapacity = Integer.parseInt(resourceBaseCapacityField.getText());
            double productionCost = Double.parseDouble(resourceProductionCostField.getText());

            // Category needs to be removed later
            Resource newResource = new Resource(name, priority, baseCapacity, productionCost);
            resourceList.add(newResource);
            resourceListView.getItems().add(name);
            clearResourceFields();
        }
    }

    @FXML
    public void handleResourceSelection() {
        if (resolveIsResourceNotSaved()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Select Resource");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to change resources?\n" +
                    "Any unsaved changes will be lost.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.OK) {
                resourceListView.getSelectionModel().select(currentResourceIndex);
                return;
            }
        }
        int selectedIndex = resourceListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1) {
            resourceNameField.setText(resourceList.get(selectedIndex).name());
            resourcePriorityField.setText(String.valueOf(resourceList.get(selectedIndex).priority()));
            resourceBaseCapacityField.setText(String.valueOf(resourceList.get(selectedIndex).baseCapacity()));
            resourceProductionCostField.setText(String.valueOf(resourceList.get(selectedIndex).productionCost()));
            changeResourceButtonVisibility(true);
            currentResourceIndex = selectedIndex;
        }
    }

    @FXML
    public void saveResource() {
        int selectedIndex = resourceListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            if (validateResourceFields()) {
                String name = resourceNameField.getText();
                double priority = Double.parseDouble(resourcePriorityField.getText());
                int baseCapacity = Integer.parseInt(resourceBaseCapacityField.getText());
                double productionCost = Double.parseDouble(resourceProductionCostField.getText());

                // Category needs to be removed later
                Resource updatedResource = new Resource(name, priority, baseCapacity, productionCost);
                resourceList.set(selectedIndex, updatedResource);
                resourceListView.getItems().set(selectedIndex, name);
                resourceListView.getSelectionModel().select(selectedIndex);
            }
        }
    }

    @FXML
    public void deleteResource() {
        int selectedIndex = resourceListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Resource");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this resource?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                resourceList.remove(selectedIndex);
                resourceListView.getItems().remove(selectedIndex);
                resourceListView.getSelectionModel().clearSelection();

                clearResourceFields();
                changeResourceButtonVisibility(false);
            }
        }
    }

    @FXML
    public void newCountry() {
        if (resolveIsCountryNotSaved()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("New Country");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to create a new country?\nAny unsaved changes will be lost.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.OK) {
                return;
            }
        }
        clearCountryFields();
        changeCountryButtonVisibility(false);
        countryListView.getSelectionModel().clearSelection();
    }

    @FXML
    public void addCountry() {
        if (validateCountryFields()) {
            String name = countryNameField.getText();
            double initialMoney = Double.parseDouble(countryInitialMoneyField.getText());
            int initialPopulation = Integer.parseInt(countryInitialPopulationField.getText());

            CountryEntity newCountry = new CountryEntity(name, initialMoney, initialPopulation);
            countryList.add(newCountry);
            countryListView.getItems().add(name);
            clearCountryFields();
        }
    }

    @FXML
    public void handleCountrySelection() {
        if (resolveIsCountryNotSaved()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Select Country");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to change countries?\nAny unsaved changes will be lost.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.OK) {
                countryListView.getSelectionModel().select(currentCountryIndex);
                return;
            }
        }
        int selectedIndex = countryListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1) {
            CountryEntity selectedCountry = countryList.get(selectedIndex);
            countryNameField.setText(selectedCountry.getName());
            countryInitialMoneyField.setText(String.valueOf(selectedCountry.getMoney()));
            countryInitialPopulationField.setText(String.valueOf(selectedCountry.getPopulation()));
            changeCountryButtonVisibility(true);
            currentCountryIndex = selectedIndex;
        }
    }

    @FXML
    public void saveCountry() {
        int selectedIndex = countryListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            if (validateCountryFields()) {
                String name = countryNameField.getText();
                double initialMoney = Double.parseDouble(countryInitialMoneyField.getText());
                int initialPopulation = Integer.parseInt(countryInitialPopulationField.getText());

                CountryEntity updatedCountry = new CountryEntity(name, initialMoney, initialPopulation);
                countryList.set(selectedIndex, updatedCountry);
                countryListView.getItems().set(selectedIndex, name);
                countryListView.getSelectionModel().select(selectedIndex);
            }
        }
    }

    @FXML
    public void deleteCountry() {
        int selectedIndex = countryListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Country");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this country?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                countryList.remove(selectedIndex);
                countryListView.getItems().remove(selectedIndex);
                countryListView.getSelectionModel().clearSelection();

                clearCountryFields();
                changeCountryButtonVisibility(false);
            }
        }
    }

    @FXML
    public void newResourceNode() {
        if (resolveIsResourceNodeNotSaved()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("New Resource Node");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to create a new resource node?\n" +
                    "Any unsaved changes will be lost.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.OK) {
                return;
            }
        }
        clearResourceNodeFields();
        changeResourceNodeButtonVisibility(false);
        resourceNodeListView.getSelectionModel().clearSelection();
    }

    @FXML
    public void addResourceNode() {
        if (validateResourceNodeFields()) {
            int tier = Integer.parseInt(resourceNodeTierField.getText());
            int baseCapacity = Integer.parseInt(resourceNodeBaseCapacityField.getText());
            double productionCost = Double.parseDouble(resourceNodeProductionCostField.getText());

            ResourceNodeDTO newResourceNode = new ResourceNodeDTO(tier, baseCapacity, productionCost, selectedResource);
            countryResourceNodes.computeIfAbsent(selectedCountry, k -> new HashMap<>()).put(selectedResource, newResourceNode);
            resourceNodeListView.getItems().add(selectedResource.name());

            populateResourceComboBox();
            selectedResource = null;
            clearResourceNodeFields();
        }
    }

    @FXML
    public void handleResourceNodeSelection() {
        if (resolveIsResourceNodeNotSaved()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Select Resource Node");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to change resource nodes?\n" +
                    "Any unsaved changes will be lost.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.OK) {
                return;
            }
        }
        int selectedIndex = resourceNodeListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1) {
            String selectedResourceName = resourceNodeListView.getItems().get(selectedIndex);
            selectedResource = resourceList.stream()
                    .filter(resource -> resource.name().equals(selectedResourceName))
                    .findFirst()
                    .orElse(null);

            if (selectedResource != null) {
                ResourceNodeDTO selectedNode = countryResourceNodes.get(selectedCountry).get(selectedResource);

                resourceNodeTierField.setText(String.valueOf(selectedNode.tier()));
                resourceNodeBaseCapacityField.setText(String.valueOf(selectedNode.baseCapacity()));
                resourceNodeProductionCostField.setText(String.valueOf(selectedNode.productionCost()));

                resourceComboBox.getSelectionModel().select(selectedResource.name());
                changeResourceNodeButtonVisibility(true);
            }
        }
    }

    @FXML
    public void saveResourceNode() {
        if (validateResourceNodeFields()) {
            int tier = Integer.parseInt(resourceNodeTierField.getText());
            int baseCapacity = Integer.parseInt(resourceNodeBaseCapacityField.getText());
            double productionCost = Double.parseDouble(resourceNodeProductionCostField.getText());

            ResourceNodeDTO updatedResourceNode = new ResourceNodeDTO(tier, baseCapacity, productionCost, selectedResource);
            countryResourceNodes.get(selectedCountry).put(selectedResource, updatedResourceNode);
        }
    }

    @FXML
    public void deleteResourceNode() {
        int selectedIndex = resourceNodeListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            countryResourceNodes.get(selectedCountry).remove(selectedResource);
            resourceNodeListView.getItems().remove(selectedIndex);
            clearResourceNodeFields();
        }
    }

    private void populateResourceNodeListView() {
        resourceNodeListView.getItems().clear();
        if (selectedCountry != null) {
            Map<Resource, ResourceNodeDTO> resourceNodes = countryResourceNodes.get(selectedCountry);
            if (resourceNodes != null) {
                resourceNodes.forEach((resource, resourceNode) -> resourceNodeListView.getItems().add(resource.name()));
            }
        }
    }

    private void populateResourceComboBox() {
        resourceComboBox.getItems().clear();
        if (selectedCountry != null) {
            Map<Resource, ResourceNodeDTO> resourceNodes = countryResourceNodes.get(selectedCountry);
            for (Resource resource : resourceList) {
                if (resourceNodes == null || !resourceNodes.containsKey(resource)) {
                    resourceComboBox.getItems().add(resource.name());
                }
            }
        }
    }

    private void populateCountryComboBox() {
        countryComboBox.getItems().clear();
        for (CountryEntity country : countryList) {
            countryComboBox.getItems().add(country.getName());
        }
    }

    private void changeResourceButtonVisibility(boolean editingMode) {
        // In editing mode, the add button is not visible, and the edit and delete buttons are visible
        isResourceEditingMode = editingMode;

        if (editingMode) {
            resourceItemsLabel.setText("Modify Resource");
        } else {
            resourceItemsLabel.setText("Insert Resource");
        }

        addResourceButton.setVisible(!editingMode);
        addResourceButton.setManaged(!editingMode);

        saveResourceButton.setVisible(editingMode);
        saveResourceButton.setManaged(editingMode);

        resourceOptionalRegion.setVisible(editingMode);
        resourceOptionalRegion.setManaged(editingMode);

        deleteResourceButton.setVisible(editingMode);
        deleteResourceButton.setManaged(editingMode);
    }

    private boolean resolveIsResourceNotSaved() {
        if (isResourceEditingMode) {
            String savedName = resourceList.get(currentResourceIndex).name();
            String savedPriority = String.valueOf(resourceList.get(currentResourceIndex).priority());
            String savedBaseCapacity = String.valueOf(resourceList.get(currentResourceIndex).baseCapacity());
            String savedProductionCost = String.valueOf(resourceList.get(currentResourceIndex).productionCost());

            return !resourceNameField.getText().equals(savedName) || !resourcePriorityField.getText().equals(savedPriority) ||
                    !resourceBaseCapacityField.getText().equals(savedBaseCapacity) || !resourceProductionCostField.getText().equals(savedProductionCost);
        } else {
            return !resourceNameField.getText().isEmpty() || !resourcePriorityField.getText().isEmpty() ||
                    !resourceBaseCapacityField.getText().isEmpty() || !resourceProductionCostField.getText().isEmpty();
        }
    }

    private void clearResourceFields() {
        resourceNameField.setText("");
        resourcePriorityField.setText("");
        resourceBaseCapacityField.setText("");
        resourceProductionCostField.setText("");

        resourceNameErrorLabel.setText("");
        resourcePriorityErrorLabel.setText("");
        resourceBaseCapacityErrorLabel.setText("");
        resourceProductionCostErrorLabel.setText("");
    }

    private void changeCountryButtonVisibility(boolean editingMode) {
        isCountryEditingMode = editingMode;

        if (editingMode) {
            countryItemsLabel.setText("Modify Country");
        } else {
            countryItemsLabel.setText("Insert Country");
        }

        addCountryButton.setVisible(!editingMode);
        addCountryButton.setManaged(!editingMode);

        saveCountryButton.setVisible(editingMode);
        saveCountryButton.setManaged(editingMode);

        countryOptionalRegion.setVisible(editingMode);
        countryOptionalRegion.setManaged(editingMode);

        deleteCountryButton.setVisible(editingMode);
        deleteCountryButton.setManaged(editingMode);
    }

    private boolean resolveIsCountryNotSaved() {
        if (isCountryEditingMode) {
            CountryEntity savedCountry = countryList.get(currentCountryIndex);
            String savedName = savedCountry.getName();
            String savedInitialMoney = String.valueOf(savedCountry.getMoney());
            String savedInitialPopulation = String.valueOf(savedCountry.getPopulation());

            return !countryNameField.getText().equals(savedName) || !countryInitialMoneyField.getText().equals(savedInitialMoney) ||
                    !countryInitialPopulationField.getText().equals(savedInitialPopulation);
        } else {
            return !countryNameField.getText().isEmpty() || !countryInitialMoneyField.getText().isEmpty() ||
                    !countryInitialPopulationField.getText().isEmpty();
        }
    }

    private void clearCountryFields() {
        countryNameField.setText("");
        countryInitialMoneyField.setText("");
        countryInitialPopulationField.setText("");

        countryNameErrorLabel.setText("");
        countryInitialMoneyErrorLabel.setText("");
        countryInitialPopulationErrorLabel.setText("");
    }

    private void changeResourceNodeButtonVisibility(boolean editingMode) {
        isResourceNodeEditingMode = editingMode;

        if (editingMode) {
            resourceNodeItemsLabel.setText("Modify Resource Node");
        } else {
            resourceNodeItemsLabel.setText("Insert Resource Node");
        }

        addResourceNodeButton.setVisible(!editingMode);
        addResourceNodeButton.setManaged(!editingMode);

        saveResourceNodeButton.setVisible(editingMode);
        saveResourceNodeButton.setManaged(editingMode);

        resourceNodeOptionalRegion.setVisible(editingMode);
        resourceNodeOptionalRegion.setManaged(editingMode);

        deleteResourceNodeButton.setVisible(editingMode);
        deleteResourceNodeButton.setManaged(editingMode);
    }

    private boolean resolveIsResourceNodeNotSaved() {
        if (isResourceNodeEditingMode) {
            int selectedIndex = resourceNodeListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex != -1) {
                ResourceNodeDTO currentResourceNode = countryResourceNodes.get(selectedCountry).get(selectedResource);
                if (currentResourceNode != null) {
                    int tier = Integer.parseInt(resourceNodeTierField.getText());
                    int baseCapacity = Integer.parseInt(resourceNodeBaseCapacityField.getText());
                    double productionCost = Double.parseDouble(resourceNodeProductionCostField.getText());

                    if (currentResourceNode.tier() != tier ||
                            currentResourceNode.baseCapacity() != baseCapacity ||
                            currentResourceNode.productionCost() != productionCost) {
                        return true;
                    }
                }
            }
        } else {
            if (!resourceNodeTierField.getText().isEmpty() ||
                    !resourceNodeBaseCapacityField.getText().isEmpty() ||
                    !resourceNodeProductionCostField.getText().isEmpty() ||
                    resourceComboBox.getSelectionModel().getSelectedItem() != null) {
                return true;
            }
        }
        return false;
    }

    private void clearResourceNodeFields() {
        resourceNodeTierField.setText("");
        resourceNodeBaseCapacityField.setText("");
        resourceNodeProductionCostField.setText("");
        resourceComboBox.getSelectionModel().clearSelection();
        selectedResource = null;

        resourceNodeTierErrorLabel.setText("");
        resourceNodeBaseCapacityErrorLabel.setText("");
        resourceNodeProductionCostErrorLabel.setText("");
    }
}