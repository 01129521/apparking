import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ManualControlDeviceComponent } from '../list/manual-control-device.component';
import { ManualControlDeviceDetailComponent } from '../detail/manual-control-device-detail.component';
import { ManualControlDeviceUpdateComponent } from '../update/manual-control-device-update.component';
import { ManualControlDeviceRoutingResolveService } from './manual-control-device-routing-resolve.service';

const manualControlDeviceRoute: Routes = [
  {
    path: '',
    component: ManualControlDeviceComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ManualControlDeviceDetailComponent,
    resolve: {
      manualControlDevice: ManualControlDeviceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ManualControlDeviceUpdateComponent,
    resolve: {
      manualControlDevice: ManualControlDeviceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ManualControlDeviceUpdateComponent,
    resolve: {
      manualControlDevice: ManualControlDeviceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(manualControlDeviceRoute)],
  exports: [RouterModule],
})
export class ManualControlDeviceRoutingModule {}
