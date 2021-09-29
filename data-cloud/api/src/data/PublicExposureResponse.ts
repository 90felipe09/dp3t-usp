export type PublicExposureEntry = {
    locationIdentifier: string;
    date: Date;
    temperature: number;
    humidity: number;
}

export type PublicExposureResponse = {
    infected: PublicExposureEntry[],
    notInfected: PublicExposureEntry[]
}