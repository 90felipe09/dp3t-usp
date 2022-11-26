import { Request, Response } from "express"

import { StoreArguments } from "../data/StoreArguments"
import { PublicListenedExposure } from "../models/PublicListenedExposure"


export class StoreHandler {
    public static handleStore = (req: Request, res: Response) => {
        const storeArgs: StoreArguments = req.body;

        const newPublicListenedExposure = new PublicListenedExposure();
        newPublicListenedExposure.exposureHash = storeArgs.hash;
        newPublicListenedExposure.location_identifier = storeArgs.location_identifier;
        newPublicListenedExposure.temperature = storeArgs.temperature;
        newPublicListenedExposure.humidity = storeArgs.humidity;
        newPublicListenedExposure.date = new Date();
        newPublicListenedExposure.save();

        res.status(200).send('OK')
    }
}