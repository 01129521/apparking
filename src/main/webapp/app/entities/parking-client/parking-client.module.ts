import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ParkingClientComponent } from './list/parking-client.component';
import { ParkingClientDetailComponent } from './detail/parking-client-detail.component';
import { ParkingClientUpdateComponent } from './update/parking-client-update.component';
import { ParkingClientDeleteDialogComponent } from './delete/parking-client-delete-dialog.component';
import { ParkingClientRoutingModule } from './route/parking-client-routing.module';

@NgModule({
  imports: [SharedModule, ParkingClientRoutingModule],
  declarations: [ParkingClientComponent, ParkingClientDetailComponent, ParkingClientUpdateComponent, ParkingClientDeleteDialogComponent],
  entryComponents: [ParkingClientDeleteDialogComponent],
})
export class ParkingClientModule {}
