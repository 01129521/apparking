import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IManualControlDevice } from '../manual-control-device.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ManualControlDeviceService } from '../service/manual-control-device.service';
import { ManualControlDeviceDeleteDialogComponent } from '../delete/manual-control-device-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-manual-control-device',
  templateUrl: './manual-control-device.component.html',
})
export class ManualControlDeviceComponent implements OnInit {
  manualControlDevices: IManualControlDevice[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected manualControlDeviceService: ManualControlDeviceService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.manualControlDevices = [];
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

    this.manualControlDeviceService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IManualControlDevice[]>) => {
          this.isLoading = false;
          this.paginateManualControlDevices(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.manualControlDevices = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  setActive(manualControlDevice: IManualControlDevice, isActivated: boolean): void {
    this.manualControlDeviceService.update({ ...manualControlDevice, state: isActivated }).subscribe(() => this.reset());
  }

  trackId(_index: number, item: IManualControlDevice): number {
    return item.id!;
  }

  delete(manualControlDevice: IManualControlDevice): void {
    const modalRef = this.modalService.open(ManualControlDeviceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.manualControlDevice = manualControlDevice;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateManualControlDevices(data: IManualControlDevice[] | null, headers: HttpHeaders): void {
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
        this.manualControlDevices.push(d);
      }
    }
  }
}
