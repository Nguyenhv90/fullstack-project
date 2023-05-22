import { HttpClient, HttpErrorResponse, HttpEvent } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private host = environment.apiUrl


  constructor(private http: HttpClient) { }

  public getUsers(): Observable<User[] | HttpErrorResponse> {
    return this.http.get<User[]>(`${this.host}/user/list`)
  }

  public save(user: User ): Observable<any> {
    return this.http.post<User>(`${this.host}/user/saveOrUpdate`, user)
  }

  public resetPassword(email: string ): Observable<any> {
    return this.http.post(`${this.host}/user/resetPassword/${email}`, null)
  }

  public updateProfileImage(user: User ): Observable<HttpEvent<any> | HttpErrorResponse> {
    return this.http.post<User>(`${this.host}/user/updateProfileImage`, user,
    {
      reportProgress: true,
      observe: 'events'
    })
  }
}
