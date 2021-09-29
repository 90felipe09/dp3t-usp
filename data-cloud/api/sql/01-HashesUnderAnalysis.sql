CREATE TABLE IF NOT EXISTS hashes_under_analysis (
    aggregator       VARCHAR(36),
    exposure_hash    VARCHAR(32) PRIMARY KEY,
    "date"           TIMESTAMPTZ NOT NULL DEFAULT NOW()
);