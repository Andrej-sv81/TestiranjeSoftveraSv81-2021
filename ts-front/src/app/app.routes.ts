import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { MainComponent } from './main/main.component';
import { RegisterODComponent } from './register-od/register-od.component';
import { RegisterPUPComponent } from './register-pup/register-pup.component';

export const routes: Routes = [
    {path: '', redirectTo: 'login', pathMatch: 'full'},
    {path: 'login', component: LoginComponent},
    {path: "register-od", component: RegisterODComponent},
    {path: "register-pup", component: RegisterPUPComponent},
    {path: 'main', component: MainComponent}
];
