import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup;
  errorMessage: string = '';
  successMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (this.registerForm.invalid) {
      this.errorMessage = 'Por favor, revisa los campos.';
      return;
    }

    const userData = {
        username: this.registerForm.value.username,
        password: this.registerForm.value.password,
        roles: ['ROLE_USER']
    };

    this.authService.register(userData).subscribe({
      next: () => {
        this.successMessage = 'Usuario registrado! Ahora puedes iniciar sesion.';
        this.registerForm.reset();
        // Opcional: redirigir a login
        // setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (err) => {
        console.error('Error en registro:', err);
        if (err.error && typeof err.error === 'string') {
             this.errorMessage = err.error;
        } else if (err.status === 400) {
          this.errorMessage = 'Datos invalidos o el usuario ya existe.';
        } else {
          this.errorMessage = 'Error al registrar. Intenta mas tarde.';
        }
      }
    });
  }
}