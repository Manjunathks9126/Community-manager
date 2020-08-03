import { ErrorHandler, Injectable, Injector } from '@angular/core';
import { Router } from '@angular/router';

@Injectable()
export class SystemErrorHandler implements ErrorHandler {

  private router: Router;
  injector: Injector;

  constructor(injector: Injector) {
    this.injector = injector;
  }

  handleError(error) {
    if (error) {
      console.log(error)
      this.router = this.injector.get(Router);
      this.router.navigate(['/errors']);
    }
  }



}
