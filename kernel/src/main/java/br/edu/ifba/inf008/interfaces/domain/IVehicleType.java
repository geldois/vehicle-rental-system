package br.edu.ifba.inf008.interfaces.domain;

import java.util.Map;

public interface IVehicleType {
    String getTypeName();

    void setAdditionalFees(Map<String, Double> additionalFees);

    double calculateTotal(double dailyRate, long rentalDays);
}
