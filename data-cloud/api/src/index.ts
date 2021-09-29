import bodyParser from 'body-parser';
import * as dotenv from 'dotenv';
import express from 'express';
import http from 'http';
import "reflect-metadata";
import { ConnectionOptions, createConnection } from "typeorm";
import { CheckHandler } from './handlers/CheckHandler';
import { CollectHandler } from './handlers/CollectHandler';
import { DiagnoseHandler } from './handlers/DiagnoseHandler';
import { PublicExposureHandler } from './handlers/PublicExposureHandler';
import { StoreHandler } from './handlers/StoreHandler';
import { HashesUnderAnalysis } from './models/HashesUnderAnalysis';
import { InfectedHashes } from './models/InfectedHashes';
import { PublicInfectedExposure } from './models/PublicInfectedExposure';
import { PublicListenedExposure } from './models/PublicListenedExposure';

dotenv.config();

export const initServer = async () => {
    try{
        const connectionOptions: ConnectionOptions = {
            type: 'postgres',
            host: process.env.POSTGRES_HOST || 'localhost',
            port: parseInt(process.env.POSTGRES_PORT || '5438'),
            username: process.env.POSTGRES_USER || 'postgres',
            password: process.env.POSTGRES_PASSWORD || 'postgres',
            database: 'postgres',
            entities: [
                HashesUnderAnalysis,
                InfectedHashes,
                PublicInfectedExposure,
                PublicListenedExposure
            ]
        }
        const connection = await createConnection(connectionOptions);
        
        const app = express();
    
        const server = http.createServer(app);
        const jsonParser = bodyParser.json();
        app.use(jsonParser);
    
        server.listen(process.env.PORT || 8008, () => {
            console.log(`Server started on port: (${process.env.PORT || 8008})`);
        });
    
        app.get('/check', (req,res) => CheckHandler.handleCheck(req, res));
        app.post('/store', (req,res) => StoreHandler.handleStore(req, res));
        app.get('/public_exposure', (req,res) => PublicExposureHandler.handlePublicExposure(req, res));
        app.post('/diagnose', (req,res) => DiagnoseHandler.handleDiagnose(req, res));
        app.post('/collect', (req,res) => CollectHandler.handleCollect(req, res));
    }
    catch(e){
        console.log(e)
    }
}


initServer();
