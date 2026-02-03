package br.edu.ifba.inf008.plugins;

import javafx.application.Platform;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
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

public class ReportFuelPlugin implements IPlugin {

    @Override
    public boolean init() {
        Platform.runLater(this::buildUI);
        
        return true;
    }

    private void buildUI() {
        IUIController ui = ICore.getInstance().getUIController();

        Label title = new Label("Vehicles by Fuel Type");

        PieChart chart = new PieChart();
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                loadSQL("/sql/report1.sql")
            );
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                String fuelType = rs.getString(1);
                int total = rs.getInt(2);

                chart.getData().add(
                    new PieChart.Data(fuelType + " (" + total + ")", total)
                );
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        VBox root = new VBox(10);
        root.getChildren().addAll(title, chart);

        ui.createTab("Fuel Report", root);
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
}
