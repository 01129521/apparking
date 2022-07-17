import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IParkingClient, ParkingClient } from '../parking-client.model';

import { ParkingClientService } from './parking-client.service';

describe('ParkingClient Service', () => {
  let service: ParkingClientService;
  let httpMock: HttpTestingController;
  let elemDefault: IParkingClient;
  let expectedResult: IParkingClient | IParkingClient[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ParkingClientService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      firstName: 'AAAAAAA',
      lastName: 'AAAAAAA',
      phoneNumber: 'AAAAAAA',
      licensePlateNumbers: 'AAAAAAA',
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

    it('should create a ParkingClient', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ParkingClient()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ParkingClient', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          firstName: 'BBBBBB',
          lastName: 'BBBBBB',
          phoneNumber: 'BBBBBB',
          licensePlateNumbers: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ParkingClient', () => {
      const patchObject = Object.assign(
        {
          firstName: 'BBBBBB',
          lastName: 'BBBBBB',
          licensePlateNumbers: 'BBBBBB',
        },
        new ParkingClient()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ParkingClient', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          firstName: 'BBBBBB',
          lastName: 'BBBBBB',
          phoneNumber: 'BBBBBB',
          licensePlateNumbers: 'BBBBBB',
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

    it('should delete a ParkingClient', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addParkingClientToCollectionIfMissing', () => {
      it('should add a ParkingClient to an empty array', () => {
        const parkingClient: IParkingClient = { id: 123 };
        expectedResult = service.addParkingClientToCollectionIfMissing([], parkingClient);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(parkingClient);
      });

      it('should not add a ParkingClient to an array that contains it', () => {
        const parkingClient: IParkingClient = { id: 123 };
        const parkingClientCollection: IParkingClient[] = [
          {
            ...parkingClient,
          },
          { id: 456 },
        ];
        expectedResult = service.addParkingClientToCollectionIfMissing(parkingClientCollection, parkingClient);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ParkingClient to an array that doesn't contain it", () => {
        const parkingClient: IParkingClient = { id: 123 };
        const parkingClientCollection: IParkingClient[] = [{ id: 456 }];
        expectedResult = service.addParkingClientToCollectionIfMissing(parkingClientCollection, parkingClient);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(parkingClient);
      });

      it('should add only unique ParkingClient to an array', () => {
        const parkingClientArray: IParkingClient[] = [{ id: 123 }, { id: 456 }, { id: 52153 }];
        const parkingClientCollection: IParkingClient[] = [{ id: 123 }];
        expectedResult = service.addParkingClientToCollectionIfMissing(parkingClientCollection, ...parkingClientArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const parkingClient: IParkingClient = { id: 123 };
        const parkingClient2: IParkingClient = { id: 456 };
        expectedResult = service.addParkingClientToCollectionIfMissing([], parkingClient, parkingClient2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(parkingClient);
        expect(expectedResult).toContain(parkingClient2);
      });

      it('should accept null and undefined values', () => {
        const parkingClient: IParkingClient = { id: 123 };
        expectedResult = service.addParkingClientToCollectionIfMissing([], null, parkingClient, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(parkingClient);
      });

      it('should return initial array if no ParkingClient is added', () => {
        const parkingClientCollection: IParkingClient[] = [{ id: 123 }];
        expectedResult = service.addParkingClientToCollectionIfMissing(parkingClientCollection, undefined, null);
        expect(expectedResult).toEqual(parkingClientCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
