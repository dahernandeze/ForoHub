-- ===================================================================
-- SCRIPTS DE CREACIÓN DE TABLAS - FORO HUB
-- Base de datos: MySQL 8.0+
-- ===================================================================

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS forohub_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE forohub_db;

-- ===================================================================
-- TABLA: usuarios
-- ===================================================================
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

-- ===================================================================
-- TABLA: cursos
-- ===================================================================
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

-- ===================================================================
-- TABLA: topicos
-- ===================================================================
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

-- ===================================================================
-- DATOS DE EJEMPLO (OPCIONAL)
-- ===================================================================

-- Insertar usuarios de ejemplo
INSERT INTO usuarios (nombre, email, password, activo) VALUES 
('Juan Pérez', 'juan@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye', TRUE),
('María García', 'maria@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye', TRUE),
('Carlos López', 'carlos@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye', TRUE),
('Ana Martínez', 'ana@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye', TRUE);

-- Insertar cursos de ejemplo
INSERT INTO cursos (nombre, descripcion, categoria, activo) VALUES 
('Spring Boot Básico', 'Curso introductorio de Spring Boot para principiantes', 'Backend', TRUE),
('Java Avanzado', 'Conceptos avanzados de Java: streams, lambdas, concurrencia', 'Backend', TRUE),
('React Fundamentals', 'Fundamentos de React para desarrollo frontend', 'Frontend', TRUE),
('MySQL Database Design', 'Diseño y optimización de bases de datos MySQL', 'Database', TRUE),
('Spring Security', 'Seguridad en aplicaciones Spring Boot', 'Backend', TRUE),
('HTML y CSS', 'Fundamentos de maquetación web', 'Frontend', TRUE);

-- Insertar tópicos de ejemplo
INSERT INTO topicos (titulo, mensaje, autor_id, curso_id, status) VALUES 
(
    '¿Cómo configurar Spring Security con JWT?',
    'Estoy intentando implementar autenticación JWT en mi aplicación Spring Boot pero tengo problemas con la configuración. ¿Alguien puede ayudarme con un ejemplo básico?',
    1, 5, 'ACTIVO'
),
(
    'Error al conectar con base de datos MySQL',
    'Mi aplicación no puede conectarse a la base de datos MySQL. He verificado las credenciales y la URL de conexión pero sigue fallando. El error que obtengo es: Communications link failure.',
    2, 4, 'ACTIVO'
),
(
    'Problema con Streams en Java',
    'No entiendo bien cómo usar los Streams de Java 8. Específicamente, necesito filtrar y transformar una lista de objetos. ¿Pueden mostrarme algunos ejemplos prácticos?',
    3, 2, 'SOLUCIONADO'
),
(
    'React Hooks - useState vs useEffect',
    'Soy nuevo en React y me confunden los hooks. ¿Cuándo debo usar useState y cuándo useEffect? ¿Pueden explicarme la diferencia con ejemplos?',
    4, 3, 'ACTIVO'
),
(
    'Optimización de consultas SQL',
    'Tengo una consulta SQL que tarda mucho en ejecutarse. La tabla tiene más de 100,000 registros. ¿Qué estrategias puedo usar para optimizarla?',
    1, 4, 'CERRADO'
);

-- ===================================================================
-- CONSULTAS ÚTILES PARA VERIFICAR LOS DATOS
-- ===================================================================

-- Verificar que las tablas se crearon correctamente
SHOW TABLES;

-- Verificar la estructura de las tablas
DESCRIBE usuarios;
DESCRIBE cursos;  
DESCRIBE topicos;

-- Contar registros en cada tabla
SELECT 'usuarios' as tabla, COUNT(*) as total FROM usuarios
UNION ALL
SELECT 'cursos' as tabla, COUNT(*) as total FROM cursos  
UNION ALL
SELECT 'topicos' as tabla, COUNT(*) as total FROM topicos;

-- Consulta completa de tópicos con información relacionada
SELECT 
    t.id,
    t.titulo,
    t.mensaje,
    t.status,
    t.fecha_creacion,
    u.nombre as autor,
    c.nombre as curso
FROM topicos t
INNER JOIN usuarios u ON t.autor_id = u.id
INNER JOIN cursos c ON t.curso_id = c.id
ORDER BY t.fecha_creacion DESC;

-- ===================================================================
-- CONFIGURACIÓN ADICIONAL (OPCIONAL)
-- ===================================================================

-- Crear usuario específico para la aplicación (recomendado para producción)
CREATE USER IF NOT EXISTS 'forohub_user'@'localhost' IDENTIFIED BY 'forohub_password';
GRANT SELECT, INSERT, UPDATE, DELETE ON forohub_db.* TO 'forohub_user'@'localhost';
FLUSH PRIVILEGES;

-- ===================================================================
-- SCRIPT DE LIMPIEZA (SOLO PARA DESARROLLO)
-- ===================================================================

/*
-- CUIDADO: Esto eliminará todos los datos y tablas
-- Solo ejecutar en entorno de desarrollo

DROP TABLE IF EXISTS topicos;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS cursos;
DROP DATABASE IF EXISTS forohub_db;
*/