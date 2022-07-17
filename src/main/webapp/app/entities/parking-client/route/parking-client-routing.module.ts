import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ParkingClientComponent } from '../list/parking-client.component';
import { ParkingClientDetailComponent } from '../detail/parking-client-detail.component';
import { ParkingClientUpdateComponent } from '../update/parking-client-update.component';
import { ParkingClientRoutingResolveService } from './parking-client-routing-resolve.service';

const parkingClientRoute: Routes = [
  {
    path: '',
    component: ParkingClientComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ParkingClientDetailComponent,
    resolve: {
      parkingClient: ParkingClientRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ParkingClientUpdateComponent,
    resolve: {
      parkingClient: ParkingClientRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ParkingClientUpdateComponent,
    resolve: {
      parkingClient: ParkingClientRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(parkingClientRoute)],
  exports: [RouterModule],
})
export class ParkingClientRoutingModule {}
