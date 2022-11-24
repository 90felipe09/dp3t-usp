#ifndef DHT_CONTROLLER_H
#define DHT_CONTROLLER_H

#include "DHT.h"

#define DHTPIN 4
#define DHTTYPE DHT11

class DHTController
// Class for BluetoothScanner operations. Will initialize Bluetooth
//  functionalities and will implement routines for BLE packets capture
//  from environment.
{
private:
    static DHTController* instance;

    DHT dhtSensor;

public:
    DHTController();
    static DHTController* getInstance();
    float getHumidity();
    float getTemperature();
};

#endif
