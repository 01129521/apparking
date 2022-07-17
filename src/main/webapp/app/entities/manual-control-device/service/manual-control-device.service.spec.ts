import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IManualControlDevice, ManualControlDevice } from '../manual-control-device.model';

import { ManualControlDeviceService } from './manual-control-device.service';

describe('ManualControlDevice Service', () => {
  let service: ManualControlDeviceService;
  let httpMock: HttpTestingController;
  let elemDefault: IManualControlDevice;
  let expectedResult: IManualControlDevice | IManualControlDevice[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ManualControlDeviceService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      device: 'AAAAAAA',
      state: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ManualControlDevice', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ManualControlDevice()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ManualControlDevice', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          device: 'BBBBBB',
          state: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ManualControlDevice', () => {
      const patchObject = Object.assign(
        {
          device: 'BBBBBB',
          state: true,
        },
        new ManualControlDevice()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ManualControlDevice', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          device: 'BBBBBB',
          state: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ManualControlDevice', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addManualControlDeviceToCollectionIfMissing', () => {
      it('should add a ManualControlDevice to an empty array', () => {
        const manualControlDevice: IManualControlDevice = { id: 123 };
        expectedResult = service.addManualControlDeviceToCollectionIfMissing([], manualControlDevice);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(manualControlDevice);
      });

      it('should not add a ManualControlDevice to an array that contains it', () => {
        const manualControlDevice: IManualControlDevice = { id: 123 };
        const manualControlDeviceCollection: IManualControlDevice[] = [
          {
            ...manualControlDevice,
          },
          { id: 456 },
        ];
        expectedResult = service.addManualControlDeviceToCollectionIfMissing(manualControlDeviceCollection, manualControlDevice);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ManualControlDevice to an array that doesn't contain it", () => {
        const manualControlDevice: IManualControlDevice = { id: 123 };
        const manualControlDeviceCollection: IManualControlDevice[] = [{ id: 456 }];
        expectedResult = service.addManualControlDeviceToCollectionIfMissing(manualControlDeviceCollection, manualControlDevice);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(manualControlDevice);
      });

      it('should add only unique ManualControlDevice to an array', () => {
        const manualControlDeviceArray: IManualControlDevice[] = [{ id: 123 }, { id: 456 }, { id: 65506 }];
        const manualControlDeviceCollection: IManualControlDevice[] = [{ id: 123 }];
        expectedResult = service.addManualControlDeviceToCollectionIfMissing(manualControlDeviceCollection, ...manualControlDeviceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const manualControlDevice: IManualControlDevice = { id: 123 };
        const manualControlDevice2: IManualControlDevice = { id: 456 };
        expectedResult = service.addManualControlDeviceToCollectionIfMissing([], manualControlDevice, manualControlDevice2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(manualControlDevice);
        expect(expectedResult).toContain(manualControlDevice2);
      });

      it('should accept null and undefined values', () => {
        const manualControlDevice: IManualControlDevice = { id: 123 };
        expectedResult = service.addManualControlDeviceToCollectionIfMissing([], null, manualControlDevice, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(manualControlDevice);
      });

      it('should return initial array if no ManualControlDevice is added', () => {
        const manualControlDeviceCollection: IManualControlDevice[] = [{ id: 123 }];
        expectedResult = service.addManualControlDeviceToCollectionIfMissing(manualControlDeviceCollection, undefined, null);
        expect(expectedResult).toEqual(manualControlDeviceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
