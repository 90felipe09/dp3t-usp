import { Request, Response } from "express"
import { Between } from "typeorm"
import { CheckResponse } from "../data/CheckResponse"
import { InfectedHashes } from "../models/InfectedHashes"


const DISEASE_WINDOW = 14

export class CheckHandler {
    public static handleCheck = async (req: Request, res: Response) => {
        const dateNow = new Date();
        const dateLimit = new Date(dateNow.getDate() - DISEASE_WINDOW);
        const infectedHashesTable = await InfectedHashes.find({
            where: {
                date: Between(dateNow, dateLimit)
            }
        })

        const infectedHashes: string[] = [];
        infectedHashesTable.forEach(entry => {
            infectedHashes.push(entry.exposureHash)
        })

        const infectedHashesData: CheckResponse = {
            infected_hashes: infectedHashes
        }
        const checkResponse = JSON.stringify(infectedHashesData)
        res.status(200).send(checkResponse);
    }
}