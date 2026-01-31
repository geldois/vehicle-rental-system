package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.domain.IVehicleType;
import java.util.Map;

public class EconomicVehicleType implements IVehicleType {

    private Map<String, Double> additionalFees;

    @Override
    public String getTypeName() {
        return "ECONOMY";
    }

    @Override
    public void setAdditionalFees(Map<String, Double> additionalFees) {
        this.additionalFees = additionalFees;
    }

    @Override
    public double calculateTotal(double dailyRate, long rentalDays) {
        double total = dailyRate * rentalDays;

        if (additionalFees != null) {
            for (Map.Entry<String, Double> e : additionalFees.entrySet()) {
                if (e.getKey().endsWith("_fee")) {
                    total += e.getValue();
                }
            }
        }
        return total;
    }
}
