import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { PlaylistListComponent } from './components/playlist-list/playlist-list.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'playlists',
    component: PlaylistListComponent,
    canActivate: [AuthGuard] 
  },
  { path: '', redirectTo: '/playlists', pathMatch: 'full' },
  { path: '**', redirectTo: '/playlists' }
];