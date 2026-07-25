-- password admin123

INSERT INTO users (id, email, password_hash, role, created_at, updated_at)
VALUES (
           gen_random_uuid(),
           'admin@stockdragon.com',
           '$2b$10$2NaRxTMT8cerq2fPrxgdPutWRDBCnBE.kDBfoJjYpy.bXD.nHPtDa',
           'ADMIN',
           now(),
           now()
       );

-- password user123

INSERT INTO users (id, email, password_hash, role, created_at, updated_at)
VALUES (
           gen_random_uuid(),
           'user@stockdragon.com',
           '$2b$10$PcXc3CXt7Q7cb.d0fIt9VucL8UpKrX5MPkOsfHWmMvvREXRPs/uMi',
           'USER',
           now(),
           now()
       );