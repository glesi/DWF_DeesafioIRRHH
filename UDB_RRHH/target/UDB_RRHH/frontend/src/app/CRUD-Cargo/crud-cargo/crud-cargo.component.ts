import { Component } from '@angular/core';
import {FormBuilder, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgForOf} from "@angular/common";
import {Cargo} from "../../Interfaces/Interfaces";
import {CargoService} from "../../../services/cargoService/cargo.service";
import Swal from "sweetalert2";

@Component({
  selector: 'app-crud-cargo',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    ReactiveFormsModule
  ],
  templateUrl: './crud-cargo.component.html',
  styleUrl: './crud-cargo.component.css'
})
export class CRUDCargoComponent {
  //Decalaracioin de array para guardar cargos
  cargos: Cargo[]=[];
  //Declaracion de objeto para enviar solicitudes por POST
  cargoSend: Cargo={idCargo:0, cargo:'', descripcionCargo:'',jefatura: false}
  path :string = '';

  constructor(private cargoSrv : CargoService) {}

  ngOnInit():void {
    this.getCargos();
  }
  assingPath(){
    if(this.cargoSend.idCargo>0){
      this.path = '/'+this.cargoSend.idCargo;
    }
  }
  getCargos(){
    this.cargoSrv.get(this.path).subscribe({
    next:(result)=>{
      this.cargos = result;
    },
      error:(error)=>{
      console.log(error);
      }
    })
  }

  getCargoById(){
    this.cargoSrv.get(this.path).subscribe({
      next:(result)=>{
        this.cargoSend = result;
        console.log(result)
      },
      error:(error)=>{
        console.log(error);
      }
    })
  }

  saveCargo(){
    var object = {
      "accion": "insertar",
      "json": this.cargoSend,
    }
    console.log(this.cargoSend);
    if(this.cargoSend.descripcionCargo.trim().length > 0 || this.cargoSend.cargo.trim().length > 0 || this.cargoSend.descripcionCargo.trim().length > 0 ){
      this.cargoSrv.post(this.path,object).subscribe({
        next: (result)=>{
          Swal.fire({
            position: "center",
            icon: "success",
            title: "se agrego exitosament",
            showConfirmButton: false,
            timer: 1500
          }).then(()=>{
            location.reload();
            console.log(result);
          })
        },
        error:(error)=>{
          Swal.fire({
            position: "center",
            icon: "error",
            title: "No se pudo agregar el elemento nuevo",
            showConfirmButton: false,
            timer: 1500
          })
          console.log(error);
        }
      })
    }else{
      Swal.fire({
        position: "center",
        icon:"error",
        title: "Valor invalido - No se aceptan cadenas vacias",
        showConfirmButton:false
      })
    }

  }

  updateCargo(){
    var object={
      "accion": "actualizar",
      "json": this.cargoSend,
    }
    if(this.cargoSend.descripcionCargo.trim().length > 0 || this.cargoSend.cargo.trim().length > 0 || this.cargoSend.descripcionCargo.trim().length > 0 ){
      this.cargoSrv.post(this.path,object).subscribe({
        next: (result)=>{
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
        error:(error)=>{
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
    }else {
      Swal.fire({
        position: "center",
        icon: "warning",
        title: "Valor invalido - No se aceptan cadenas vacias",
        showConfirmButton: true,
      })
    }
  }

  deleteCargo(){
    var object = {
      "accion": "eliminar",
      "json": this.cargoSend,
    }
    this.cargoSrv.post(this.path,object).subscribe({
      next: (result)=>{
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
      },
      error: (error) =>{
        Swal.fire({
          position: "center",
          icon: "error",
          title: "Debe seleccionar el elemento a borrar",
          showConfirmButton: false,
          timer: 1500
        })
      }
    })
  }

  onCheckBoxChange(event: Event,idCargo:number ): void {

    this.cargoSend.idCargo=idCargo;
    this.assingPath();
    this.getCargoById();
    console.log(this.cargoSend.idCargo);
  }

}
