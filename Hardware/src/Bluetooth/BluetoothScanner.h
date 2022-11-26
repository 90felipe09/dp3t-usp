#ifndef BLUETOOTH_SCANNER_H
#define BLUETOOTH_SCANNER_H

#include <BLEScan.h>

#include "AdvertisedDeviceCallbacks.h"

#define SCAN_TIME 5 //In seconds

class BluetoothScanner
// Class for BluetoothScanner operations. Will initialize Bluetooth
//  functionalities and will implement routines for BLE packets capture
//  from environment.
{
private:
    static BluetoothScanner* instance;
    BLEScan *BLEScanInstance;

    // init()
    //  Configures BLE listener to scan environment every second.
    //  If finds a BLE packet, reacts by calling MyAdvertisedDeviceCallbacks
    //  activity.
    void init();
    void runScan();
    static void endScan(BLEScanResults scanResults);

public:
    BluetoothScanner();
    static BluetoothScanner* getInstance();
    void startScanning();
    void clearScanning();
};

#endif
