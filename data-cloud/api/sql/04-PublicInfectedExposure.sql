CREATE TABLE IF NOT EXISTS PublicInfectedExposure (
    id                  INT PRIMARY KEY generated always as identity,
    location_identifier VARCHAR(64),
    exposure_hash       VARCHAR(32),
    temperature         FLOAT,
    humidity            FLOAT,
    "date"              TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
