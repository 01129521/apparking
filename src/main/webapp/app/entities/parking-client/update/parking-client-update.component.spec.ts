import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ParkingClientService } from '../service/parking-client.service';
import { IParkingClient, ParkingClient } from '../parking-client.model';

import { ParkingClientUpdateComponent } from './parking-client-update.component';

describe('ParkingClient Management Update Component', () => {
  let comp: ParkingClientUpdateComponent;
  let fixture: ComponentFixture<ParkingClientUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let parkingClientService: ParkingClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ParkingClientUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ParkingClientUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ParkingClientUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    parkingClientService = TestBed.inject(ParkingClientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const parkingClient: IParkingClient = { id: 456 };

      activatedRoute.data = of({ parkingClient });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(parkingClient));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ParkingClient>>();
      const parkingClient = { id: 123 };
      jest.spyOn(parkingClientService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parkingClient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: parkingClient }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(parkingClientService.update).toHaveBeenCalledWith(parkingClient);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ParkingClient>>();
      const parkingClient = new ParkingClient();
      jest.spyOn(parkingClientService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parkingClient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: parkingClient }));
      saveSubject.complete();

      // THEN
      expect(parkingClientService.create).toHaveBeenCalledWith(parkingClient);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ParkingClient>>();
      const parkingClient = { id: 123 };
      jest.spyOn(parkingClientService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parkingClient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(parkingClientService.update).toHaveBeenCalledWith(parkingClient);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
