import { Component } from '@angular/core';
import {FormBuilder, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgForOf} from "@angular/common";
import {Cargo} from "../../Interfaces/Interfaces";
import {CargoService} from "../../../services/cargoService/cargo.service";

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
  cargo: Cargo[]=[];
  //Declaracion de objeto para enviar solicitudes por POST
  cargoSend: Cargo={idCargo:0, cargo:'', descripcionCargo:'',jefatura: false}
  path :string = '';

  constructor(private cargoSrv : CargoService) {}

  ngOnInit():void {
    this.getCargos();
  }
  assigPath(){
    if(this.cargoSend.idCargo>0){
      this.path = '/'+this.cargoSend.idCargo;
    }
  }
  getCargos(){
    this.cargoSrv.get(this.path).subscribe({
    next:(result)=>{
      this.cargo = result;
    },
      error:(error)=>{
      console.log(error);
      }
    })
  }

  getCargoById(){
    this.cargoSrv.get(this.path).subscribe({
      next:(result)=>{
        this.cargo = result;
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
      "json": this.cargo,
    }
    if(this.cargoSend.descripcionCargo.trim().length > 0 || this.cargoSend.cargo.trim().length > 0 ){}
  }

}
