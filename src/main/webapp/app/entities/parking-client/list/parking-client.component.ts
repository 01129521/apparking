import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IParkingClient } from '../parking-client.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ParkingClientService } from '../service/parking-client.service';
import { ParkingClientDeleteDialogComponent } from '../delete/parking-client-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-parking-client',
  templateUrl: './parking-client.component.html',
})
export class ParkingClientComponent implements OnInit {
  parkingClients: IParkingClient[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected parkingClientService: ParkingClientService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.parkingClients = [];
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

    this.parkingClientService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IParkingClient[]>) => {
          this.isLoading = false;
          this.paginateParkingClients(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.parkingClients = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IParkingClient): number {
    return item.id!;
  }

  delete(parkingClient: IParkingClient): void {
    const modalRef = this.modalService.open(ParkingClientDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.parkingClient = parkingClient;
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

  protected paginateParkingClients(data: IParkingClient[] | null, headers: HttpHeaders): void {
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
        this.parkingClients.push(d);
      }
    }
  }
}
