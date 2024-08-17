import {Component, NO_ERRORS_SCHEMA} from '@angular/core';
import { TipoContratacionService } from '../../../services/tipoContratacionService/tipoContratacion.service';
import {ViewService} from "../../../services/viewService/view.service";
import {CommonModule} from "@angular/common";
import {Empleado, TipoContratacion} from "../../Interfaces/Interfaces";
import {RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";


@Component({
  selector: 'app-landing-page',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './landing-page.component.html',
  styleUrl: './landing-page.component.css',
  schemas: [NO_ERRORS_SCHEMA]
})


export class LandingPageComponent {

  constructor(private tipoContratacionService: TipoContratacionService, private viewSrv: ViewService) {}

  viewDatos:Empleado[] =[];
  contratraciones: TipoContratacion[]=[];
  contratracionesSend: TipoContratacion[]=[];
  ngOnInit(): void {
    // this.getTipoContrataciones();
    this.getViewDatos();
  }



  getViewDatos(){
    this.viewSrv.get().subscribe({
      next: (result) => {
        this.viewDatos = result;
        console.log(this.viewDatos);
      },
      error: (error) => {
        console.log("Error: "+error);
      }
    })
  }
}
