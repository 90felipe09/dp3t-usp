#include "DHTController.h"

DHTController* DHTController::instance = nullptr;

DHTController::DHTController()
{
    dhtSensor = new DHT(DHTPIN, DHTTYPE);
    dhtSensor->begin();
}

DHTController* DHTController::getInstance()
{
    if (instance == nullptr)
        instance = new DHTController();
    return instance;
}

float DHTController::getHumidity()
{
    return dhtSensor->readHumidity();
}

float DHTController::getTemperature()
{
    return dhtSensor->readTemperature();
}
