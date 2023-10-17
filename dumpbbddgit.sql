Create schema  `chatpro` ;

-- Crear la tabla Usuarios
CREATE TABLE Usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(255) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    conectado BOOLEAN,
    grupo_id INT
    
);

-- Crear la tabla Grupos
CREATE TABLE Grupos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_grupo VARCHAR(255) UNIQUE NOT NULL,
    administrador_id INT
   
);

-- Crear la tabla MiembrosGrupos (Tabla intermedia para la relación muchos a muchos entre Usuarios y Grupos)
CREATE TABLE MiembrosGrupos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,
    grupo_id INT
    
);

-- Crear la tabla Mensajes
CREATE TABLE Mensajes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    contenido TEXT,
    usuario_id INT,
    grupo_id INT,
    fecha_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    
);

-- Crear la tabla Archivos
CREATE TABLE Archivos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_archivo VARCHAR(255),
    ruta_archivo VARCHAR(255),
    tipo ENUM('publico', 'privado', 'protegido'),
    usuario_id INT,
    grupo_id INT
    
);

-- Crear la tabla ConfiguracionServidor
CREATE TABLE ConfiguracionServidor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tamano_maximo INT,
    max_conexiones_simultaneas INT,
    contrasena_bd VARCHAR(255),
    cliente_admin VARCHAR(255),
    nombre_servidor VARCHAR(255),
    ruta_archivos VARCHAR(255)
);

-- Crear la tabla ConfiguracionCliente
CREATE TABLE ConfiguracionCliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_cliente VARCHAR(255),
    tamano_maximo INT,
    ip_servidor_predeterminada VARCHAR(255),
    puerto_servidor_predeterminado INT
);

ALTER TABLE Usuarios ADD FOREIGN KEY (grupo_id) REFERENCES Grupos(id);
ALTER TABLE Grupos ADD FOREIGN KEY (administrador_id) REFERENCES Usuarios(id);
ALTER TABLE MiembrosGrupos ADD FOREIGN KEY (usuario_id) REFERENCES Usuarios(id);
ALTER TABLE MiembrosGrupos ADD FOREIGN KEY (grupo_id) REFERENCES Grupos(id);
ALTER TABLE Mensajes ADD FOREIGN KEY (usuario_id) REFERENCES Usuarios(id);
ALTER TABLE Mensajes ADD FOREIGN KEY (grupo_id) REFERENCES Grupos(id);
ALTER TABLE Archivos ADD FOREIGN KEY (usuario_id) REFERENCES Usuarios(id);
ALTER TABLE Archivos ADD FOREIGN KEY (grupo_id) REFERENCES Grupos(id);
ALTER TABLE MiembrosGrupos
DROP COLUMN id; 

ALTER TABLE MiembrosGrupos
ADD COLUMN rol ENUM('admin', 'miembro') NOT NULL DEFAULT 'miembro';