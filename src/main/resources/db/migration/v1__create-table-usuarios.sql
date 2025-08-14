CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    fecha_registro DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id),
    INDEX idx_usuarios_email (email),
    INDEX idx_usuarios_activo (activo),
    INDEX idx_usuarios_fecha_registro (fecha_registro)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;