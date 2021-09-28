CREATE TABLE IF NOT EXISTS InfectedHashes (
    exposure_hash       VARCHAR(32),
    "date"              TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
