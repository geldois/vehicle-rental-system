package br.edu.ifba.inf008.plugins;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;

public class ReportRentalsPlugin implements IPlugin {

    @Override
    public boolean init() {
        Platform.runLater(this::buildUI);
        return true;
    }

    private void buildUI() {
        IUIController ui = ICore.getInstance().getUIController();

        Label title = new Label("Rentals Report");

        TableView<RentalRow> table = new TableView<>();

        TableColumn<RentalRow, String> clientCol =
                new TableColumn<>("Client");
        clientCol.setCellValueFactory(
                new PropertyValueFactory<>("client")
        );

        TableColumn<RentalRow, String> vehicleCol =
                new TableColumn<>("Vehicle");
        vehicleCol.setCellValueFactory(
                new PropertyValueFactory<>("vehicle")
        );

        TableColumn<RentalRow, String> startCol =
                new TableColumn<>("Start Date");
        startCol.setCellValueFactory(
                new PropertyValueFactory<>("startDate")
        );

        TableColumn<RentalRow, String> endCol =
                new TableColumn<>("End Date");
        endCol.setCellValueFactory(
                new PropertyValueFactory<>("endDate")
        );

        TableColumn<RentalRow, Double> totalCol =
                new TableColumn<>("Total");
        totalCol.setCellValueFactory(
                new PropertyValueFactory<>("total")
        );

        table.getColumns().addAll(
                clientCol, vehicleCol, startCol, endCol, totalCol
        );

        table.setItems(mockData());

        VBox root = new VBox(10);
        root.getChildren().addAll(title, table);

        ui.createTab("Rentals Report", root);
    }

    private ObservableList<RentalRow> mockData() {
        return FXCollections.observableArrayList(
                new RentalRow("alice@email.com", "ECONOMY", "2026-01-10", "2026-01-15", 550.0),
                new RentalRow("bob@email.com", "SUV", "2026-01-12", "2026-01-14", 780.0),
                new RentalRow("carol@email.com", "ELECTRIC", "2026-01-20", "2026-01-25", 1200.0)
        );
    }

    public static class RentalRow {
        private final String client;
        private final String vehicle;
        private final String startDate;
        private final String endDate;
        private final Double total;

        public RentalRow(String client, String vehicle,
                         String startDate, String endDate, Double total) {
            this.client = client;
            this.vehicle = vehicle;
            this.startDate = startDate;
            this.endDate = endDate;
            this.total = total;
        }

        public String getClient() {
            return client;
        }

        public String getVehicle() {
            return vehicle;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public Double getTotal() {
            return total;
        }
    }
}
