package br.edu.ifba.inf008.plugins;

import java.util.Map;

import br.edu.ifba.inf008.interfaces.domain.IVehicleType;

public class SuvVehicleType implements IVehicleType {
    private Map<String, Double> additionalFees;

    @Override
    public String getTypeName() {
        return "SUV";
    }

    @Override
    public void setAdditionalFees(Map<String, Double> additionalFees) {
        this.additionalFees = additionalFees;
    }

    @Override
    public double calculateTotal(double dailyRate, long rentalDays) {
        double total = dailyRate * rentalDays;

        if (additionalFees != null) {
            for (Map.Entry<String, Double> entry : additionalFees.entrySet()) {
                if (entry.getKey().endsWith("_fee")) {
                    total += entry.getValue();
                }
            }
        }

        return total;
    }
}
