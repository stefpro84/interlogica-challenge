import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-mobile-number',
  templateUrl: './mobile-number.component.html',
  styleUrls: ['./mobile-number.component.css']
})
export class MobileNumberComponent implements OnInit {

  form: any = {
    mobileNumber: null
  };
  isSuccessful = false;
  isMobileNumberCheckFailed = false;
  errorMessage = '';
  checkResultAvailable = false;
  checkResult = {};

  constructor(private userService: UserService) { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    console.log(this.form);
    const { mobileNumber } = this.form;

    this.userService.checkMobileNumber(mobileNumber).subscribe(
      data => {
        console.log(data);
        this.isSuccessful = true;
        this.isMobileNumberCheckFailed = false;
        this.checkResult = data;
        this.checkResultAvailable = true;
      },
      (err:any) => {
        console.log(err);
        this.checkResultAvailable = false;
      }
    );
  }

}
