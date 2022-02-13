CREATE TABLE `rol` (
    `idRol` INT(11) NOT NULL AUTO_INCREMENT,
    `nombre` VARCHAR(20),
    PRIMARY KEY (`idRol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `usuario` (
    `idUsuario` INT(11) NOT NULL AUTO_INCREMENT,
    `nombre` VARCHAR(1024) DEFAULT NULL,
    `apellido` VARCHAR(1024) DEFAULT NULL,
    `correo` VARCHAR(50) DEFAULT NULL,
    `password` VARCHAR(60) DEFAULT NULL,
    `idRol` INT(11) NOT NULL,
    CONSTRAINT `fk_Usuario_1` FOREIGN KEY (`idRol`) REFERENCES `rol` (`idRol`) ON DELETE CASCADE,
    PRIMARY KEY (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `solucion` (
    `idSolucion` INT NOT NULL AUTO_INCREMENT,
    `descripcion` VARCHAR(2048) DEFAULT NULL,
    `img_ruta1` VARCHAR(1024) DEFAULT NULL,
    `img_ruta1` VARCHAR(1024) DEFAULT NULL,
    `img_ruta2` VARCHAR(1024) DEFAULT NULL,
    `img_ruta3` VARCHAR(1024) DEFAULT NULL,
    PRIMARY KEY (`idSolucion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `incidencia` (
    `idIncidencia` INT(11) NOT NULL AUTO_INCREMENT,
    `titulo` VARCHAR(100) DEFAULT NULL,
    `descripcion` VARCHAR(2048) DEFAULT NULL,
    `creacion` DATETIME DEFAULT NULL,
    `actualizacion` DATETIME DEFAULT NULL,
    `cierre` DATETIME DEFAULT NULL,
    `estado` INT DEFAULT 0,
    `idSolucion` INT NOT NULL,
    `idUsuario` INT(11) NOT NULL,
    CONSTRAINT `fk_Incidencia_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`) ON DELETE CASCADE,
    CONSTRAINT `fk_Incidencia_2` FOREIGN KEY (`idUsuario`) REFERENCES `solucion` (`idSolucion`) ON DELETE CASCADE,
    PRIMARY KEY (`idIncidencia`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `asignacion` (
    `idAsignacion` INT NOT NULL AUTO_INCREMENT,
    `idIncidencia` INT NOT NULL,
    `idUsuario` INT NOT NULL,
    CONSTRAINT `fk_Asignacion_1` FOREIGN KEY (`idIncidencia`) REFERENCES `incidencia` (`idIncidencia`) ON DELETE CASCADE,
    CONSTRAINT `fk_Asignacion_2` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`) ON DELETE CASCADE,
    PRIMARY KEY (`idAsignacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `historial` (
    `idHistorial` INT NOT NULL AUTO_INCREMENT,
    `idAsignacion` INT NOT NULL,
    `actualizacion` DATETIME DEFAULT NULL,
    `idSolucion` INT NOT NULL,
    CONSTRAINT `fk_Historial_1` FOREIGN KEY (`idAsignacion`) REFERENCES `asignacion` (`idAsignacion`) ON DELETE CASCADE,
    CONSTRAINT `fk_Historial_2` FOREIGN KEY (`idSolucion`) REFERENCES `solucion` (`idSolucion`) ON DELETE CASCADE,
    PRIMARY KEY (`idHistorial`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `rol`(idRol, nombre) VALUES (1, 'administrador');
INSERT INTO `rol`(idRol, nombre) VALUES (2, 'ingeniero');
INSERT INTO `rol`(idRol, nombre) VALUES (3, 'usuario');