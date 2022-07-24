import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CameraReading, ICameraReading } from '../camera-reading.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { CameraReadingService } from '../service/camera-reading.service';
import { DataUtils } from 'app/core/util/data-util.service';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-camera-reading',
  templateUrl: './camera-reading.component.html',
})
export class CameraReadingComponent implements OnInit {
  cameraReadings: ICameraReading[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected cameraReadingService: CameraReadingService, protected dataUtils: DataUtils, protected parseLinks: ParseLinks) {
    this.cameraReadings = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.cameraReadingService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<ICameraReading[]>) => {
          this.isLoading = false;
          this.paginateCameraReadings(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.cameraReadings = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ICameraReading): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  public searchReading(key: string): void {
    const results: CameraReading[] = [];
    for (const reading of this.cameraReadings) {
      if (
        reading.cameraReadingDate?.toLowerCase().indexOf(key.toLowerCase()) !== -1 ||
        reading.licensePlateNumbers?.toLowerCase().indexOf(key.toLowerCase()) !== -1 ||
        reading.licensePlatePhotoContentType?.toLowerCase().indexOf(key.toLowerCase()) !== -1
      ) {
        results.push(reading);
      }
    }
    this.cameraReadings = results;
    if (results.length === 0 || !key) {
      this.loadAll();
    }
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCameraReadings(data: ICameraReading[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.cameraReadings.push(d);
      }
    }
  }
}
