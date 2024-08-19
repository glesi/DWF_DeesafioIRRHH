import { Component } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgForOf} from "@angular/common";
import {RouterOutlet} from "@angular/router";
import {Empleado} from "../../Interfaces/Interfaces";
import {ViewService} from "../../../services/viewService/view.service";
import Swal from "sweetalert2";

@Component({
  selector: 'app-crud-contratos',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    ReactiveFormsModule,
    RouterOutlet
  ],
  templateUrl: './crud-contratos.component.html',
  styleUrl: './crud-contratos.component.css'
})
export class CRUDContratosComponent {

  constructor(private viewSrv: ViewService,) {
  }

  ngOnInit(){
    this.getInactive()
  }

  pathE :string = '';

  viewInactive:Empleado[] =[];
  view: Empleado={idEmpleado:0,numeroDui:'', nombrePersona:'', numeroTelefono:'',correoInstitucional:'',cargo:'', nombreDepartamento:'',fechaContratacion:new Date(),salario:0,fechaNacimiento: new Date(), tipoContratacion:'',estado:true};


  assingPath(){
    this.pathE='/';
    if(this.view.idEmpleado>0){
      this.pathE += this.view.idEmpleado.toString();
    }
  }
  //Funcion para obtener los registros de empleados inactivos
  getInactive(){
    this.viewSrv.get(this.pathE).subscribe({
      next: (result) => {
        this.viewInactive = result.filter((dato: Empleado) => !dato.estado);
      },
      error: (error) => {
        console.log("Error: "+error);
      }
    })
  }
  //Funcion para borrar registros de dos tablas
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

  onCheckBoxChange(event: Event,idEmpleado:number ): void {
    const trg = event.target as HTMLInputElement;
    this.view.idEmpleado=idEmpleado;
    this.assingPath();
    console.log(this.pathE);
    console.log(this.view);
  }


}
