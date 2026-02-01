package br.edu.ifba.inf008.interfaces.domain;

public interface IVehicleTypeRegistry {
    void register(IVehicleType type);
    
    IVehicleType getByName(String name);
}

