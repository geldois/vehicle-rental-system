package br.edu.ifba.inf008.plugins;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.sql.Date;

import br.edu.ifba.inf008.db.DatabaseConnection;
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

        ComboBox<String> customerCombo = new ComboBox<>();
        customerCombo.setPromptText("Select customer email");
        customerCombo.setItems(loadClients());

        ComboBox<String> vehicleTypeCombo = new ComboBox<>();
        vehicleTypeCombo.getItems().addAll(
                "ECONOMY", "COMPACT", "SUV", "LUXURY", "VAN", "ELECTRIC"
        );

        TableView<VehicleRow> vehicleTable = new TableView<>();
        vehicleTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn<VehicleRow, String> makeCol = new TableColumn<>("Make");
        makeCol.setCellValueFactory(new PropertyValueFactory<>("make"));

        TableColumn<VehicleRow, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<VehicleRow, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<VehicleRow, String> fuelCol = new TableColumn<>("Fuel");
        fuelCol.setCellValueFactory(new PropertyValueFactory<>("fuelType"));

        TableColumn<VehicleRow, String> transmissionCol = new TableColumn<>("Transmission");
        transmissionCol.setCellValueFactory(new PropertyValueFactory<>("transmission"));

        TableColumn<VehicleRow, Double> mileageCol = new TableColumn<>("Mileage");
        mileageCol.setCellValueFactory(new PropertyValueFactory<>("mileage"));

        vehicleTable.getColumns().addAll(
                makeCol, modelCol, yearCol, fuelCol, transmissionCol, mileageCol
        );

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start date");

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End date");

        TextField pickupLocationField = new TextField();
        pickupLocationField.setPromptText("Pickup location");

        TextField baseRateField = new TextField();
        baseRateField.setPromptText("Base rate");

        TextField insuranceFeeField = new TextField();
        insuranceFeeField.setPromptText("Insurance fee");

        Label totalLabel = new Label("Total: -");

        Button confirmButton = new Button("Confirm Rental");

        Runnable recalcTotal = () -> {
            try {
                LocalDate start = startDatePicker.getValue();
                LocalDate end = endDatePicker.getValue();

                if (start == null || end == null || end.isBefore(start)) {
                    totalLabel.setText("Total: -");
                    return;
                }

                long days = ChronoUnit.DAYS.between(start, end);
                if (days == 0) days = 1;

                double baseRate = Double.parseDouble(baseRateField.getText());
                double insurance = Double.parseDouble(insuranceFeeField.getText());

                String typeName = vehicleTypeCombo.getValue();
                if (typeName == null) {
                    totalLabel.setText("Total: -");
                    return;
                }

                IVehicleType type = registry.getByName(typeName);
                if (type == null) {
                    totalLabel.setText("Total: -");
                    return;
                }

                double subtotal = type.calculateTotal(baseRate, days);
                double total = subtotal + insurance;

                totalLabel.setText(String.format("Total: %.2f", total));

            } catch (Exception exception) {
                totalLabel.setText("Total: -");
            }
        };

        vehicleTypeCombo.setOnAction(event -> {
            String selectedType = vehicleTypeCombo.getValue();
            if (selectedType != null) {
                vehicleTable.setItems(loadAvailableVehicles(selectedType));
                recalcTotal.run();
            }
        });

        vehicleTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSel, newSel) -> {
                if (newSel != null) {
                    recalcTotal.run();
                }
            }
        );

        confirmButton.setOnAction(e -> {
            String customerEmail = customerCombo.getValue();
            VehicleRow selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();

            if (customerEmail == null || selectedVehicle == null) {
                showAlert("Error", "Select a customer and a vehicle.");
                return;
            }

            if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
                showAlert("Error", "Select start and end dates.");
                return;
            }

            if (pickupLocationField.getText().isBlank()) {
                showAlert("Error", "Enter pickup location.");
                return;
            }

            if (baseRateField.getText().isBlank() || insuranceFeeField.getText().isBlank()) {
                showAlert("Error", "Enter base rate and insurance fee.");
                return;
            }

            long customerId = getCustomerIdByEmail(customerEmail);
            long vehicleId = selectedVehicle.getVehicleId();

            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            long days = ChronoUnit.DAYS.between(startDate, endDate);
            if (days == 0) days = 1;

            double baseRate = Double.parseDouble(baseRateField.getText());
            double insurance = Double.parseDouble(insuranceFeeField.getText());

            String rentalType = "DAILY";

            try (Connection conn = DatabaseConnection.getConnection()) {
                PreparedStatement insert = conn.prepareStatement(loadSQL("/sql/insert_rental.sql"));
                insert.setLong(1, customerId);
                insert.setLong(2, vehicleId);
                insert.setString(3, rentalType);
                insert.setDate(4, Date.valueOf(startDate));
                insert.setDate(5, Date.valueOf(endDate));
                insert.setString(6, pickupLocationField.getText());
                insert.setDouble(7, baseRate);
                insert.setDouble(8, insurance);
                insert.setDouble(9, selectedVehicle.getMileage());
                insert.setDouble(10, extractTotalFromLabel(totalLabel));

                insert.executeUpdate();

                PreparedStatement update = conn.prepareStatement(
                    "UPDATE vehicles SET status = 'RENTED' WHERE vehicle_id = ?"
                );
                update.setLong(1, vehicleId);
                update.executeUpdate();

                vehicleTable.getItems().remove(selectedVehicle);
                showAlert("Success", "Rental confirmed.");

            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Database error", "Failed to confirm rental.");
            }
        });

        startDatePicker.setOnAction(e -> recalcTotal.run());
        endDatePicker.setOnAction(e -> recalcTotal.run());
        baseRateField.setOnKeyReleased(e -> recalcTotal.run());
        insuranceFeeField.setOnKeyReleased(e -> recalcTotal.run());

        root.getChildren().addAll(
            title,
            customerCombo,
            vehicleTypeCombo,
            vehicleTable,
            startDatePicker,
            endDatePicker,
            pickupLocationField,
            baseRateField,
            insuranceFeeField,
            totalLabel
        );

        root.getChildren().add(confirmButton);

        ui.createTab("Rental", root);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private long getCustomerIdByEmail(String email) {
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT customer_id FROM customers WHERE email = ?"
            )
        ) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("customer_id");
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        throw new RuntimeException("Customer not found: " + email);
    }

    private double extractTotalFromLabel(Label totalLabel) {
        try {
            String text = totalLabel.getText().replace("Total:", "").trim();
            return Double.parseDouble(text);
        } catch (Exception e) {
            throw new RuntimeException("Invalid total value");
        }
    }

    private ObservableList<String> loadClients() {
        ObservableList<String> clients = FXCollections.observableArrayList();

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                loadSQL("/sql/list_clients.sql")
            );
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                clients.add(rs.getString("email"));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return clients;
    }

    private ObservableList<VehicleRow> loadAvailableVehicles(String typeName) {
        ObservableList<VehicleRow> vehicles = FXCollections.observableArrayList();

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                loadSQL("/sql/list_available_vehicles.sql")
            )
        ) {
            stmt.setString(1, typeName);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vehicles.add(new VehicleRow(
                            rs.getInt("vehicle_id"),
                            rs.getString("make"),
                            rs.getString("model"),
                            rs.getInt("year"),
                            rs.getString("fuel_type"),
                            rs.getString("transmission"),
                            rs.getDouble("mileage")
                    ));
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return vehicles;
    }

    private String loadSQL(String path) {
        try (var is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                throw new RuntimeException("SQL file not found: " + path);
            }
            return new String(is.readAllBytes());
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static class VehicleRow {

        private final int vehicleId;
        private final String make;
        private final String model;
        private final int year;
        private final String fuelType;
        private final String transmission;
        private final double mileage;

        public VehicleRow(int vehicleId, String make, String model, int year,
                        String fuelType, String transmission, double mileage) {
            this.vehicleId = vehicleId;
            this.make = make;
            this.model = model;
            this.year = year;
            this.fuelType = fuelType;
            this.transmission = transmission;
            this.mileage = mileage;
        }

        public int getVehicleId() { return vehicleId; }
        public String getMake() { return make; }
        public String getModel() { return model; }
        public int getYear() { return year; }
        public String getFuelType() { return fuelType; }
        public String getTransmission() { return transmission; }
        public double getMileage() { return mileage; }
    }
}
