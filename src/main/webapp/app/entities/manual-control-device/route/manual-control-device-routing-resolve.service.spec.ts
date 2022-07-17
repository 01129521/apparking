import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IManualControlDevice, ManualControlDevice } from '../manual-control-device.model';
import { ManualControlDeviceService } from '../service/manual-control-device.service';

import { ManualControlDeviceRoutingResolveService } from './manual-control-device-routing-resolve.service';

describe('ManualControlDevice routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ManualControlDeviceRoutingResolveService;
  let service: ManualControlDeviceService;
  let resultManualControlDevice: IManualControlDevice | undefined;

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
    routingResolveService = TestBed.inject(ManualControlDeviceRoutingResolveService);
    service = TestBed.inject(ManualControlDeviceService);
    resultManualControlDevice = undefined;
  });

  describe('resolve', () => {
    it('should return IManualControlDevice returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultManualControlDevice = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultManualControlDevice).toEqual({ id: 123 });
    });

    it('should return new IManualControlDevice if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultManualControlDevice = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultManualControlDevice).toEqual(new ManualControlDevice());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ManualControlDevice })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultManualControlDevice = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultManualControlDevice).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
