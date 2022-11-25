#include "Bluetooth/BluetoothScanner.h"
#include "Hardware/HardwareController.h"

BluetoothScanner* scanner;
// setup()
//  Initializes resources and operation parameters such as:
//    - Baud rate for serial ports;
//    - Initialize BLE resources (For preparing listening for an specific UUID)
void setup()
{
  scanner = BluetoothScanner::getInstance();
  scanner->startScanning();
  HardwareController::getInstance();
}

// loop()
//  Responsible for the main operation of the device: it'll scan the
//  environment for BLE packets every 0.5 seconds.
void loop()
{
}
