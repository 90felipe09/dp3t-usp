import { Column, Entity } from "typeorm";
import { BaseDatedHash } from "./BaseDatedHash";

@Entity({ name: "public_listened_exposure" })
export class PublicListenedExposure extends BaseDatedHash {
    @Column({ name: "location_identifier" })
    location_identifier: string;

    @Column({ name: "temperature" })
    temperature: number;

    @Column({ name: "humidity" })
    humidity: number;
}