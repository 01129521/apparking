import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IParkingClient, getParkingClientIdentifier } from '../parking-client.model';

export type EntityResponseType = HttpResponse<IParkingClient>;
export type EntityArrayResponseType = HttpResponse<IParkingClient[]>;

@Injectable({ providedIn: 'root' })
export class ParkingClientService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/parking-clients');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(parkingClient: IParkingClient): Observable<EntityResponseType> {
    return this.http.post<IParkingClient>(this.resourceUrl, parkingClient, { observe: 'response' });
  }

  update(parkingClient: IParkingClient): Observable<EntityResponseType> {
    return this.http.put<IParkingClient>(`${this.resourceUrl}/${getParkingClientIdentifier(parkingClient) as number}`, parkingClient, {
      observe: 'response',
    });
  }

  partialUpdate(parkingClient: IParkingClient): Observable<EntityResponseType> {
    return this.http.patch<IParkingClient>(`${this.resourceUrl}/${getParkingClientIdentifier(parkingClient) as number}`, parkingClient, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IParkingClient>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IParkingClient[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addParkingClientToCollectionIfMissing(
    parkingClientCollection: IParkingClient[],
    ...parkingClientsToCheck: (IParkingClient | null | undefined)[]
  ): IParkingClient[] {
    const parkingClients: IParkingClient[] = parkingClientsToCheck.filter(isPresent);
    if (parkingClients.length > 0) {
      const parkingClientCollectionIdentifiers = parkingClientCollection.map(
        parkingClientItem => getParkingClientIdentifier(parkingClientItem)!
      );
      const parkingClientsToAdd = parkingClients.filter(parkingClientItem => {
        const parkingClientIdentifier = getParkingClientIdentifier(parkingClientItem);
        if (parkingClientIdentifier == null || parkingClientCollectionIdentifiers.includes(parkingClientIdentifier)) {
          return false;
        }
        parkingClientCollectionIdentifiers.push(parkingClientIdentifier);
        return true;
      });
      return [...parkingClientsToAdd, ...parkingClientCollection];
    }
    return parkingClientCollection;
  }
}
