import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { LoginService } from '../../app-connexion/app/service/login.service';


@Injectable({
  providedIn: 'root',
})
export class authGuard implements CanActivate {

  constructor(private loginService: LoginService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const expectedRole = route.data['role']; // Rôle attendu pour cette route
    const userRole = this.loginService.getUserRole(); // Rôle de l'utilisateur connecté

    if (userRole && expectedRole.includes(userRole)) {
      return true; // Autoriser l'accès
    } else {
      this.router.navigate(['/login']); // Rediriger si rôle non valide
      return false;
    }
  }
};
