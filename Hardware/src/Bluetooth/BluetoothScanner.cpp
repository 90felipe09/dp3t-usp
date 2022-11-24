#include "BluetoothScanner.h"

BluetoothScanner* BluetoothScanner::instance = 0;

BluetoothScanner* BluetoothScanner::getInstance(){
    if (instance == 0){
        instance = new BluetoothScanner();
    }
    return instance;
}

BluetoothScanner::BluetoothScanner(){
    init();
}

void BluetoothScanner::init() {
    BLEDevice::init("");
    BLEScanInstance = BLEDevice::getScan();
    AdvertisedDeviceCallbacks* callback = new AdvertisedDeviceCallbacks();
    BLEScanInstance->setAdvertisedDeviceCallbacks(callback, false);
    BLEScanInstance->setActiveScan(true);
}

void BluetoothScanner::endScan(BLEScanResults scanResults)
{
    BluetoothScanner* bleScanner = BluetoothScanner::getInstance();
    bleScanner->clearScanning();
    bleScanner->startScanning();
}

void BluetoothScanner::runScan()
{
    BLEScanInstance->start(SCAN_TIME, endScan);
}

void BluetoothScanner::startScanning() {
    runScan();
}

void BluetoothScanner::clearScanning() {
    BLEScanInstance->clearResults();
}
