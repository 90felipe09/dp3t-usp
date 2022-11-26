#include "AdvertisedDeviceCallbacks.h"


String AdvertisedDeviceCallbacks::serviceUUID = SERVICE_UUID;

void AdvertisedDeviceCallbacks::onResult(BLEAdvertisedDevice advertisedDevice)
{
    if (!advertisedDevice.haveServiceUUID())
    {
        return;
    }
    BLEUUID devUUID = advertisedDevice.getServiceUUID();
    const char* devUUID_str = devUUID.toString().c_str();
    bool isFromService = AdvertisedDeviceCallbacks::serviceUUID.equals(devUUID_str);
    if (isFromService){
        initDataCaptureRoutine(advertisedDevice);
    }
}


void AdvertisedDeviceCallbacks::initDataCaptureRoutine(BLEAdvertisedDevice advertisedDevice)
{
    if (!advertisedDevice.haveManufacturerData())
    {
        return;
    }
    String manufData = advertisedDevice.toString().c_str();
    String hash = manufData.substring(55,75);
    
    HardwareController* hw = HardwareController::getInstance();
    hw->activateCapture(hash);
}
