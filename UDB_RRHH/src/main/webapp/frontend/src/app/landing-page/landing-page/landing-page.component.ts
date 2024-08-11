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
    this.getTipoContrataciones()
    this.saveTipoContrataciones()
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
    this.tipoContratacionService.post().subscribe({
      next: (result) => {
        console.log(result)
      },
      error: (error) => {
        console.log(error)
      }
    })
  }

}
