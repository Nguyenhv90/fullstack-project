import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { NotificationType } from 'src/app/enum/notification-type.enum';
import { User } from 'src/app/model/user';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { NotificationService } from 'src/app/service/notification.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit, OnDestroy {
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
    }
  }
  ngOnDestroy(): void {
  }

  onRegister(user: User) {
    this.showLoading = false;
    this.subscriptions.push(
      this.authService.register(user).subscribe(res => {
          console.log(res);
          this.notificationService.notify(NotificationType.SUCCESS, "Register success" )
          this.router.navigateByUrl('/login');
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
