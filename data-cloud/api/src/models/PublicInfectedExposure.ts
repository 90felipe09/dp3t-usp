import { Column, Entity } from "typeorm";
import { BaseDatedHash } from "./BaseDatedHash";

@Entity({ name: "public_infected_exposure" })
export class PublicInfectedExposure extends BaseDatedHash {
    @Column({ name: "location_identifier" })
    location_identifier: string;

    @Column({ name: "temperature" })
    temperature: number;

    @Column({ name: "humidity" })
    humidity: number;
}