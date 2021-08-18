import { Component, OnInit } from '@angular/core';
import { HttpEventType, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserService } from '../_services/user.service'
import { TokenStorageService } from '../_services/token-storage.service';

@Component({
  selector: 'app-csv-file',
  templateUrl: './csv-file.component.html',
  styleUrls: ['./csv-file.component.css']
})
export class CsvFileComponent implements OnInit {

  selectedFiles?: FileList;
  currentFile?: File;
  progress = 0;
  message = '';
  fileInfos?: Observable<any>;
  fileDetailsAvailable = false;
  fileDetails = {
    id: null,
    filename: null,
    numbers: null
  };

  constructor(private userService: UserService, private tokenStorageService: TokenStorageService) { }

  ngOnInit(): void {
    this.fileInfos = this.userService.getcsvFiles();
  }

 selectFile(event: any): void {
    this.selectedFiles = event.target.files;
  }


  downloadLink(id: number): String {
    return this.userService.getCsvFileDownloadLink(id) + '?jwt=' + this.tokenStorageService.getToken();
  }

  fileInfo(id: number): void {
    this.userService.getCsvFile(id).subscribe(
      data => {
        console.log(data);
        this.fileDetails = data;
        this.fileDetailsAvailable = true;
      },
      err => {
        console.log(err);
        this.fileDetailsAvailable = false;
      }
    )
          
  }

  upload(): void {
    this.progress = 0;

    if (this.selectedFiles) {
      const file: File | null = this.selectedFiles.item(0);

      if (file) {
        this.currentFile = file;

        this.userService.uploadCsvFile(this.currentFile).subscribe(
          (event: any) => {
            if (event.type === HttpEventType.UploadProgress) {
              this.progress = Math.round(100 * event.loaded / event.total);
            }
            else if (event instanceof HttpResponse) {
              this.message = event.body.message;
              this.fileInfos = this.userService.getcsvFiles();              
            }
          },
          (err: any) => {
            console.log(err);
            this.progress = 0;

            if (err.error && err.error.message) {
              this.message = err.error.message;
            }
            else {
              this.message = 'Could not upload the file!';
            }

            this.currentFile = undefined;
          });

      }

      this.selectedFiles = undefined;
    }
  }

}
