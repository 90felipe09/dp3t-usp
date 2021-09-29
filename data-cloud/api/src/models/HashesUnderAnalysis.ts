import { Column, Entity } from "typeorm";
import { BaseDatedHash } from "./BaseDatedHash";

@Entity({ name: "hashes_under_analysis" })
export class HashesUnderAnalysis extends BaseDatedHash {
    @Column({ name: "aggregator" })
    aggregator: string;
}