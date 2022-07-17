import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICameraReading, CameraReading } from '../camera-reading.model';
import { CameraReadingService } from '../service/camera-reading.service';

@Injectable({ providedIn: 'root' })
export class CameraReadingRoutingResolveService implements Resolve<ICameraReading> {
  constructor(protected service: CameraReadingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICameraReading> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cameraReading: HttpResponse<CameraReading>) => {
          if (cameraReading.body) {
            return of(cameraReading.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CameraReading());
  }
}
