export interface Empleado {
  correoInstitucional: string;
  idEmpleado: number;
  fechaNacimiento: Date;
  fechaContratacion: Date ;
  numeroDui: string;
  salario: number;
  nombrePersona: string;
  cargo: string;
  nombreDepartamento: string;
  numeroTelefono: string;
  tipoContratacion: string;
}
export interface TipoContratacion {
  idTipoContratacion: number;
  tipoContratacion: string;
}

export interface Departamento {
  idDepartamento: number;
  nombreDepartamento: string;
  descripcionDepartamento: string;

}

export interface Cargo {
  idCargo: number;
  cargo: string;
  descripcionCargo: string;
  jefatura: boolean;
}

export interface Contrataciones {
  idContratacion: number;
  idDepartamento: number;
  idEmpleado: number;
  idCargo: number;
  idTipoContratacion: number;
  fechaContratacion: string;
  salario: number;
  estado: boolean;
}

export interface Individuo {
  idEmpleado: number;
  numeroDui: string;
  nombrePersona: string;
  usuario: string;
  numeroTelefono: string;
  correoInstitucional: string;
  fechaNacimiento: string;
}
