import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AxiosService } from '../axios.service';

@Component({
  selector: 'app-content',
  templateUrl: './content.component.html',
  styleUrls: ['./content.component.css']
})
export class ContentComponent {
  componentToShow:string='welcome';

  constructor(private axiosService:AxiosService){}

  showComponent(componentToShow:string):void{
    this.componentToShow=componentToShow;
     
  }


  onLogin(event:any):void{
    this.axiosService.request(
      "POST",
      "/login",
      {
        login:event.login,
        password:event.password
      }
    ).then(responce=>{
      this.axiosService.setAuthToken(responce.data.token);
      this.componentToShow='messages'
    });
  }

  onRegister(event:any):void{
    this.axiosService.request(
      "POST",
      "/register",
      {
        firstName:event.firstName,
        lastName:event.lastName,
        login:event.login,
        password:event.password
      }
    ).then(responce=>{
      this.axiosService.setAuthToken(responce.data.token);
      this.componentToShow='messages'
    });
  }
}
