import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IManualControlDevice } from '../manual-control-device.model';

@Component({
  selector: 'jhi-manual-control-device-detail',
  templateUrl: './manual-control-device-detail.component.html',
})
export class ManualControlDeviceDetailComponent implements OnInit {
  manualControlDevice: IManualControlDevice | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ manualControlDevice }) => {
      this.manualControlDevice = manualControlDevice;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
