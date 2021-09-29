CREATE TABLE IF NOT EXISTS infected_hashes (
    exposure_hash       VARCHAR(32) PRIMARY KEY,
    "date"              TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
