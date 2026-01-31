package br.edu.ifba.inf008.app;

import br.edu.ifba.inf008.interfaces.domain.*;
import java.util.HashMap;
import java.util.Map;

public class VehicleTypeRegistry implements IVehicleTypeRegistry{

    private static VehicleTypeRegistry instance;
    private Map<String, IVehicleType> types = new HashMap<>();

    private VehicleTypeRegistry() {}

    public static VehicleTypeRegistry getInstance() {
        if (instance == null) {
            instance = new VehicleTypeRegistry();
        }
        return instance;
    }

    public void register(IVehicleType type) {
        types.put(type.getTypeName(), type);
    }

    public IVehicleType getByName(String name) {
        return types.get(name);
    }
}
