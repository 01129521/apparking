import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ParkingClientDetailComponent } from './parking-client-detail.component';

describe('ParkingClient Management Detail Component', () => {
  let comp: ParkingClientDetailComponent;
  let fixture: ComponentFixture<ParkingClientDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ParkingClientDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ parkingClient: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ParkingClientDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ParkingClientDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load parkingClient on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.parkingClient).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
