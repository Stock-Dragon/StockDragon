CREATE TABLE fs_resolved_instrument (
    id                 UUID         PRIMARY KEY,
    created_at         TIMESTAMP    NOT NULL,
    updated_at         TIMESTAMP    NOT NULL,
    isin               VARCHAR(12)  NOT NULL,
    outcome            VARCHAR(32)  NOT NULL,
    ticker             VARCHAR(32),
    cik                BIGINT,
    resolution_source  VARCHAR(64)  NOT NULL,
    details            JSONB
);

CREATE INDEX idx_fs_resolved_isin ON fs_resolved_instrument (isin, created_at DESC);

CREATE TABLE fs_raw_payload (
    id            UUID         PRIMARY KEY,
    created_at    TIMESTAMP    NOT NULL,
    updated_at    TIMESTAMP    NOT NULL,
    source        VARCHAR(32)  NOT NULL,
    source_key    VARCHAR(64)  NOT NULL,
    endpoint      VARCHAR(64)  NOT NULL,
    http_status   INT          NOT NULL,
    content_hash  VARCHAR(64)  NOT NULL,
    payload       TEXT         NOT NULL
);

CREATE INDEX idx_fs_raw_lookup ON fs_raw_payload (source, source_key, endpoint, created_at DESC);