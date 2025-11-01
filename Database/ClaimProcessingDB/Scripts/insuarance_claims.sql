-- Create table for storing claim details
CREATE TABLE insurance_claims (
    claim_id INT IDENTITY(1,1) PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    policy_number VARCHAR(50) NOT NULL,
    claim_type VARCHAR(50) NOT NULL,
    submission_date DATE NOT NULL DEFAULT GETDATE(),
    incident VARCHAR(MAX),
    claim_amount DECIMAL(12,2) CHECK (claim_amount >= 0),
    claim_form VARBINARY(MAX),   -- store PDF binary data
    created_at DATETIME DEFAULT GETDATE()
);
GO
