package br.edu.ifba.inf008.shell;

import br.edu.ifba.inf008.app.VehicleTypeRegistry;
import br.edu.ifba.inf008.interfaces.*;
import br.edu.ifba.inf008.interfaces.domain.IVehicleTypeRegistry;
import javafx.application.Application;
import javafx.application.Platform;

public class Core extends ICore
{
    private Core() {}

    public static boolean init() {
	if (instance != null) {
	    System.out.println("Fatal error: core is already initialized!");
	    System.exit(-1);
	}

    instance = new Core();
    instance.getPluginController().init();

    UIController.launch(UIController.class);

    return true;
    }
    /*
	instance = new Core();
        UIController.launch(UIController.class);

        return true;
    }

    */
    public IUIController getUIController() {
        return UIController.getInstance();
    }
    public IAuthenticationController getAuthenticationController() {
        return authenticationController;
    }
    public IIOController getIOController() {
        return ioController;
    }
    public IPluginController getPluginController() {
        return pluginController;
    }

    private IAuthenticationController authenticationController = new AuthenticationController();
    private IIOController ioController = new IOController();
    private IPluginController pluginController = new PluginController();

    // Alterações

    private IVehicleTypeRegistry vehicleTypeRegistry = VehicleTypeRegistry.getInstance();

    @Override
    public IVehicleTypeRegistry getVehicleTypeRegistry() {
        return vehicleTypeRegistry;
    }

    // alt
    public static void main(String[] args) {
        Core.init();
    }


}
