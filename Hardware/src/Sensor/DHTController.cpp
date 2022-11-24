#include "DHTController.h"

DHTController* DHTController::instance = nullptr;

DHTController::DHTController()
{
    dht = new DHT(DHTPIN, DHTTYPE);
}

DHTController* DHTController::getInstance()
{
    if (instance == nullptr)
        instance = new DHTController();
    return instance;
}

float DHTController::getHumidity()
{
    return dht.readHumidity();
}

float DHTController::getTemperature()
{
    return dht.readTemperature();
}
