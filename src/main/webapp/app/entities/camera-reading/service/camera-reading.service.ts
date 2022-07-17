import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICameraReading, getCameraReadingIdentifier } from '../camera-reading.model';

export type EntityResponseType = HttpResponse<ICameraReading>;
export type EntityArrayResponseType = HttpResponse<ICameraReading[]>;

@Injectable({ providedIn: 'root' })
export class CameraReadingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/camera-readings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICameraReading>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICameraReading[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  addCameraReadingToCollectionIfMissing(
    cameraReadingCollection: ICameraReading[],
    ...cameraReadingsToCheck: (ICameraReading | null | undefined)[]
  ): ICameraReading[] {
    const cameraReadings: ICameraReading[] = cameraReadingsToCheck.filter(isPresent);
    if (cameraReadings.length > 0) {
      const cameraReadingCollectionIdentifiers = cameraReadingCollection.map(
        cameraReadingItem => getCameraReadingIdentifier(cameraReadingItem)!
      );
      const cameraReadingsToAdd = cameraReadings.filter(cameraReadingItem => {
        const cameraReadingIdentifier = getCameraReadingIdentifier(cameraReadingItem);
        if (cameraReadingIdentifier == null || cameraReadingCollectionIdentifiers.includes(cameraReadingIdentifier)) {
          return false;
        }
        cameraReadingCollectionIdentifiers.push(cameraReadingIdentifier);
        return true;
      });
      return [...cameraReadingsToAdd, ...cameraReadingCollection];
    }
    return cameraReadingCollection;
  }
}
