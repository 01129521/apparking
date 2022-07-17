import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IManualControlDevice, getManualControlDeviceIdentifier } from '../manual-control-device.model';

export type EntityResponseType = HttpResponse<IManualControlDevice>;
export type EntityArrayResponseType = HttpResponse<IManualControlDevice[]>;

@Injectable({ providedIn: 'root' })
export class ManualControlDeviceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/manual-control-devices');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(manualControlDevice: IManualControlDevice): Observable<EntityResponseType> {
    return this.http.post<IManualControlDevice>(this.resourceUrl, manualControlDevice, { observe: 'response' });
  }

  update(manualControlDevice: IManualControlDevice): Observable<EntityResponseType> {
    return this.http.put<IManualControlDevice>(
      `${this.resourceUrl}/${getManualControlDeviceIdentifier(manualControlDevice) as number}`,
      manualControlDevice,
      { observe: 'response' }
    );
  }

  partialUpdate(manualControlDevice: IManualControlDevice): Observable<EntityResponseType> {
    return this.http.patch<IManualControlDevice>(
      `${this.resourceUrl}/${getManualControlDeviceIdentifier(manualControlDevice) as number}`,
      manualControlDevice,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IManualControlDevice>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IManualControlDevice[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addManualControlDeviceToCollectionIfMissing(
    manualControlDeviceCollection: IManualControlDevice[],
    ...manualControlDevicesToCheck: (IManualControlDevice | null | undefined)[]
  ): IManualControlDevice[] {
    const manualControlDevices: IManualControlDevice[] = manualControlDevicesToCheck.filter(isPresent);
    if (manualControlDevices.length > 0) {
      const manualControlDeviceCollectionIdentifiers = manualControlDeviceCollection.map(
        manualControlDeviceItem => getManualControlDeviceIdentifier(manualControlDeviceItem)!
      );
      const manualControlDevicesToAdd = manualControlDevices.filter(manualControlDeviceItem => {
        const manualControlDeviceIdentifier = getManualControlDeviceIdentifier(manualControlDeviceItem);
        if (manualControlDeviceIdentifier == null || manualControlDeviceCollectionIdentifiers.includes(manualControlDeviceIdentifier)) {
          return false;
        }
        manualControlDeviceCollectionIdentifiers.push(manualControlDeviceIdentifier);
        return true;
      });
      return [...manualControlDevicesToAdd, ...manualControlDeviceCollection];
    }
    return manualControlDeviceCollection;
  }
}
