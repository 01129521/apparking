import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IManualControlDevice, ManualControlDevice } from '../manual-control-device.model';
import { ManualControlDeviceService } from '../service/manual-control-device.service';

@Component({
  selector: 'jhi-manual-control-device-update',
  templateUrl: './manual-control-device-update.component.html',
})
export class ManualControlDeviceUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    device: [null, [Validators.required, Validators.maxLength(100)]],
    state: [],
  });

  constructor(
    protected manualControlDeviceService: ManualControlDeviceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ manualControlDevice }) => {
      this.updateForm(manualControlDevice);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const manualControlDevice = this.createFromForm();
    if (manualControlDevice.id !== undefined) {
      this.subscribeToSaveResponse(this.manualControlDeviceService.update(manualControlDevice));
    } else {
      this.subscribeToSaveResponse(this.manualControlDeviceService.create(manualControlDevice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IManualControlDevice>>): void {
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

  protected updateForm(manualControlDevice: IManualControlDevice): void {
    this.editForm.patchValue({
      id: manualControlDevice.id,
      device: manualControlDevice.device,
      state: manualControlDevice.state,
    });
  }

  protected createFromForm(): IManualControlDevice {
    return {
      ...new ManualControlDevice(),
      id: this.editForm.get(['id'])!.value,
      device: this.editForm.get(['device'])!.value,
      state: this.editForm.get(['state'])!.value,
    };
  }
}
