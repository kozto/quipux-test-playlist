import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Cancion {
  id?: number;
  titulo: string;
  artista: string;
  album: string;
  anno: number;
  genero: string;
}

export interface ListaReproduccion {
  id?: number;
  nombre: string;
  descripcion?: string;
  canciones?: Cancion[];
}

@Injectable({
  providedIn: 'root'
})
export class PlaylistService {
  private apiUrl = 'http://localhost:8080/quipux-test-playlist/lists'; 

  constructor(private http: HttpClient) { }

  getAllPlaylists(): Observable<ListaReproduccion[]> {
    return this.http.get<ListaReproduccion[]>(this.apiUrl);
  }

  getPlaylistByNombre(nombre: string): Observable<ListaReproduccion> {
    return this.http.get<ListaReproduccion>(`${this.apiUrl}/${nombre}`);
  }

  createPlaylist(playlist: ListaReproduccion): Observable<ListaReproduccion> {
    return this.http.post<ListaReproduccion>(this.apiUrl, playlist);
  }

  deletePlaylistByNombre(nombre: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${nombre}`);
  }
}
