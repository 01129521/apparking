import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IParkingClient, ParkingClient } from '../parking-client.model';
import { ParkingClientService } from '../service/parking-client.service';

import { ParkingClientRoutingResolveService } from './parking-client-routing-resolve.service';

describe('ParkingClient routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ParkingClientRoutingResolveService;
  let service: ParkingClientService;
  let resultParkingClient: IParkingClient | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(ParkingClientRoutingResolveService);
    service = TestBed.inject(ParkingClientService);
    resultParkingClient = undefined;
  });

  describe('resolve', () => {
    it('should return IParkingClient returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultParkingClient = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultParkingClient).toEqual({ id: 123 });
    });

    it('should return new IParkingClient if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultParkingClient = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultParkingClient).toEqual(new ParkingClient());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ParkingClient })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultParkingClient = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultParkingClient).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
