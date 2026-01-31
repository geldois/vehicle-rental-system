package br.edu.ifba.inf008.dao.persistence.memory;

import br.edu.ifba.inf008.dao.interfaces.IVehicleTypeDAO;
import br.edu.ifba.inf008.model.VehicleTypeEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class VehicleTypeDAOMemory implements IVehicleTypeDAO {

    private final Connection connection;

    public VehicleTypeDAOMemory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public VehicleTypeEntity findByName(String typeName) {
        String sql = 
            "SELECT type_name, daily_rate, additional_fees " +
            "FROM vehicle_types " +
            "WHERE type_name = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, typeName);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return null;
            }

            VehicleTypeEntity entity = new VehicleTypeEntity();
            entity.setTypeName(rs.getString("type_name"));
            entity.setDailyRate(rs.getDouble("daily_rate"));

            // JSON -> Map (simplificado)
            String jsonFees = rs.getString("additional_fees");
            entity.setAdditionalFees(parseFees(jsonFees));

            return entity;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar VehicleType", e);
        }
    }

    // Conversão simples (ex: {"insurance_fee":15.0})
    private Map<String, Double> parseFees(String json) {
        Map<String, Double> map = new HashMap<>();

        if (json == null || json.isBlank()) {
            return map;
        }

        json = json.replace("{", "")
                   .replace("}", "")
                   .replace("\"", "");

        String[] entries = json.split(",");

        for (String entry : entries) {
            String[] kv = entry.split(":");
            map.put(kv[0].trim(), Double.parseDouble(kv[1].trim()));
        }

        return map;
    }
}
