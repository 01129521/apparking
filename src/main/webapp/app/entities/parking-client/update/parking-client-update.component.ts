import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IParkingClient, ParkingClient } from '../parking-client.model';
import { ParkingClientService } from '../service/parking-client.service';

@Component({
  selector: 'jhi-parking-client-update',
  templateUrl: './parking-client-update.component.html',
})
export class ParkingClientUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    firstName: [null, [Validators.required, Validators.maxLength(50)]],
    lastName: [null, [Validators.required, Validators.maxLength(50)]],
    phoneNumber: [null, [Validators.required, Validators.maxLength(20)]],
    licensePlateNumbers: [null, [Validators.required, Validators.maxLength(20)]],
  });

  constructor(protected parkingClientService: ParkingClientService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parkingClient }) => {
      this.updateForm(parkingClient);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const parkingClient = this.createFromForm();
    if (parkingClient.id !== undefined) {
      this.subscribeToSaveResponse(this.parkingClientService.update(parkingClient));
    } else {
      this.subscribeToSaveResponse(this.parkingClientService.create(parkingClient));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParkingClient>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(parkingClient: IParkingClient): void {
    this.editForm.patchValue({
      id: parkingClient.id,
      firstName: parkingClient.firstName,
      lastName: parkingClient.lastName,
      phoneNumber: parkingClient.phoneNumber,
      licensePlateNumbers: parkingClient.licensePlateNumbers,
    });
  }

  protected createFromForm(): IParkingClient {
    return {
      ...new ParkingClient(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      licensePlateNumbers: this.editForm.get(['licensePlateNumbers'])!.value,
    };
  }
}
