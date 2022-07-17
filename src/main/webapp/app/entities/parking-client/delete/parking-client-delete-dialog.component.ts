import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IParkingClient } from '../parking-client.model';
import { ParkingClientService } from '../service/parking-client.service';

@Component({
  templateUrl: './parking-client-delete-dialog.component.html',
})
export class ParkingClientDeleteDialogComponent {
  parkingClient?: IParkingClient;

  constructor(protected parkingClientService: ParkingClientService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.parkingClientService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
