CREATE TABLE IF NOT EXISTS HashesUnderAnalysis (
    aggregator       INT PRIMARY KEY generated always as identity,
    exposure_hash    VARCHAR(32),
    "date"           TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
