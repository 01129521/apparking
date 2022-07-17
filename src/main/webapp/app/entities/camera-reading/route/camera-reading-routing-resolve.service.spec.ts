import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICameraReading, CameraReading } from '../camera-reading.model';
import { CameraReadingService } from '../service/camera-reading.service';

import { CameraReadingRoutingResolveService } from './camera-reading-routing-resolve.service';

describe('CameraReading routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CameraReadingRoutingResolveService;
  let service: CameraReadingService;
  let resultCameraReading: ICameraReading | undefined;

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
    routingResolveService = TestBed.inject(CameraReadingRoutingResolveService);
    service = TestBed.inject(CameraReadingService);
    resultCameraReading = undefined;
  });

  describe('resolve', () => {
    it('should return ICameraReading returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCameraReading = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCameraReading).toEqual({ id: 123 });
    });

    it('should return new ICameraReading if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCameraReading = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCameraReading).toEqual(new CameraReading());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CameraReading })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCameraReading = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCameraReading).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
