import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ManualControlDeviceService } from '../service/manual-control-device.service';
import { IManualControlDevice, ManualControlDevice } from '../manual-control-device.model';

import { ManualControlDeviceUpdateComponent } from './manual-control-device-update.component';

describe('ManualControlDevice Management Update Component', () => {
  let comp: ManualControlDeviceUpdateComponent;
  let fixture: ComponentFixture<ManualControlDeviceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let manualControlDeviceService: ManualControlDeviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ManualControlDeviceUpdateComponent],
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
      .overrideTemplate(ManualControlDeviceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ManualControlDeviceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    manualControlDeviceService = TestBed.inject(ManualControlDeviceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const manualControlDevice: IManualControlDevice = { id: 456 };

      activatedRoute.data = of({ manualControlDevice });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(manualControlDevice));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ManualControlDevice>>();
      const manualControlDevice = { id: 123 };
      jest.spyOn(manualControlDeviceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manualControlDevice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: manualControlDevice }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(manualControlDeviceService.update).toHaveBeenCalledWith(manualControlDevice);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ManualControlDevice>>();
      const manualControlDevice = new ManualControlDevice();
      jest.spyOn(manualControlDeviceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manualControlDevice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: manualControlDevice }));
      saveSubject.complete();

      // THEN
      expect(manualControlDeviceService.create).toHaveBeenCalledWith(manualControlDevice);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ManualControlDevice>>();
      const manualControlDevice = { id: 123 };
      jest.spyOn(manualControlDeviceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manualControlDevice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(manualControlDeviceService.update).toHaveBeenCalledWith(manualControlDevice);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
