import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { HeaderType } from 'src/app/enum/header-type.enum';
import { NotificationType } from 'src/app/enum/notification-type.enum';
import { User } from 'src/app/model/user';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { NotificationService } from 'src/app/service/notification.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {
  showLoading!: boolean;
  private subscriptions: Subscription[] = [];

  constructor(
    private authService: AuthenticationService,
    private router: Router,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.router.navigateByUrl('/user/management');
    } else {
      this.router.navigateByUrl('/login');
    }
  }
  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  public onLogin(user: User): void {
    this.showLoading = false;
    this.subscriptions.push(
      this.authService.login(user).subscribe(res => {
          console.log(res);
          const token = res.headers.get(HeaderType.JWT_TOKEN);
          this.authService.saveToken(token || '');
          this.authService.addUserToLocalCache(res.body || new User());
          this.router.navigateByUrl('/user/management');
          this.showLoading = false;
        },
        (error) => {
          const message = "An error occured when login. Please try again!"
          console.log(error);
          this.notificationService.notify(NotificationType.ERROR, error.error.message || message)
        }
      )
    )
  }
}
