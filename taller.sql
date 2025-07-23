-- Borra la base de datos si existe para empezar de cero.
DROP DATABASE IF EXISTS taller;

-- Crea la base de datos con la codificación correcta.
CREATE DATABASE taller CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Selecciona la base de datos para usarla.
USE taller;

-- Tabla de Categorías (estandarizada)
CREATE TABLE categorias (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    descripcion TEXT,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre)
);

-- Tabla de Proveedores (estandarizada)
CREATE TABLE proveedores (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion TEXT,
    creado_en DATETIME DEFAULT CURRENT_TIMESTAMP,
    actualizado_en DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    eliminado_en DATETIME,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre),
    UNIQUE KEY (email)
);

-- Tabla de Repuestos (estandarizada)
CREATE TABLE repuestos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    ubicacion VARCHAR(100),
    stock INT DEFAULT 0,
    stock_minimo INT DEFAULT 5,
    precio DECIMAL(10, 2) NOT NULL,
    categoria_id BIGINT NOT NULL,
    proveedor_id BIGINT NOT NULL,
    creado_en DATETIME DEFAULT CURRENT_TIMESTAMP,
    actualizado_en DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    eliminado_en DATETIME,
    PRIMARY KEY (id),
    FOREIGN KEY (categoria_id) REFERENCES categorias (id),
    FOREIGN KEY (proveedor_id) REFERENCES proveedores (id)
);

-- Tabla de Clientes (estandarizada)
CREATE TABLE clientes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion TEXT,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    eliminado_en DATETIME,
    PRIMARY KEY (id),
    UNIQUE KEY (email)
);

-- Tabla de Vehículos (estandarizada)
CREATE TABLE vehiculos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    anio VARCHAR(4),
    matricula VARCHAR(20) NOT NULL,
    cliente_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (matricula),
    FOREIGN KEY (cliente_id) REFERENCES clientes (id) ON DELETE CASCADE
);

-- Tabla de Usuarios (estandarizada)
CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT, -- Usamos BIGINT para consistencia
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL, -- Nombre de columna estándar
    rol ENUM(
        'ADMIN',
        'ALMACEN',
        'MECANICO'
    ) NOT NULL,
    creado_en DATETIME DEFAULT CURRENT_TIMESTAMP,
    actualizado_en DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    eliminado_en DATETIME,
    PRIMARY KEY (id),
    UNIQUE KEY (email)
);

-- Tabla de Órdenes de Trabajo (estandarizada)
CREATE TABLE ordenes_trabajo (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cliente_id BIGINT NOT NULL,
    vehiculo_id BIGINT NOT NULL,
    descripcion_problema TEXT NOT NULL,
    estado ENUM(
        'PENDIENTE',
        'EN_PROCESO',
        'COMPLETADA',
        'CANCELADA'
    ) NOT NULL DEFAULT 'PENDIENTE',
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_completado DATETIME,
    PRIMARY KEY (id),
    FOREIGN KEY (cliente_id) REFERENCES clientes (id),
    FOREIGN KEY (vehiculo_id) REFERENCES vehiculos (id)
);

-- Tabla de Repuestos Utilizados (estandarizada)
CREATE TABLE repuestos_utilizados (
    id BIGINT NOT NULL AUTO_INCREMENT,
    orden_id BIGINT NOT NULL,
    repuesto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (orden_id) REFERENCES ordenes_trabajo (id) ON DELETE CASCADE,
    FOREIGN KEY (repuesto_id) REFERENCES repuestos (id)
);

-- Tabla de Movimientos de Inventario (estandarizada)
CREATE TABLE movimientos_inventario (
    id BIGINT NOT NULL AUTO_INCREMENT,
    repuesto_id BIGINT NOT NULL,
    tipo ENUM('ENTRADA', 'SALIDA', 'AJUSTE') NOT NULL,
    cantidad INT NOT NULL,
    fecha_movimiento DATETIME DEFAULT CURRENT_TIMESTAMP,
    cliente_id BIGINT,
    usuario_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (repuesto_id) REFERENCES repuestos (id),
    FOREIGN KEY (cliente_id) REFERENCES clientes (id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
);