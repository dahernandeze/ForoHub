CREATE TABLE cursos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    categoria VARCHAR(50) NOT NULL,
    fecha_creacion DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id),
    UNIQUE KEY uk_cursos_nombre (nombre),
    INDEX idx_cursos_categoria (categoria),
    INDEX idx_cursos_activo (activo),
    INDEX idx_cursos_fecha_creacion (fecha_creacion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;