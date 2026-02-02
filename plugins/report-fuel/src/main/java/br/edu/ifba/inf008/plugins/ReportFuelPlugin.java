package br.edu.ifba.inf008.plugins;

import javafx.application.Platform;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

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
        chart.getData().addAll(
            new PieChart.Data("GASOLINE", 12),
            new PieChart.Data("DIESEL", 5),
            new PieChart.Data("ELECTRIC", 3),
            new PieChart.Data("FLEX", 7)
        );

        VBox root = new VBox(10);
        root.getChildren().addAll(title, chart);

        ui.createTab("Fuel Report", root);
    }
}
