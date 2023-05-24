import { Injectable, inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from '@angular/router';
import { AuthenticationService } from './authentication.service';
import { NotificationService } from './notification.service';
import { NotificationType } from '../enum/notification-type.enum';

@Injectable({
  providedIn: 'root'
})
export class PermissionService {

  constructor(
    private authenticationService: AuthenticationService, 
    private router: Router,
    private notificationService: NotificationService,
    ) { }
  
  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    return true;
  }

  private isUserLoggedIn(): boolean {
    if(this.authenticationService.isLoggedIn()) {
      return true;
    }
    this.router.navigate(['/login']);
    this.notificationService.notify(NotificationType.ERROR, `You need to login to access this page`.toUpperCase())
    return false;
  }


}
export const AuthGuard: CanActivateFn = (next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean => {
  return inject(PermissionService).canActivate(next, state);
}
