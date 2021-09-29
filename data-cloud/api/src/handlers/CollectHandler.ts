import { Request, Response } from "express"
import { v4 as uuidv4 } from "uuid"
import { CollectArguments } from "../data/CollectArguments"
import { HashesUnderAnalysis } from "../models/HashesUnderAnalysis"


export class CollectHandler {
    public static handleCollect = async (req: Request, res: Response) => {
        const collectArgs: CollectArguments = req.body;
        const randomAggregator = uuidv4();
        collectArgs.hashes.forEach(async (hash) => {
            const newHashUnderAnalysis = new HashesUnderAnalysis();
            newHashUnderAnalysis.exposureHash = hash.hash;
            newHashUnderAnalysis.date = hash.date;
            newHashUnderAnalysis.aggregator = randomAggregator.toString();
            await newHashUnderAnalysis.save();
        });
        res.status(200).send('OK')
    }
}