export interface IParkingClient {
  id?: number;
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
  licensePlateNumbers?: string;
}

export class ParkingClient implements IParkingClient {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public phoneNumber?: string,
    public licensePlateNumbers?: string
  ) {}
}

export function getParkingClientIdentifier(parkingClient: IParkingClient): number | undefined {
  return parkingClient.id;
}
