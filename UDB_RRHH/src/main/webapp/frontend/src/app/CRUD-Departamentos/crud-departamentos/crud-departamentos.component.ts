import { Component } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgForOf} from "@angular/common";
import {Departamento} from "../../Interfaces/Interfaces";
import {DepartamentoService} from "../../../services/departamentoService/departamento.service";
import Swal from "sweetalert2";

@Component({
  selector: 'app-crud-departamentos',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    ReactiveFormsModule
  ],
  templateUrl: './crud-departamentos.component.html',
  styleUrl: './crud-departamentos.component.css'
})
export class CRUDDepartamentosComponent {
//Declaracion de array para guardar la response del GET
  departamentos: Departamento[]=[];
  //Declaracion de objeto para enviar solicitudes por POST
  departamentoSend: Departamento={idDepartamento:0, nombreDepartamento:'',descripcionDepartamento:''};
  path : string = '';


  constructor(private departamentoSrv : DepartamentoService) {}

  ngOnInit(): void {
    this.getDepartamento();
  }

  assignPath(){
    if (this.departamentoSend.idDepartamento > 0){
      this.path = '/'+this.departamentoSend.idDepartamento;
    }
  }

  getDepartamento() {
    // if (this.departamentoSend.idDepartamento > 0){
    //   this.path = '/'+this.departamentoSend.idDepartamento;
    // }
    // console.log(this.path);
    this.departamentoSrv.get(this.path).subscribe({
      next: (result) => {
        this.departamentos = result;
        console.log(result)
      },
      error: (error) => {
        console.log(error)
      }
    })
  }

  getDepartamentoByID(){
    this.departamentoSrv.get(this.path).subscribe({
      next: (result) => {
        this.departamentoSend = result;
        console.log(result)
      },
      error: (error) => {
        console.log(error)
      }
    })
  }

  saveDepartamento() {

    var object = {
      "accion": "insertar",
      "json": this.departamentoSend,

    }
    if(this.departamentoSend.nombreDepartamento.trim().length > 0 || this.departamentoSend.descripcionDepartamento.trim().length > 0 ){
      this.departamentoSrv.post(this.path,object).subscribe({
        next: (result) => {
          Swal.fire({
            position: "center",
            icon: "success",
            title: "Se agrego exitosamente",
            showConfirmButton: false,
            timer: 1500
          }).then(()=>{
            location.reload()
            console.log(result)
          });

        },
        error: (error) => {
          Swal.fire({
            position: "center",
            icon: "error",
            title: "No se pudo agregar el elemento nuevo",
            showConfirmButton: false,
            timer: 1500
          })
          console.log(error)
        }
      })
    } else{
      Swal.fire({
        position: "center",
        icon:"warning",
        title: "Valor invalido - No se aceptan cadenas vacias",
        showConfirmButton: true,}
      )
    }
    console.log(this.departamentoSend);

  }

  updateDepartamento() {
    //Agregar objeto
    var object = {
      "accion": "actualizar",
      "json": this.departamentoSend,
    }

    if (this.departamentoSend.nombreDepartamento.trim().length > 0 || this.departamentoSend.descripcionDepartamento.trim().length > 0 ) {
      this.departamentoSrv.post(this.path,object).subscribe({
        next: (result) => {
          Swal.fire({
            position: "center",
            icon: "success",
            title: "Actualizacion procesada",
            showConfirmButton: false,
            timer: 1500
          }).then(() => {
            location.reload()
            console.log(result)
          });

        },
        error: (error) => {
          Swal.fire({
            position: "center",
            icon: "error",
            title: "No se pudo actualizar elemento",
            showConfirmButton: false,
            timer: 1500
          })
          console.log("No se pudo porque "+ JSON.stringify(error) )
        }
      })
    } else {
      Swal.fire({
        position: "center",
        icon: "warning",
        title: "Valor invalido - No se aceptan cadenas vacias",
        showConfirmButton: true,
      })
    }
  }

  deleteDepartamento() {
    //Agregar objeto
    var object = {
      "accion": "eliminar",
      "json": this.departamentoSend,
    }
    this.departamentoSrv.post(this.path,object).subscribe({
      next: (result) => {
        console.log(this.path)
        Swal.fire({
          position: "center",
          icon: "success",
          title: "Eliminacion exitosa",
          showConfirmButton: false,
          timer: 1500
        }).then(()=>{
          location.reload()
          console.log(result)
        });
        console.log(result)
      },
      error: (error) => {
        Swal.fire({
          position: "center",
          icon: "error",
          title: "Debe seleccionar el elemento a borrar",
          showConfirmButton: false,
          timer: 1500
        })
        console.log(error)
      }
    })
  }

  onCheckBoxChange(event: Event,idDepartment:number ): void {
    const trg = event.target as HTMLInputElement;
    this.departamentoSend.idDepartamento= idDepartment;
    this.assignPath();
    this.getDepartamentoByID();
    console.log(this.departamentoSend.idDepartamento);

  }


}
