USE ClaimProcessingDB;
INSERT INTO insurance_claims (
    first_name,
    last_name,
    policy_number,
    claim_type,
    submission_date,
    incident,
    claim_amount
)
VALUES (
    'John',
    'Doe',
    'POL12345',
    'Auto Accident',
    '2025-10-15',
    'Rear-end collision on highway, no injuries reported.',
    3200.50
);