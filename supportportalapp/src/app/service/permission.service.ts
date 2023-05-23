import { Injectable, inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from '@angular/router';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class PermissionService {

  constructor(private authenticationService: AuthenticationService, private router: Router) { }
  
  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    return true;
  }

  private isUserLoggedIn(): boolean {
    if(this.authenticationService.isLoggedIn()) {
      return true;
    }
    this.router.navigate(['/login']);
    //Send notification to user
    return false;
  }


}
export const AuthGuard: CanActivateFn = (next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean => {
  return inject(PermissionService).canActivate(next, state);
}
