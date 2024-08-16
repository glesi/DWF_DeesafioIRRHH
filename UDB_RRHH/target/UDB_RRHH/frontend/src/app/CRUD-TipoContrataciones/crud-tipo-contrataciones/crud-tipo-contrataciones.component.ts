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

  // @ViewChild()
  //Declaracion de array para guardar la response del GET
  contratraciones: TipoContratacion[]=[];
  //Declaracion de objeto para enviar solicitudes por POST
  contratracionesSend: TipoContratacion={idTipoContratacion:0, tipoContratacion:''};

  allDisabled = false;

  constructor(private tipoContratacionesSrv : TipoContratacionService) {}

  ngOnInit(): void {
    this.getTipoContrataciones();
  }

  getTipoContrataciones() {
    this.tipoContratacionesSrv.get().subscribe({
      next: (result) => {
        this.contratraciones = result;
        console.log(result)
      },
      error: (error) => {
        console.log(error)
      }
    })
  }

  saveTipoContrataciones() {
    // this.contratracionesSend = {idTipoContratacion:0, tipoContratacion:''};
    var object = {
      "action": "insertar",
      "json": this.contratracionesSend,

    }
    console.log(this.contratracionesSend);
    this.tipoContratacionesSrv.post(object).subscribe({
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
  }

  updateTipoContrataciones() {
    this.contratracionesSend = {idTipoContratacion:0, tipoContratacion:''};
    //Agregar objeto
    var object = {
      "action": "actualizar",
      "json": this.contratracionesSend,
    }
    this.tipoContratacionesSrv.post(object).subscribe({
      next: (result) => {
        console.log(result)
      },
      error: (error) => {
        console.log(error)
      }
    })
  }

  deleteTipoContrataciones() {
    //Agregar objeto
    var object = {
      "action": "eliminar",
      "json": this.contratracionesSend,
    }
    this.tipoContratacionesSrv.post(object).subscribe({
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
    if(trg.checked){

      this.contratracionesSend.idTipoContratacion= idTipoContratacion;
      console.log(this.contratracionesSend.idTipoContratacion);


    }
  }
}
