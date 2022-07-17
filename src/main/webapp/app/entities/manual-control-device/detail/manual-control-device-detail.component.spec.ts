import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ManualControlDeviceDetailComponent } from './manual-control-device-detail.component';

describe('ManualControlDevice Management Detail Component', () => {
  let comp: ManualControlDeviceDetailComponent;
  let fixture: ComponentFixture<ManualControlDeviceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ManualControlDeviceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ manualControlDevice: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ManualControlDeviceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ManualControlDeviceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load manualControlDevice on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.manualControlDevice).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
