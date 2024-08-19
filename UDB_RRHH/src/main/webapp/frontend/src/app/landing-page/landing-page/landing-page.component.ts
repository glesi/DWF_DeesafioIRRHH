import {Component} from '@angular/core';
import { TipoContratacionService } from '../../../services/tipoContratacionService/tipoContratacion.service';
import {ViewService} from "../../../services/viewService/view.service";
import {CommonModule, NgFor} from "@angular/common";
import {Cargo, Contrataciones, Departamento, Empleado, Individuo, TipoContratacion} from "../../Interfaces/Interfaces";
import {RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {CargoService} from "../../../services/cargoService/cargo.service";
import {EmpleadoService} from "../../../services/empleadoService/empleado.service";
import {ContratacionService} from "../../../services/contratacionService/contratacion.service";
import Swal from "sweetalert2";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {DepartamentoService} from "../../../services/departamentoService/departamento.service";
import { formatDate } from '@angular/common';
import {lastValueFrom, max, switchMap} from "rxjs";
import {get} from "node:http";


@Component({
  selector: 'app-landing-page',
  standalone: true,
  imports: [CommonModule,
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    FormsModule,
    NgFor,
    ReactiveFormsModule],
  templateUrl: './landing-page.component.html',
  styleUrl: './landing-page.component.css',
})


export class LandingPageComponent {

  constructor(private tipoContratacionService: TipoContratacionService,
              private viewSrv: ViewService,
              private cargoSrv: CargoService,
              private empleadoSrv: EmpleadoService,
              private contratoSrv: ContratacionService,
              private dptoSrv: DepartamentoService
              ) {}

  viewDatos:Empleado[] =[];
  viewActive:Empleado[] =[];
  view: Empleado={idEmpleado:0,numeroDui:'', nombrePersona:'', numeroTelefono:'',correoInstitucional:'',cargo:'', nombreDepartamento:'',fechaContratacion:new Date(),salario:0,fechaNacimiento: new Date(), tipoContratacion:'',estado:true};
  contratraciones: Contrataciones[]=[];
  contratoSend: Contrataciones={idContratacion: 0, idDepartamento:0, idEmpleado:0, idCargo:0, idTipoContratacion:0,fechaContratacion:"",salario:0,estado:true,};
  contratoAdd: Contrataciones={idContratacion: 0, idDepartamento:0, idEmpleado:0, idCargo:0, idTipoContratacion:0,fechaContratacion:"",salario:0,estado:true,};
  todosLosTipos: TipoContratacion[]=[];
  tiposContratos: TipoContratacion={idTipoContratacion:0, tipoContratacion:''};
  individuo: Individuo={idEmpleado:0,numeroDui:'',nombrePersona:'',usuario:'',numeroTelefono:'', correoInstitucional:'',fechaNacimiento:''};
  individuos : Individuo[]=[];
  individuoAdd: Individuo={idEmpleado:0,numeroDui:'',nombrePersona:'',usuario:'',numeroTelefono:'', correoInstitucional:'',fechaNacimiento:''};
  todosLosCargos: Cargo[]=[];
  cargo : Cargo ={idCargo:0,cargo:'',descripcionCargo:'', jefatura: false};
  departamentos: Departamento[]=[];
  depto : Departamento ={idDepartamento:0,nombreDepartamento:'', descripcionDepartamento:'' }

  //variable para path de endpoint
  pathE :string = '';
  pathA: string= '';
  pathC: string='';
  idBaja: number=0;
  ahora: Date = new Date();

  ngOnInit(): void {
    this.getCargo();
    this.getViewDatos();
    this.getTipoContratacion();
    this.getDepartamentos();
    console.log("Individuos: ")
    this.getIndividuos();
    this.getAllContratos();
    this.getActive();
  }

  //Funcion para asignar el path al endpoint para procesar solicitudes
  assingPath(){
    this.pathE='/';
    if(this.view.idEmpleado>0){
      this.pathE += this.view.idEmpleado.toString();
    }
  }
  //funcion para asignar path para hacer las peticiones a la tabla contrataciones
  assignPathContrato(id: number ){
    this.pathC='/'+id;
  }
  //funcion para obtener la fecha de hoy
  getToday(){
    const today = new Date();
    this.ahora = today;
  }
//Funcion para obtener todos los datos en la vista
  getViewDatos(){
    this.viewSrv.get(this.pathE).subscribe({
      next: (result) => {
        this.viewDatos = result;
      },
      error: (error) => {
        console.log("Error: "+error);
      }
    })
  }
  //muestra solamente los datos de empleados activos
  getActive(){
    this.viewSrv.get(this.pathE).subscribe({
      next: (result) => {
        this.viewActive = result.filter((dato: Empleado) => dato.estado);
      },
      error: (error) => {
        console.log("Error: "+error);
      }
    })
  }

  getIndividuos(){
    this.empleadoSrv.get(this.pathE).subscribe({
      next: (result) => {
        this.individuos = result;
      },
      error: (error) => {
        console.log("Error: "+error);
      }
    })
  }
//Funcion para obtener solamente un dato
  getViewDatosById(){
    this.viewSrv.get(this.pathE).subscribe({
      next:(result)=>{
        this.view = result;
    },
      error: (error) => {
        console.log("Error: "+error);
      }
    })
  }
//Funcion para obtener todos los cargos
  getCargo(){
    this.cargoSrv.get(this.pathA).subscribe({
      next: (result)=>{
        this.todosLosCargos = result;
      },
      error:(error)=>{
        console.log("Error: "+error);
    }
    })
  }
  //Funcion para obtener un solo cargo
  getCargoById(){
    this.cargoSrv.get(this.pathA).subscribe({
      next:(result)=>{
        this.cargo = result;
      },
      error: (error) => {
        console.log("Error: "+error);
      }
    })
  }
  //Funcion para obtener todos los tipos de contratacion
  getTipoContratacion(){
    this.tipoContratacionService.get(this.pathA).subscribe({
      next:(result)=>{
        this.todosLosTipos=result;
      },
      error:(error)=>{
        console.log("Error: "+error);
      }

    })
  }
  //Funcion para obtener un solo tipo de contratacion
  getTipoContratacionById(){
    this.tipoContratacionService.get(this.pathA).subscribe({
      next:(result)=>{
        this.tiposContratos=result;
        console.log(result);
      },
      error:(error)=>{
        console.log("Error: "+error);
      }

    })
  }
  //Funcion para obtener todos los departamentos
  getDepartamentos(){
    this.dptoSrv.get(this.pathA).subscribe({
      next:(result)=>{
        this.departamentos = result;
      },
      error: (error)=>{
        console.log("Error: "+error);
      }
    })
  };
  //Funcion para obtener todos los datos de los contratos
  getAllContratos(){
    this.contratoSrv.get(this.pathA).subscribe({
      next:(result)=>{
        this.contratraciones = result;
      },
      error: (error)=>{
        console.log("Error: "+error);
      }
    })
  };
  //Funcion para obtener un solo contrato
  getContratoById(id: number){
    this.contratoSrv.get("/"+id).subscribe({
      next:(result)=>{
        this.contratoSend = result;
      },
      error: (error) => {
        console.log("Error: "+error);
      }
    })
  }

  updateAll(){
    this.updateEmpleado().then(()=>{
      this.updateContrato().then(()=>{
        console.log("finish");
      })
    });
  };

  async updateEmpleado(){
    try {
      const result = await lastValueFrom(
        this.empleadoSrv.post(this.pathE, {
          accion: "actualizar",
          json: this.individuo,
        })
      );
      console.log(result);
    } catch (error) {
      console.log("Error:", error);
    }
  }

  async updateContrato(){
    try {
      const result = await lastValueFrom(
        this.contratoSrv.post(this.pathC, {
          accion: "actualizar",
          json: this.contratoSend,
        })
      );
      console.log(result);
      Swal.fire({
        position: "center",
        icon: "success",
        title: "El registro se ha agregado con éxito",
        showConfirmButton: false,
        timer: 1500,
      }).then(() => {
        location.reload();
      });
    } catch (error) {
      console.log("Error:", error);
    }
  }

//Enviar peticion de agreagar registro a tablas empleado y contrataciones
  async saveAll(){
    await this.saveEmpleado();
  }
//guardar reistro en tabla empleados
  async saveEmpleado(): Promise<void> {
    try {
      const result = await lastValueFrom(
        this.empleadoSrv.post(this.pathA, {
          accion: "insertar",
          json: this.individuoAdd,
        })
      );
      if(result){
        const allNew = await lastValueFrom(
          this.empleadoSrv.get("/")
        )
        const id= allNew.reduce((max: Empleado ,current:Empleado)=>{
          return current.idEmpleado> max.idEmpleado ? current : max;
        }).idEmpleado;
        await this.saveContrato(id);
      } else{
        console.log("Algo paso")
      }
    } catch (error) {
      console.log("Error:", error);
    }
  }
//guardar registro en tabla contrataciones
  async saveContrato(id:number) {
    this.contratoAdd.idEmpleado = id;
    try {
      const result = await lastValueFrom(
        this.contratoSrv.post(this.pathA, {
          accion: "insertar",
          json: this.contratoAdd,
        })
      );
      console.log(result);
      Swal.fire({
        position: "center",
        icon: "success",
        title: "El registro se ha agregado con éxito",
        showConfirmButton: false,
        timer: 1500,
      })
        .then(() => {
        location.reload();
      });
    } catch (error) {
      console.log("Error:", error);
    }
  }
//Funcion para eliminar registros de dos tablas, empleados y contrataciones
//usa un stored procedure en el backend
  deleteBoth(){
    var object ={
      "accion": "eliminar",
      "json": this.view,
    }
    console.log(this.pathE);
    this.viewSrv.post(this.pathE,object).subscribe({
      next: (result)=>{
        Swal.fire({
          position: 'center',
          icon: "success",
          title: 'Registro eliminado',
          showConfirmButton: false,
          timer: 1500
        }).then(()=>{
          location.reload();
          console.log(result);
        })
      },
      error:(error)=>{
        console.log("Error: "+JSON.stringify(error));
      }
    })
  }

  showDatos(){
    this.viewSrv.get(this.pathE).subscribe({
      next:(result)=>{
        console.log(result);
        const resultado = this.individuos.filter(dato => dato.idEmpleado === result.idEmpleado);
        const resCargos= this.todosLosCargos.filter(dato=> dato.cargo===result.cargo);
        const resDept= this.departamentos.filter(dato => {dato.idDepartamento===result.idDepartamento});
        const resContratos= this.contratraciones.filter(dato=> dato.idEmpleado===result.idEmpleado);
        console.log(resContratos);
        this.view = result;
        this.individuo.idEmpleado= result.idEmpleado;
        this.individuo.numeroDui= result.numeroDui;
        this.individuo.nombrePersona= result.nombrePersona;
        this.individuo.usuario= result.usuario;
        this.individuo.numeroTelefono= result.numeroTelefono;
        this.individuo.correoInstitucional= result.correoInstitucional;
        this.individuo.fechaNacimiento= result.fechaNacimiento;
        this.individuo.usuario= resultado[0].usuario;
        this.contratoSend.salario = result.salario;
        this.contratoSend.fechaContratacion= result.fechaContratacion;
        this.depto.nombreDepartamento= result.nombreDepartamento;
        this.contratoSend.idContratacion = resContratos[0].idContratacion;
        console.log(this.contratoSend.idContratacion);
        this.contratoSend.idCargo = result.idCargo;
        this.assignPathContrato(this.contratoSend.idContratacion);



      },
      error: (error) => {
        console.log("Error: "+error);
      }
    })
  }

  onCheckBoxChange(event: Event,idEmpleado:number ): void {
    const trg = event.target as HTMLInputElement;
    this.idBaja=idEmpleado;
    this.view.idEmpleado=idEmpleado;
    this.contratoSend.idEmpleado=idEmpleado;
    this.individuo.idEmpleado=idEmpleado;
    this.getViewDatosById();
    this.assingPath();
    this.showDatos();
    this.getViewDatosById();
    this.getCargoById();
    this.getDepartamentos();
    console.log(this.pathE);
    console.log(this.view);
  }

//Funcion para "procesar una baja", que cambia el estado del empleado
procesarBaja(){
  this.getAllContratos();
    // const Idbaja = this.individuos.filter(dato => {dato.idEmpleado==id});
    const idE = this.contratraciones.filter(dato=>dato.idEmpleado===this.idBaja);
    const idBorrar = idE[0].idContratacion;

    this.getContratoById(idBorrar);
    console.log(this.contratoSend);
    this.contratoSend.idContratacion=idBorrar;
    this.contratoSend.idDepartamento=idE[0].idDepartamento;
    this.contratoSend.idEmpleado=idE[0].idEmpleado;
    this.contratoSend.idCargo= idE[0].idCargo;
    this.contratoSend.idTipoContratacion= idE[0].idTipoContratacion;
    this.contratoSend.fechaContratacion=idE[0].fechaContratacion;
    this.contratoSend.salario= idE[0].salario;
    console.log(this.contratoSend);
    this.contratoSend.estado= false;
    this.contratoSrv.post("/"+idBorrar,{
      "accion":"actualizar",
      "json":this.contratoSend,
    }).
    subscribe({
      next: (result) => {
        Swal.fire({
          position: "center",
          icon: "success",
          title: "Baja procesada exitosamente exitosa",
          showConfirmButton: false,
          timer: 1500
        }).then(()=>{
          location.reload()
        })
      },
      error: (error) => {
        Swal.fire({
          position: "center",
          icon: "error",
          title: "Hubo un error al procesar la baja",
          showConfirmButton: true,
        });

      }
    })

};
}

