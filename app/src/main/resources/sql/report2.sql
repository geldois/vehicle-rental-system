SELECT
    r.rental_id,
    TRIM(
        CONCAT(
            COALESCE(CONCAT(c.first_name, ' ', c.last_name), ''),
            CASE
            WHEN c.first_name IS NOT NULL THEN ' '
            ELSE ''
            END,
            '(',
            c.email,
            ')'
        )
    ) AS customer_display,
    CONCAT(v.make, ' ', v.model) AS vehicle,
    vt.type_name AS vehicle_type,
    DATE_FORMAT(r.start_date, '%Y-%m-%d') AS start_date,
    r.total_amount,
    r.rental_status,
    r.payment_status
FROM rentals r
JOIN customers c ON r.customer_id = c.customer_id
JOIN vehicles v ON r.vehicle_id = v.vehicle_id
JOIN vehicle_types vt ON v.type_id = vt.type_id
ORDER BY r.start_date DESC
LIMIT 100;
