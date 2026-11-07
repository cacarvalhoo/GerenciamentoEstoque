CREATE TABLE IF NOT EXISTS produto (
    codigo    INTEGER PRIMARY KEY AUTOINCREMENT,
    nome      TEXT    NOT NULL,
    quantidade INTEGER NOT NULL CHECK(quantidade >= 0)
);