import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { MainComponent } from './main/main.component';
import { RegisterODComponent } from './register-od/register-od.component';

export const routes: Routes = [
    {path: '', redirectTo: 'login', pathMatch: 'full'},
    {path: 'login', component: LoginComponent},
    {path: "register-od", component: RegisterODComponent},
    {path: 'main', component: MainComponent}
];
