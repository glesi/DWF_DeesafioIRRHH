--creando tabla departamento

CREATE TABLE Departamento (
idDepartamento int PRIMARY KEY IDENTITY(1,1) NOT NULL,
nombreDepartamento varchar(50) NOT NULL,
descripcionDepartamento varchar(max));


--creando tabla Cargos

CREATE TABLE Cargos(
idCargo int PRIMARY KEY NOT NULL,
cargo varchar(50) NOT NULL,
descripcionCargo varchar(max),
jefatura BIT
);

--creando tabla TipoContratacion

CREATE TABLE TipoContratacion(
idTipoContratacion INT PRIMARY KEY IDENTITY(1,1) NOT NULL,
tipoContratacion varchar(100));

--creando tabla Empleados

CREATE TABLE Empleados(
idEmpleado int PRIMARY KEY IDENTITY(1,1) NOT NULL,
numeroDui varchar(9) NOT NULL,
nombrePersona varchar(50) NOT NULL,
usuario varchar(50) NOT NULL,
numeroTelefono varchar(9) NOT NULL,
correoInstitucional varchar(50) NOT NULL,
fechaNacimiento date
);
CREATE TABLE Contrataciones (
idContracion int PRIMARY KEY IDENTITY(1,1),
idDepartamento int NOT NULL,
idEmpleado int NOT NULL,
idCargo int NOT NULL,
idTipoContratacion INT NOT NULL,
fechaContratacion DATE,
salario DECIMAL(10,2) NOT NULL,
estado BIT,
CONSTRAINT FK_Empleados FOREIGN KEY (idEmpleado)
	REFERENCES Empleados(idEmpleado),
CONSTRAINT FK_TipoContratacion FOREIGN KEY (idTipoContratacion)
	REFERENCES TipoContratacion (idTipoContratacion),
CONSTRAINT FK_Departamento FOREIGN KEY (idDepartamento)
	REFERENCES Departamento (idDepartamento),
CONSTRAINT FK_Cargos FOREIGN KEY (idCargo)
	REFERENCES Cargos (idCargo),

)