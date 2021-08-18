import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

const API_URL = 'http://localhost:8080/api/app';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient) { }

  uploadCsvFile(file: File): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();

    formData.append('file', file);

    const req = new HttpRequest('POST', API_URL+'/csvFile', formData, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }

  getcsvFiles(): Observable<any> {

    return this.http.get(API_URL+'/csvFile', {responseType: 'json'})

  }

  getCsvFile(id : number): Observable<any> {
    
    return this.http.get(API_URL+'/csvFile/'+id, {responseType: 'json'})

  }

  getCsvFileDownloadLink (id: number): String {

    return API_URL+'/csvFile/download/'+id;

  }

  checkMobileNumber(mobileNumber: string): Observable<any> {
    return this.http.post(
      API_URL+'/mobileNumber',
      {
        mobileNumber
      },
      httpOptions
    )
  }

}