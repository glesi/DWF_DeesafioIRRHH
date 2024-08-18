import {Component, NO_ERRORS_SCHEMA} from '@angular/core';
import { TipoContratacionService } from '../../../services/tipoContratacionService/tipoContratacion.service';
import {ViewService} from "../../../services/viewService/view.service";
import {CommonModule, NgFor} from "@angular/common";
import {Cargo, Contrataciones, Empleado, Individuo, TipoContratacion} from "../../Interfaces/Interfaces";
import {RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {CargoService} from "../../../services/cargoService/cargo.service";
import {EmpleadoService} from "../../../services/empleadoService/empleado.service";
import {ContratacionService} from "../../../services/contratacionService/contratacion.service";
import Swal from "sweetalert2";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";


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
  schemas: [NO_ERRORS_SCHEMA]
})


export class LandingPageComponent {

  constructor(private tipoContratacionService: TipoContratacionService,
              private viewSrv: ViewService,
              private cargoSrv: CargoService,
              private empleadoSrv: EmpleadoService,
              private contratoSrv: ContratacionService,
              ) {}

  viewDatos:Empleado[] =[];
  view: Empleado={idEmpleado:0,numeroDui:'', nombrePersona:'', numeroTelefono:'',correoInstitucional:'',cargo:'',fechaContratacion:'',salario:0,fechaNacimiento:''};
  contratraciones: Contrataciones[]=[];
  contratoSend: Contrataciones={idContratacion: 0, idDepartamento:0, idEmpleado:0, idCargo:0, idTipoContratacion:0,fechaContratacion:"",salario:0,estado:false,};
  todosLosTipos: TipoContratacion[]=[];
  tiposContratos: TipoContratacion={idTipoContratacion:0, tipoContratacion:''};
  individuo: Individuo={idEmpleado:0,numeroDui:'',nombrePersona:'',usuario:'',numeroTelefono:'', correoInstitucional:'',fechaNacimiento:''};
  todosLosCargos: Cargo[]=[];
  cargo : Cargo ={idCargo:0,cargo:'',descripcionCargo:'', jefatura: false};

  //variable para path de endpoint
  path :string = '';

  ngOnInit(): void {
    this.getCargo();
    this.getViewDatos();
    this.getTipoContratacion();
  }

  assingPath(){
    if(this.view.idEmpleado>0){
      this.path = '/'+this.view.idEmpleado;
    }
  }
//Funcion para obtener todos los datos en la vista
  getViewDatos(){
    this.viewSrv.get(this.path).subscribe({
      next: (result) => {
        this.viewDatos = result;
        console.log(this.viewDatos);
      },
      error: (error) => {
        console.log("Error: "+error);
      }
    })
  }
//Funcion para obtener solamente un datos
  getViewDatosById(){
    this.viewSrv.get(this.path).subscribe({
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
    this.cargoSrv.get(this.path).subscribe({
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
    this.cargoSrv.get(this.path).subscribe({
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
    this.tipoContratacionService.get(this.path).subscribe({
      next:(result)=>{
        this.todosLosTipos=result;
        console.log(result);
      },
      error:(error)=>{
        console.log("Error: "+error);
      }

    })
  }
  //Funcion para obtener un solo tipo de contratacion
  getTipoContratacionById(){
    this.tipoContratacionService.get(this.path).subscribe({
      next:(result)=>{
        this.tiposContratos=result;
        console.log(result);
      },
      error:(error)=>{
        console.log("Error: "+error);
      }

    })
  }


  saveEmpleado(){
    var object = {
      "accion": "insertar",
      "json": this.individuo,
    }
    this.empleadoSrv.post(this.path, object).subscribe({
      next: (result)=>{
        Swal.fire({
          position: 'center',
          icon: 'success',
          title: 'Se agrego existosament',
          showConfirmButton: false,
          timer: 1500
        }).then(()=>{
          location.reload
          console.log(result)
        });
      }
    })
  }

  onCheckBoxChange(event: Event,idEmpleado:number ): void {
    const trg = event.target as HTMLInputElement;
    this.view.idEmpleado=idEmpleado;
    this.assingPath();
    this.getViewDatosById();
    console.log(this.view.idEmpleado);
  }
}
