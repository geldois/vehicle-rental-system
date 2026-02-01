package br.edu.ifba.inf008.plugins;

import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.shell.Core;

public class RentalUIPlugin implements IPlugin {
    
    @Override
    public void init() {
        IUIController ui = Core.getInstance().getUIController();

        Label label = new Label("Rental UI loaded successfully!");
        VBox content = new VBox(label);

        ui.createTab("Rental", content);

        MenuItem menuItem = ui.createMenuItem("Rental", "New Rental");

        menuItem.setOnAction(e -> {
            label.setText("New Rental clicked!");
            System.out.println("Rental menu clicked");
        });
    }
}
