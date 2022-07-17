import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'parking-client',
        data: { pageTitle: 'apparkingApp.parkingClient.home.title' },
        loadChildren: () => import('./parking-client/parking-client.module').then(m => m.ParkingClientModule),
      },
      {
        path: 'camera-reading',
        data: { pageTitle: 'apparkingApp.cameraReading.home.title' },
        loadChildren: () => import('./camera-reading/camera-reading.module').then(m => m.CameraReadingModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
