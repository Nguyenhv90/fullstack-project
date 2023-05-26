import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { Subscription } from 'rxjs/internal/Subscription';
import { NotificationType } from 'src/app/enum/notification-type.enum';
import { User } from 'src/app/model/user';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { NotificationService } from 'src/app/service/notification.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit, OnDestroy {
  showLoading!: boolean;
  private subscriptions: Subscription[] = [];
  private titleSubject = new BehaviorSubject<string>('User');
  public titleAction$ = this.titleSubject.asObservable();
  refreshing!: boolean;
  users!: User[];

  constructor(
    private authService: AuthenticationService,
    private router: Router,
    private notificationService: NotificationService,
    private userService: UserService,
  ) { }

  ngOnInit(): void {
    this.getUsers(true);
  }
  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  public changeTitle(title: string): void{
    this.titleSubject.next(title);
  }

  onLogout() {
    this.authService.logout();
    this.router.navigateByUrl("/login");
  }

  getUsers(showNotification: boolean): void {
    this.refreshing = true;
    this.subscriptions.push(
      this.userService.getUsers().subscribe(
        (res) =>{
          this.userService.addUsersToLocalCache(res);
          this.users = res;
          this.refreshing = false;
          if(showNotification) {
            this.notificationService.notify(NotificationType.SUCCESS, `${res?.length} users loaded successlly`)
          }
        },
        (error) =>{
          this.notificationService.notify(NotificationType.ERROR, error.error.message);
          this.refreshing = true;
        }
      )
    )
  }
}
