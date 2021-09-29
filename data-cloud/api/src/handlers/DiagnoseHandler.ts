import { Request, Response } from "express"
import { Connection } from "typeorm"

import { DiagnoseArguments } from "../data/DiagnoseArguments"
import { PostBody } from "../data/PostBody"

import { HashesUnderAnalysis } from "../models/HashesUnderAnalysis"
import { InfectedHashes } from "../models/InfectedHashes"
import { PublicInfectedExposure } from "../models/PublicInfectedExposure"
import { PublicListenedExposure } from "../models/PublicListenedExposure"


export class DiagnoseHandler {
    public static handleDiagnose = async (req: Request, res: Response) => {
        const diagnoseArgs: DiagnoseArguments = req.body;
        
        const hashesUnderAnalysisQuery = await HashesUnderAnalysis.find({
            where: {
                aggregator: diagnoseArgs.aggregator
            }
        })

        if (diagnoseArgs.infected){
            hashesUnderAnalysisQuery.forEach(async (analysisEntry) => {
                const newInfectedHash = new InfectedHashes();
                newInfectedHash.date = analysisEntry.date;
                newInfectedHash.exposureHash = analysisEntry.exposureHash;
                await newInfectedHash.save();

                const correspondentPublicExposure = await PublicListenedExposure.find({
                    where: {
                        exposureHash: newInfectedHash.exposureHash
                    }
                })

                correspondentPublicExposure.forEach(async (publicExposure) => {
                    const newPublicInfectedExposure = new PublicInfectedExposure();
                    newPublicInfectedExposure.date = publicExposure.date;
                    newPublicInfectedExposure.exposureHash = publicExposure.exposureHash;
                    newPublicInfectedExposure.humidity = publicExposure.humidity;
                    newPublicInfectedExposure.location_identifier = publicExposure.location_identifier;
                    newPublicInfectedExposure.temperature = publicExposure.temperature;
                    await newPublicInfectedExposure.save();
                    await publicExposure.remove();
                });

            });
        }
        
        hashesUnderAnalysisQuery.forEach(async (analysisEntry) => {
            await analysisEntry.remove();
        });

        res.status(200).send('OK')
    }
}