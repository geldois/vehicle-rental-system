package br.edu.ifba.inf008.dao.interfaces;

import br.edu.ifba.inf008.model.VehicleTypeEntity;
import java.util.Map;

public interface IVehicleTypeDAO {

    VehicleTypeEntity findByName(String typeName);
    
    class VehicleTypeData {
        public String typeName;
        public double dailyRate;
        public double insuranceRate;
        public Map<String, Double> additionalFees;
    }
}
