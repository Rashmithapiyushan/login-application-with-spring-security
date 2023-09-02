import { Component } from '@angular/core';
import { AxiosService } from '../axios.service';

@Component({
  selector: 'app-auth-content',
  templateUrl: './auth-content.component.html',
  styleUrls: ['./auth-content.component.css']
})
export class AuthContentComponent {
  data:string[]=[];
  constructor(private axioService:AxiosService){}
  ngOnInit():void{
    this.axioService.request(
      "GET",
      "/message",
      {}
    ).then(
      (responce)=>{
        this.data=responce.data;
      }
    )
  }

}
