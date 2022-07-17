import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IParkingClient } from '../parking-client.model';

@Component({
  selector: 'jhi-parking-client-detail',
  templateUrl: './parking-client-detail.component.html',
})
export class ParkingClientDetailComponent implements OnInit {
  parkingClient: IParkingClient | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parkingClient }) => {
      this.parkingClient = parkingClient;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
