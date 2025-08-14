CREATE TABLE topicos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    mensaje TEXT NOT NULL,
    fecha_creacion DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    fecha_actualizacion DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    status ENUM('ACTIVO', 'CERRADO', 'SOLUCIONADO') NOT NULL DEFAULT 'ACTIVO',
    autor_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,

    PRIMARY KEY (id),

    -- Claves foráneas
    CONSTRAINT fk_topicos_autor
        FOREIGN KEY (autor_id) REFERENCES usuarios(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT fk_topicos_curso
        FOREIGN KEY (curso_id) REFERENCES cursos(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    -- Índices para optimizar consultas
    INDEX idx_topicos_autor_id (autor_id),
    INDEX idx_topicos_curso_id (curso_id),
    INDEX idx_topicos_status (status),
    INDEX idx_topicos_fecha_creacion (fecha_creacion),
    INDEX idx_topicos_fecha_actualizacion (fecha_actualizacion),

    -- Índice compuesto para prevenir duplicados (opcional)
    INDEX idx_topicos_titulo_mensaje (titulo(50), mensaje(100))

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;