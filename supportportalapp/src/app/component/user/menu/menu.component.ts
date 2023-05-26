import { Component } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'header-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent {
  private titleSubject = new BehaviorSubject<string>('User');
  public titleAction$ = this.titleSubject.asObservable();

  constructor() {}

  public changeTitle(title: string): void{
    this.titleSubject.next(title);
  }
}
