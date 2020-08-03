import { Injectable } from '@angular/core';
import {
    CanActivate, Router,
    ActivatedRouteSnapshot,
    RouterStateSnapshot,
    CanLoad, Route
} from '@angular/router';

import { WorkflowTpMsgService } from './workflowtpmsg.service';

@Injectable()
export class WorkflowTpMsgGuard implements CanActivate {
    constructor(private router: Router, private workflowService: WorkflowTpMsgService) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        let path: string = route.routeConfig.path;

        return this.verifyWorkFlow(path);
    }

    verifyWorkFlow(path) : boolean {
        // If any of the previous steps is invalid, go back to the first invalid step
        let firstPath = this.workflowService.getFirstInvalidStep(path,this.router);
        if (firstPath.length > 0) {
            let url = `/${firstPath}`;
            this.router.navigate([url]);
            return false;
        };

        return true;
    }
}


