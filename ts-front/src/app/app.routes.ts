import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { MainComponent } from './main/main.component';
import { RegisterODComponent } from './register-od/register-od.component';
import { RegisterPUPComponent } from './register-pup/register-pup.component';
import { PictureODComponent } from './picture-od/picture-od.component';
import { PicturePUPComponent } from './picture-pup/picture-pup.component';
import { AgendaComponent } from './agenda/agenda.component';

export const routes: Routes = [
    {path: '', redirectTo: 'register-od', pathMatch: 'full'},
    {path: 'login', component: LoginComponent},
    {path: "register-od", component: RegisterODComponent},
    {path: "register-pup", component: RegisterPUPComponent},
    {path: "picture-od", component: PictureODComponent},
    {path: "picture-pup", component: PicturePUPComponent},
    {path: 'main', component: MainComponent},
    {path: 'agenda', component: AgendaComponent}
];
