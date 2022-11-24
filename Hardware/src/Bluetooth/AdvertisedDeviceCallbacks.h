#ifndef ADVERTISED_DEVICE_CALLBACKS_H
#define ADVERTISED_DEVICE_CALLBACKS_H

#include <Arduino.h>

#include <BLEDevice.h>
#include <BLEAdvertisedDevice.h>

#include "../Hardware/HardwareController.h"

#define SERVICE_UUID "03d95df9-63c9-494d-a8b0-d86f2bdc06c3"

class AdvertisedDeviceCallbacks : public BLEAdvertisedDeviceCallbacks
// Class for BLE callback reaction for advertisement package capture
{
public:
    // serviceUUID;
    //  Defines service UUID to device to listen.
    //  If a packet with the corresponding serviceUUID comes, the device
    //  triggers its initDataCaptureRoutine() method.
    static String serviceUUID;
    // onResult(BLEAdvertisedDevice)
    //  Asserts that the intercepted package has a Service UUID, verify that
    //  it corresponds to the expected service uuid and proceeds to capture
    //  data if everthing is fine.
    void onResult(BLEAdvertisedDevice advertisedDevice);
private:
    // initDataCaptureRoutine(BLEAdvertisedDevice)
    //  - Is the device BLE event handler
    void initDataCaptureRoutine(BLEAdvertisedDevice advertisedDevice);
};

#endif
