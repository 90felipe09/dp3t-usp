import { BaseEntity, Column, PrimaryColumn } from "typeorm";


export class BaseDatedHash extends BaseEntity {
    @PrimaryColumn({ name: "exposure_hash" })
    exposureHash: string;

    @Column({ name: "date"})
    date: Date
}