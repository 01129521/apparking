import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IManualControlDevice } from '../manual-control-device.model';
import { ManualControlDeviceService } from '../service/manual-control-device.service';

@Component({
  templateUrl: './manual-control-device-delete-dialog.component.html',
})
export class ManualControlDeviceDeleteDialogComponent {
  manualControlDevice?: IManualControlDevice;

  constructor(protected manualControlDeviceService: ManualControlDeviceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.manualControlDeviceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
