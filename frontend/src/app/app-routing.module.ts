import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { PlaylistListComponent } from './components/playlist-list/playlist-list.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { 
    path: 'playlists', 
    component: PlaylistListComponent,
    canActivate: [AuthGuard] // Protege esta ruta
  },
  { path: '', redirectTo: '/playlists', pathMatch: 'full' }, // Redirige al inicio
  { path: '**', redirectTo: '/playlists' } // Ruta wildcard para paginas no encontradas
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

