import { Component, OnInit } from '@angular/core';
import { LogoutService } from './services/logout.service';
import { MatomoInjector } from 'ngx-matomo';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {

  // Don't remove matomoInjector though it says it is unused 
  constructor(private matomoInjector: MatomoInjector, private logoutService: LogoutService) {
  }


  ngOnInit(): void {

  }
  logout(event) {
    if (event == 'timeout') {
      this.logoutService.sessionTimeout();
    } else {
      this.logoutService.logout();
    }
  }
  keepAlive() {
    this.logoutService.keepAlive().subscribe();
  }
}
