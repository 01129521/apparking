import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICameraReading } from '../camera-reading.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-camera-reading-detail',
  templateUrl: './camera-reading-detail.component.html',
})
export class CameraReadingDetailComponent implements OnInit {
  cameraReading: ICameraReading | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cameraReading }) => {
      this.cameraReading = cameraReading;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
