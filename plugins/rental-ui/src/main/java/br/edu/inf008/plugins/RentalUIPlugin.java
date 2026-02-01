package br.edu.ifba.inf008.plugins;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.interfaces.domain.IVehicleType;
import br.edu.ifba.inf008.interfaces.domain.IVehicleTypeRegistry;

public class RentalUIPlugin implements IPlugin {

    @Override
    public boolean init() {

        Platform.runLater(this::buildUI);

        return true;
    }

    private void buildUI() {
        IUIController ui = ICore.getInstance().getUIController();
        IVehicleTypeRegistry registry = ICore.getInstance().getVehicleTypeRegistry();

        VBox root = new VBox(10);

        Label title = new Label("New Rental");

        ComboBox<String> vehicleTypeCombo = new ComboBox<>();
        vehicleTypeCombo.getItems().addAll(
                "ECONOMY", "COMPACT", "SUV", "LUXURY", "VAN", "ELECTRIC"
        );

        TextField dailyRateField = new TextField();
        dailyRateField.setPromptText("Daily rate");

        TextField daysField = new TextField();
        daysField.setPromptText("Number of days");

        Label totalLabel = new Label("Total: -");

        Button calculate = new Button("Calculate total");

        calculate.setOnAction(e -> {
            String typeName = vehicleTypeCombo.getValue();

            if (typeName == null) {
                totalLabel.setText("Select a vehicle type");
                return;
            }

            IVehicleType type = registry.getByName(typeName);
            if (type == null) {
                totalLabel.setText("Vehicle type not registered");
                return;
            }

            try {
                double dailyRate = Double.parseDouble(dailyRateField.getText());
                long days = Long.parseLong(daysField.getText());

                double total = type.calculateTotal(dailyRate, days);
                totalLabel.setText("Total: " + total);

            } catch (NumberFormatException ex) {
                totalLabel.setText("Invalid values");
            }
        });

        root.getChildren().addAll(
                title,
                vehicleTypeCombo,
                dailyRateField,
                daysField,
                calculate,
                totalLabel
        );

        ui.createTab("Rental", root);
    }
}
