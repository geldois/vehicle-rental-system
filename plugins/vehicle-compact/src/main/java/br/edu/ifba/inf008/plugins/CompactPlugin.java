package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.domain.IVehicleTypeRegistry;

public class CompactPlugin implements IPlugin {

    @Override
    public boolean init() {
        IVehicleTypeRegistry registry = ICore.getInstance().getVehicleTypeRegistry();

        registry.register(new CompactVehicleType());

        return true;
    }

}
