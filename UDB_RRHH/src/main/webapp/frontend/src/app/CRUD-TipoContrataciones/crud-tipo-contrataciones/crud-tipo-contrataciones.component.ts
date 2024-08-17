import {Component, ViewChild} from '@angular/core';
import {CommonModule} from "@angular/common";
import {TipoContratacion} from "../../Interfaces/Interfaces";
import {RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {TipoContratacionService} from "../../../services/tipoContratacionService/tipoContratacion.service";
import {FormsModule} from "@angular/forms";
import Swal from "sweetalert2";

@Component({
  selector: 'app-crud-tipo-contrataciones',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive, FormsModule],
  templateUrl: './crud-tipo-contrataciones.component.html',
  styleUrl: './crud-tipo-contrataciones.component.css'
})
export class CRUDTipoContratacionesComponent {

  //Declaracion de array para guardar la response del GET
  contratraciones: TipoContratacion[]=[];
  //Declaracion de objeto para enviar solicitudes por POST
  contratracionesSend: TipoContratacion={idTipoContratacion:0, tipoContratacion:''};

  path : string = '/';
  allDisabled = false;

  constructor(private tipoContratacionesSrv : TipoContratacionService) {}

  ngOnInit(): void {
    this.getTipoContrataciones();
  }

  assignPath(){
    if (this.contratracionesSend.idTipoContratacion > 0){
      this.path = '/'+this.contratracionesSend.idTipoContratacion;
    }
  }

  getTipoContrataciones() {
    // if(this.contratracionesSend.idTipoContratacion>0){
    //   this.path='/'+this.contratracionesSend.idTipoContratacion;
    // }
    this.tipoContratacionesSrv.get(this.path).subscribe({
      next: (result) => {
        this.contratraciones = result;
        console.log(result)
      },
      error: (error) => {
        console.log(error)
      }
    })
  }

  getContratacionByID(){
    this.tipoContratacionesSrv.get(this.path).subscribe({
      next: (result) =>{
        this.contratracionesSend = result;
      },
      error: (error) => {
        console.log(error)
      }
    })
  }

  saveTipoContrataciones() {

    var object = {
      "accion": "insertar",
      "json": this.contratracionesSend,

    }
    if(this.contratracionesSend.tipoContratacion.trim().length > 0){
      this.tipoContratacionesSrv.post(this.path, object).subscribe({
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
    console.log(this.contratracionesSend);

  }

  updateTipoContrataciones() {
    //Agregar objeto
    var object = {
      "accion": "actualizar",
      "json": this.contratracionesSend,
    }

    if (this.contratracionesSend.tipoContratacion.trim().length > 0) {
      this.tipoContratacionesSrv.post(this.path, object).subscribe({
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
          console.log(error)
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

  deleteTipoContrataciones() {
    //Agregar objeto
    var object = {
      "accion": "eliminar",
      "json": this.contratracionesSend,
    }
    this.tipoContratacionesSrv.post(this.path, object).subscribe({
      next: (result) => {
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

  onCheckBoxChange(event: Event,idTipoContratacion:number ): void {
    const trg = event.target as HTMLInputElement;
      this.contratracionesSend.idTipoContratacion= idTipoContratacion;
      this.assignPath();
      this.getContratacionByID();
      console.log(this.contratracionesSend.idTipoContratacion);

  }
}
