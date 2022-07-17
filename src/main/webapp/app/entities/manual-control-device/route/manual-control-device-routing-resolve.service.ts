import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IManualControlDevice, ManualControlDevice } from '../manual-control-device.model';
import { ManualControlDeviceService } from '../service/manual-control-device.service';

@Injectable({ providedIn: 'root' })
export class ManualControlDeviceRoutingResolveService implements Resolve<IManualControlDevice> {
  constructor(protected service: ManualControlDeviceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IManualControlDevice> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((manualControlDevice: HttpResponse<ManualControlDevice>) => {
          if (manualControlDevice.body) {
            return of(manualControlDevice.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ManualControlDevice());
  }
}
