package br.edu.ifba.inf008.plugins;

import javafx.scene.layout.VBox;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;

public class RentalUIPlugin implements IPlugin {
    
    @Override
    public boolean init() {
        IUIController ui = ICore.getInstance().getUIController();

        VBox root = new VBox();
        ui.createTab("Rental", root);

        return true;
    }
}
