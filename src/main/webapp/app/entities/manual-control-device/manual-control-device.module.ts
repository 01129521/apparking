import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ManualControlDeviceComponent } from './list/manual-control-device.component';
import { ManualControlDeviceDetailComponent } from './detail/manual-control-device-detail.component';
import { ManualControlDeviceUpdateComponent } from './update/manual-control-device-update.component';
import { ManualControlDeviceDeleteDialogComponent } from './delete/manual-control-device-delete-dialog.component';
import { ManualControlDeviceRoutingModule } from './route/manual-control-device-routing.module';

@NgModule({
  imports: [SharedModule, ManualControlDeviceRoutingModule],
  declarations: [
    ManualControlDeviceComponent,
    ManualControlDeviceDetailComponent,
    ManualControlDeviceUpdateComponent,
    ManualControlDeviceDeleteDialogComponent,
  ],
  entryComponents: [ManualControlDeviceDeleteDialogComponent],
})
export class ManualControlDeviceModule {}
