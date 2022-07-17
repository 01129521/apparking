import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IParkingClient, ParkingClient } from '../parking-client.model';
import { ParkingClientService } from '../service/parking-client.service';

@Injectable({ providedIn: 'root' })
export class ParkingClientRoutingResolveService implements Resolve<IParkingClient> {
  constructor(protected service: ParkingClientService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IParkingClient> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((parkingClient: HttpResponse<ParkingClient>) => {
          if (parkingClient.body) {
            return of(parkingClient.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ParkingClient());
  }
}
