INSERT INTO rentals (
    customer_id,
    vehicle_id,
    rental_type,
    start_date,
    scheduled_end_date,
    pickup_location,
    base_rate,
    insurance_fee,
    initial_mileage,
    total_amount,
    rental_status,
    payment_status
) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'ACTIVE', 'PENDING');
