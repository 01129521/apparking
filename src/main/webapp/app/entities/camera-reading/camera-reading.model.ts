export interface ICameraReading {
  id?: number;
  cameraReadingDate?: string | null;
  licensePlateNumbers?: string | null;
  licensePlatePhotoContentType?: string | null;
  licensePlatePhoto?: string | null;
}

export class CameraReading implements ICameraReading {
  constructor(
    public id?: number,
    public cameraReadingDate?: string | null,
    public licensePlateNumbers?: string | null,
    public licensePlatePhotoContentType?: string | null,
    public licensePlatePhoto?: string | null
  ) {}
}

export function getCameraReadingIdentifier(cameraReading: ICameraReading): number | undefined {
  return cameraReading.id;
}
