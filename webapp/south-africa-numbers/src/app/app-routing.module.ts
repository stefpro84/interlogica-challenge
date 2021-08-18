import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { CsvFileComponent } from './csv-file/csv-file.component';
import { MobileNumberComponent } from './mobile-number/mobile-number.component';

const routes: Routes = [
  { path: 'csvFile', component: CsvFileComponent },
  { path: 'mobileNumber', component: MobileNumberComponent },  
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: '', redirectTo: 'home', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash:true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }