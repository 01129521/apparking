export interface IManualControlDevice {
  id?: number;
  device?: string;
  state?: boolean | null;
}

export class ManualControlDevice implements IManualControlDevice {
  constructor(public id?: number, public device?: string, public state?: boolean | null) {
    this.state = this.state ?? false;
  }
}

export function getManualControlDeviceIdentifier(manualControlDevice: IManualControlDevice): number | undefined {
  return manualControlDevice.id;
}
