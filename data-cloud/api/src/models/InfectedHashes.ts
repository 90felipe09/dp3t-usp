import { Column, Entity } from "typeorm";
import { BaseDatedHash } from "./BaseDatedHash";

@Entity({ name: "infected_hashes" })
export class InfectedHashes extends BaseDatedHash {
}