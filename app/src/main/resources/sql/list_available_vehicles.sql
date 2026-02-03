SELECT
    v.vehicle_id,
    v.make,
    v.model,
    v.year,
    v.fuel_type,
    v.transmission,
    v.mileage
    FROM vehicles v
    JOIN vehicle_types vt ON vt.type_id = v.type_id
    WHERE vt.type_name = ? AND v.status = 'AVAILABLE'
    ORDER BY v.make, v.model;
