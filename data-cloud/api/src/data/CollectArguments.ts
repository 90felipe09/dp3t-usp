export type DatedHash = {
    hash: string,
    date: Date
}

export type CollectArguments = {
    hashes: DatedHash[];
}