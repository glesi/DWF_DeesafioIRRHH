import { Component } from '@angular/core';
import { TipoContratacionService } from '../../../services/tipoContratacionService/tipoContratacion.service';

@Component({
  selector: 'app-landing-page',
  standalone: true,
  imports: [],
  templateUrl: './landing-page.component.html',
  styleUrl: './landing-page.component.css'
})
export class LandingPageComponent {

  constructor(private tipoContratacionService: TipoContratacionService) {}

  ngOnInit(): void {
  }

  getTipoContrataciones() {
    this.tipoContratacionService.get().subscribe({
      next: (result) => {
        console.log(result)
      },
      error: (error) => {
        console.log(error)
      }
    })
  }

  saveTipoContrataciones() {
    //Agregar objeto
    var object = {
      "action": "insertar"
    }
    this.tipoContratacionService.post(object).subscribe({
      next: (result) => {
        console.log(result)
      },
      error: (error) => {
        console.log(error)
      }
    })
  }

  updateTipoContrataciones() {
    //Agregar objeto
    var object = {
      "action": "actualizar"
    }
    this.tipoContratacionService.post(object).subscribe({
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
      "action": "eliminar"
    }
    this.tipoContratacionService.post(object).subscribe({
      next: (result) => {
        console.log(result)
      },
      error: (error) => {
        console.log(error)
      }
    })
  }
}
