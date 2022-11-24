#ifndef HARDWARE_CONTROLLER_H
#define HARDWARE_CONTROLLER_H

#include <Arduino.h>

#include "../Sensor/DHTController.h"
#include "../Wifi/WifiController.h"

#define uS_TO_S_FACTOR 1000000
#define S_TO_HOUR_FACTOR 3600
#define HOURS_TO_SLEEP  12
#define BAUD_RATE 115200

class HardwareController
// Class for BluetoothScanner operations. Will initialize Bluetooth
//  functionalities and will implement routines for BLE packets capture
//  from environment.
{
private:
    static HardwareController* instance;

    DHTController* dht;
    WifiController* wifi;
    hw_timer_t* timer;

    void IRAM_ATTR goToSleep();
    void initOperationTimer();

public:
    HardwareController();
    static HardwareController* getInstance();
    void activateCapture(String hash);
};

#endif
