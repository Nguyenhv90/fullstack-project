import { HttpClient, HttpErrorResponse, HttpEvent } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { User } from '../model/user';

@Injectable({providedIn: 'root'})
export class UserService {
  private host = environment.apiUrl


  constructor(private http: HttpClient) { }

  public getUsers(): Observable<any> {
    return this.http.get<User[]>(`${this.host}/user/search`,{})
  }

  public save(user: User): Observable<any> {
    return this.http.post<User>(`${this.host}/user/saveOrUpdate`, user)
  }

  public resetPassword(email: string ): Observable<any> {
    return this.http.post(`${this.host}/user/resetPassword/${email}`, null)
  }

  public updateProfileImage(user: User): Observable<HttpEvent<any> | HttpErrorResponse> {
    return this.http.post<User>(`${this.host}/user/updateProfileImage`, user,
    {
      reportProgress: true,
      observe: 'events'
    })
  }

  public delete(userId: number): Observable<any> {
    return this.http.delete(`${this.host}/user/delete/${userId}`)
  }

  public addUsersToLocalCache(users: User[]): void {
    localStorage.setItem('users', JSON.stringify(users));
  }

  public getUsersFromLocalCache(): User[] {
    if(localStorage.getItem('users')) {
      return JSON.parse(localStorage.getItem('users') || '[]');
    }
    return [];
  }

  public createUserFormData(loggedInUsername: string, user: User, profileImage: File): FormData {
    const formData = new FormData();
    formData.append('currentUserName', loggedInUsername);
    formData.append('firstName', user.firstName);
    formData.append('lastName', user.lastName);
    formData.append('username', user.username);
    formData.append('email', user.email);
    formData.append('profileImage', profileImage);
    return formData;
  }
  
}
