import {Routes} from '@angular/router';
import {LandingPageComponent} from "./landing-page/landing-page/landing-page.component";
import {
  CRUDTipoContratacionesComponent
} from "./CRUD-TipoContrataciones/crud-tipo-contrataciones/crud-tipo-contrataciones.component";
import {NgModule} from "@angular/core";
import {AppComponent} from "./app.component";
import {CRUDDepartamentosComponent} from "./CRUD-Departamentos/crud-departamentos/crud-departamentos.component";
import {CRUDCargoComponent} from "./CRUD-Cargo/crud-cargo/crud-cargo.component";

export const routes: Routes = [
  {
    path:'',
    component: AppComponent,
    children:[
      {path:'', redirectTo: 'landing-page', pathMatch: 'full'},
      {path: 'landing-page',             component: LandingPageComponent},
      {path: 'crud-tipo-contrataciones', component: CRUDTipoContratacionesComponent},
      {path: 'crud-departamentos', component: CRUDDepartamentosComponent},
      {path: 'crud-cargo', component: CRUDCargoComponent},
    ]
  }
];
