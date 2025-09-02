import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserOD } from './models/user-od';
import { Observable } from 'rxjs/internal/Observable';
import { UserPUP } from './models/user-pup';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  private baseUrl = 'http://localhost:8080/api/user';

  constructor(private http: HttpClient) { }

  registerOD(user: UserOD): Observable<any> {
    return this.http.post(`${this.baseUrl}/registration/`, user, {responseType: 'text'});
  }

  registerPUP(user: UserPUP): Observable<any> {
    return this.http.post(`${this.baseUrl}/registration/pup`, user, {responseType: 'text'});
  }
}
