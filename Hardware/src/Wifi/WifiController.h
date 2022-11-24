#ifndef WIFI_CONTROLLER_H
#define WIFI_CONTROLLER_H

#include <WiFi.h>
#include <HTTPClient.h>

#define SSID ""
#define PASSWORD ""
#define SERVER_ENDPOINT "http://192.168.0.x:8008/store"
#define NODE_LOCATION "Praça da sé"

class WifiController
// Class for BluetoothScanner operations. Will initialize Bluetooth
//  functionalities and will implement routines for BLE packets capture
//  from environment.
{
private:
    static WifiController* instance;

    WiFiClient client;
    HTTPClient http;

    // init()
    //  Configures BLE listener to scan environment every second.
    //  If finds a BLE packet, reacts by calling MyAdvertisedDeviceCallbacks
    //  activity.
    void initWifiConnection();
    const char* mountDataPacket(String hash, float t, float h);

public:
    WifiController();
    static WifiController* getInstance();
    void sendData(String hash, float temperature, float humidity);
};

#endif
