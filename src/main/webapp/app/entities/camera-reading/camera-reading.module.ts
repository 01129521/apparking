import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CameraReadingComponent } from './list/camera-reading.component';
import { CameraReadingDetailComponent } from './detail/camera-reading-detail.component';
import { CameraReadingRoutingModule } from './route/camera-reading-routing.module';

@NgModule({
  imports: [SharedModule, CameraReadingRoutingModule],
  declarations: [CameraReadingComponent, CameraReadingDetailComponent],
})
export class CameraReadingModule {}
