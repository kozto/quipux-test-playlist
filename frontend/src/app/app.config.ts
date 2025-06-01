import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms'; 

import { routes } from './app.routes';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes), // Configura las rutas
    provideHttpClient(withInterceptorsFromDi()), // Configura HttpClient y permite interceptores DI
    
    importProvidersFrom(FormsModule, ReactiveFormsModule), 

    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }

  ]
};