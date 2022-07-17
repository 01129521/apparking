import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICameraReading } from '../camera-reading.model';

import { CameraReadingService } from './camera-reading.service';

describe('CameraReading Service', () => {
  let service: CameraReadingService;
  let httpMock: HttpTestingController;
  let elemDefault: ICameraReading;
  let expectedResult: ICameraReading | ICameraReading[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CameraReadingService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      cameraReadingDate: 'AAAAAAA',
      licensePlateNumbers: 'AAAAAAA',
      licensePlatePhotoContentType: 'image/png',
      licensePlatePhoto: 'AAAAAAA',
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

    it('should return a list of CameraReading', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cameraReadingDate: 'BBBBBB',
          licensePlateNumbers: 'BBBBBB',
          licensePlatePhoto: 'BBBBBB',
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

    describe('addCameraReadingToCollectionIfMissing', () => {
      it('should add a CameraReading to an empty array', () => {
        const cameraReading: ICameraReading = { id: 123 };
        expectedResult = service.addCameraReadingToCollectionIfMissing([], cameraReading);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cameraReading);
      });

      it('should not add a CameraReading to an array that contains it', () => {
        const cameraReading: ICameraReading = { id: 123 };
        const cameraReadingCollection: ICameraReading[] = [
          {
            ...cameraReading,
          },
          { id: 456 },
        ];
        expectedResult = service.addCameraReadingToCollectionIfMissing(cameraReadingCollection, cameraReading);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CameraReading to an array that doesn't contain it", () => {
        const cameraReading: ICameraReading = { id: 123 };
        const cameraReadingCollection: ICameraReading[] = [{ id: 456 }];
        expectedResult = service.addCameraReadingToCollectionIfMissing(cameraReadingCollection, cameraReading);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cameraReading);
      });

      it('should add only unique CameraReading to an array', () => {
        const cameraReadingArray: ICameraReading[] = [{ id: 123 }, { id: 456 }, { id: 70725 }];
        const cameraReadingCollection: ICameraReading[] = [{ id: 123 }];
        expectedResult = service.addCameraReadingToCollectionIfMissing(cameraReadingCollection, ...cameraReadingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cameraReading: ICameraReading = { id: 123 };
        const cameraReading2: ICameraReading = { id: 456 };
        expectedResult = service.addCameraReadingToCollectionIfMissing([], cameraReading, cameraReading2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cameraReading);
        expect(expectedResult).toContain(cameraReading2);
      });

      it('should accept null and undefined values', () => {
        const cameraReading: ICameraReading = { id: 123 };
        expectedResult = service.addCameraReadingToCollectionIfMissing([], null, cameraReading, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cameraReading);
      });

      it('should return initial array if no CameraReading is added', () => {
        const cameraReadingCollection: ICameraReading[] = [{ id: 123 }];
        expectedResult = service.addCameraReadingToCollectionIfMissing(cameraReadingCollection, undefined, null);
        expect(expectedResult).toEqual(cameraReadingCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
