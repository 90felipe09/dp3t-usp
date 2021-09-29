import { Request, Response } from "express"
import { Connection } from "typeorm"
import { PublicExposureParams } from "../data/PublicExposureParams";
import { PublicExposureEntry, PublicExposureResponse } from "../data/PublicExposureResponse";
import { PublicInfectedExposure } from "../models/PublicInfectedExposure";
import { PublicListenedExposure } from "../models/PublicListenedExposure";

export class PublicExposureHandler {
    public static handlePublicExposure = async (
        req: Request<{}, {}, {}, PublicExposureParams>,
        res: Response) => {
        const params = req.query;
        const publicInfectedQuery = await PublicInfectedExposure.find();
        const publicSafeQuery = await PublicListenedExposure.find();
        
        const publicInfectedData: PublicExposureEntry[] = [];
        publicInfectedQuery.forEach(infectedEntry => {
            const publicInfectedExposure: PublicExposureEntry = {
                date: infectedEntry.date,
                humidity: infectedEntry.humidity,
                temperature: infectedEntry.temperature,
                locationIdentifier: infectedEntry.location_identifier
            }
            publicInfectedData.push(publicInfectedExposure)
        })

        const publicSafeData: PublicExposureEntry[] = [];
        publicSafeQuery.forEach(infectedEntry => {
            const publicSafeExposure: PublicExposureEntry = {
                date: infectedEntry.date,
                humidity: infectedEntry.humidity,
                temperature: infectedEntry.temperature,
                locationIdentifier: infectedEntry.location_identifier
            }
            publicSafeData.push(publicSafeExposure)
        })
        
        const publicExposureResponse: PublicExposureResponse = {
            infected: publicInfectedData,
            notInfected: publicSafeData
        }

        const responseString = JSON.stringify(publicExposureResponse)

        res.status(200).send(responseString);
    }
}