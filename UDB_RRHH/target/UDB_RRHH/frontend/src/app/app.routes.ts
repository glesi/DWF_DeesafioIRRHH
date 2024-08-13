import { Routes } from '@angular/router';
import {LandingPageComponent} from "./landing-page/landing-page/landing-page.component";

export const routes: Routes = [
  {
    path:'',
    redirectTo: 'landing-page',
    pathMatch: 'full',
  },
  {
    path: 'landing-page',
    component: LandingPageComponent,
  }

];
