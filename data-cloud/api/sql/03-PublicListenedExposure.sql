CREATE TABLE IF NOT EXISTS public_listened_exposure (
    location_identifier VARCHAR(64),
    exposure_hash       VARCHAR(32) PRIMARY KEY,
    temperature         FLOAT,
    humidity            FLOAT,
    "date"              TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
