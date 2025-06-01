import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PlaylistService, ListaReproduccion } from '../../services/playlist.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-playlist-list',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './playlist-list.component.html',
  styleUrls: ['./playlist-list.component.css']
})
export class PlaylistListComponent implements OnInit {
  playlists: ListaReproduccion[] = [];
  playlistForm: FormGroup;
  errorMessage: string = '';
  successMessage: string = '';
  selectedPlaylist: ListaReproduccion | null = null;

  constructor(
    private playlistService: PlaylistService,
    private fb: FormBuilder,
    public authService: AuthService
  ) {
    this.playlistForm = this.fb.group({
      nombre: ['', Validators.required],
      descripcion: ['']
    });
  }

  ngOnInit(): void {
    this.loadPlaylists();
  }

  loadPlaylists(): void {
    this.clearMessages();
    this.playlistService.getAllPlaylists().subscribe({
      next: (data) => this.playlists = data,
      error: (err) => this.handleLoadError(err)
    });
  }

  onSubmitPlaylist(): void {
    this.clearMessages();
    if (this.playlistForm.invalid) {
      this.errorMessage = 'El nombre es requerido.';
      return;
    }
    
    const newPlaylistData: ListaReproduccion = {
      nombre: this.playlistForm.value.nombre,
      descripcion: this.playlistForm.value.descripcion,
      canciones: []
    };

    this.playlistService.createPlaylist(newPlaylistData).subscribe({
      next: (savedPlaylist) => {
        this.loadPlaylists();
        this.playlistForm.reset();
        this.successMessage = `Playlist '${savedPlaylist.nombre}' creada.`;
      },
      error: (err) => this.handleCreateError(err)
    });
  }

  viewPlaylistDetails(nombrePlaylist: string): void {
    this.clearMessages();
    this.playlistService.getPlaylistByNombre(nombrePlaylist).subscribe({
        next: (data) => this.selectedPlaylist = data,
        error: (err) => {
            console.error('Error obteniendo detalles', err);
            this.errorMessage = 'No se pudo cargar los detalles.';
        }
    });
  }

  closeDetails(): void {
    this.selectedPlaylist = null;
  }

  deletePlaylist(nombrePlaylist: string, event: Event): void {
    event.stopPropagation();
    this.clearMessages();

    if (confirm(`Eliminar playlist '${nombrePlaylist}'?`)) {
      this.playlistService.deletePlaylistByNombre(nombrePlaylist).subscribe({
        next: () => {
          this.loadPlaylists();
          if (this.selectedPlaylist?.nombre === nombrePlaylist) {
            this.selectedPlaylist = null;
          }
          this.successMessage = `Playlist '${nombrePlaylist}' eliminada.`;
        },
        error: (err) => this.handleDeleteError(err, nombrePlaylist)
      });
    }
  }

  private clearMessages(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }

  private handleLoadError(err: any): void {
    console.error('Error cargando playlists', err);
    this.errorMessage = 'Error al cargar listas.';
    if (err.status === 401 || err.status === 403) {
        this.errorMessage = 'No autorizado. Inicia sesion.';
    }
  }

  private handleCreateError(err: any): void {
    console.error('Error creando playlist', err);
    this.errorMessage = err.error?.message || 'Error al crear playlist.';
  }

  private handleDeleteError(err: any, nombrePlaylist: string): void {
    console.error('Error eliminando playlist', err);
    this.errorMessage = `Error al eliminar '${nombrePlaylist}'.`;
    if (err.status === 404) {
        this.errorMessage = `Playlist '${nombrePlaylist}' no encontrada.`;
    }
  }
}