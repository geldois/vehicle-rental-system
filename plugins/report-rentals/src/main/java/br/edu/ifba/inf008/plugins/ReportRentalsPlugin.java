package br.edu.ifba.inf008.plugins;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import br.edu.ifba.inf008.db.DatabaseConnection;
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

        TableColumn<RentalRow, String> customerCol = new TableColumn<>("Customer (Name / Email)");
		customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));

		TableColumn<RentalRow, String> vehicleCol = new TableColumn<>("Vehicle");
		vehicleCol.setCellValueFactory(new PropertyValueFactory<>("vehicle"));

		TableColumn<RentalRow, String> startCol = new TableColumn<>("Start Date");
		startCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));

		TableColumn<RentalRow, Double> totalCol = new TableColumn<>("Total");
		totalCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

		TableColumn<RentalRow, String> rentalStatusCol = new TableColumn<>("Rental Status");
		rentalStatusCol.setCellValueFactory(new PropertyValueFactory<>("rentalStatus"));

		TableColumn<RentalRow, String> paymentStatusCol = new TableColumn<>("Payment Status");
		paymentStatusCol.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));

		table.getColumns().addAll(
			customerCol,
			vehicleCol,
			startCol,
			totalCol,
			rentalStatusCol,
			paymentStatusCol
		);

        ObservableList<RentalRow> data = FXCollections.observableArrayList();

		try (
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(
				loadSQL("/sql/report2.sql")
			);
			ResultSet rs = stmt.executeQuery()
		) {
			while (rs.next()) {
				data.add(new RentalRow(
					rs.getString("customer_display"),
					rs.getString("vehicle"),
					rs.getString("start_date"),
					rs.getDouble("total_amount"),
					rs.getString("rental_status"),
					rs.getString("payment_status")
				));
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		table.setItems(data);

        VBox root = new VBox(10);
        root.getChildren().addAll(title, table);

        ui.createTab("Rentals Report", root);
    }

	private String loadSQL(String path) {
		try (InputStream is = getClass().getResourceAsStream(path)) {
			if (is == null) {
				throw new RuntimeException("SQL file not found: " + path);
			}
			return new String(is.readAllBytes(), StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

    public static class RentalRow {

		private final String customerName;
		private final String vehicle;
		private final String startDate;
		private final Double totalAmount;
		private final String rentalStatus;
		private final String paymentStatus;

		public RentalRow(String customerName,
						String vehicle,
						String startDate,
						Double totalAmount,
						String rentalStatus,
						String paymentStatus) {
			this.customerName = customerName;
			this.vehicle = vehicle;
			this.startDate = startDate;
			this.totalAmount = totalAmount;
			this.rentalStatus = rentalStatus;
			this.paymentStatus = paymentStatus;
		}

		public String getCustomerName() { return customerName; }
		public String getVehicle() { return vehicle; }
		public String getStartDate() { return startDate; }
		public Double getTotalAmount() { return totalAmount; }
		public String getRentalStatus() { return rentalStatus; }
		public String getPaymentStatus() { return paymentStatus; }
	}
}
