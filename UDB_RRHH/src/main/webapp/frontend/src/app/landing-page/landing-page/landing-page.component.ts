import {Component, NO_ERRORS_SCHEMA} from '@angular/core';
import { TipoContratacionService } from '../../../services/tipoContratacionService/tipoContratacion.service';
import {ViewService} from "../../../services/viewService/view.service";
import {CommonModule} from "@angular/common";
import {Contrataciones, Empleado, Individuo, TipoContratacion} from "../../Interfaces/Interfaces";
import {RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {CargoService} from "../../../services/cargoService/cargo.service";
import {EmpleadoService} from "../../../services/empleadoService/empleado.service";
import {ContratacionService} from "../../../services/contratacionService/contratacion.service";


@Component({
  selector: 'app-landing-page',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './landing-page.component.html',
  styleUrl: './landing-page.component.css',
  schemas: [NO_ERRORS_SCHEMA]
})


export class LandingPageComponent {

  constructor(private tipoContratacionService: TipoContratacionService,
              private viewSrv: ViewService,
              private cargoSrv: CargoService,
              private empleadoSrv: EmpleadoService,
              private contratoSrv: ContratacionService) {}

  viewDatos:Empleado[] =[];
  contratraciones: Contrataciones[]=[];
  contratracionesSend: Contrataciones={idContratacion: 0, idDepartamento:0, idEmpleado:0, idCargo:0, idTipoContratacion:0,fechaContratacion:"",salario:0,estado:false,};
  individuo: Individuo={idEmpleado:0,numeroDui:'',nombrePersona:'',usuario:'',numeroTelefono:'', correoInstitucional:'',fechaNacimiento:''}

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
