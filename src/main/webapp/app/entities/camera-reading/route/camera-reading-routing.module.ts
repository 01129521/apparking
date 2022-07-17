import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CameraReadingComponent } from '../list/camera-reading.component';
import { CameraReadingDetailComponent } from '../detail/camera-reading-detail.component';
import { CameraReadingRoutingResolveService } from './camera-reading-routing-resolve.service';

const cameraReadingRoute: Routes = [
  {
    path: '',
    component: CameraReadingComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CameraReadingDetailComponent,
    resolve: {
      cameraReading: CameraReadingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cameraReadingRoute)],
  exports: [RouterModule],
})
export class CameraReadingRoutingModule {}
