/*
   Based on Neil Kolban example for IDF: https://github.com/nkolban/esp32-snippets/blob/master/cpp_utils/tests/BLE%20Tests/SampleScan.cpp
   Ported to Arduino ESP32 by Evandro Copercini
   Changed to a beacon scanner to report iBeacon, EddystoneURL and EddystoneTLM beacons by beegee-tokyo

   Code developed by:
   - Felipe Kenzo Shiraishi
   - Hector Kobayashi Yassuda
   - Yoram Kraiser Goldstein
*/

#include <Arduino.h>

#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEScan.h>
#include <BLEBeacon.h>

#include "DHT.h"

#include <WiFi.h>
#include <HTTPClient.h>

#define uS_TO_S_FACTOR 1000000
#define S_TO_HOUR_FACTOR 3600
#define HOURS_TO_SLEEP  12

String serverName = "http://192.168.15.23:8008/store";

String nodeLocation = "Praça da sé";

#define DHTPIN 4
#define DHTTYPE DHT11

DHT dht(DHTPIN, DHTTYPE);
hw_timer_t *timer = NULL;

int scanTime = 5; //In seconds
BLEScan *pBLEScan;

// mountDataPacket(String, float, float)
//  Utilitary function to mount json data to send to server.
const char* mountDataPacket(String hash, float t, float h){
  String hashJsonPart = "{\"hash\": \"" + hash + "\",";
  String locJsonPart = "\"location_identifier\": \"" + nodeLocation + "\",";
  String tempJsonPart =  "\"temperature\": " + String(t) + ",";
  String humdJsonPart = "\"humidity\": " + String(h) + "}";
  String jsonString = hashJsonPart + locJsonPart + tempJsonPart + humdJsonPart;

  const char* httpRequestData = jsonString.c_str();
  return httpRequestData;
}

// initDataCaptureRoutine(BLEAdvertisedDevice)
//  - Captures present temperature and humidity from DHT;
//  - Parse hash information in BLE packet;
//  - Sends this info via Wifi to the service endpoint;
void initDataCaptureRoutine(BLEAdvertisedDevice advertisedDevice){
    Serial.println("Iniciando captura de dados.");
    float h = dht.readHumidity();
    float t = dht.readTemperature();
  
    if (isnan(h) || isnan(t)) {
      return;
    }

    else{ 
      WiFiClient client;
      HTTPClient http;
  
      http.begin(client, serverName);
      http.addHeader("Content-Type", "application/json");

      if (advertisedDevice.haveManufacturerData() == true)
      {
        String manufData = advertisedDevice.toString().c_str();
        String hash = manufData.substring(55,75);
        const char* httpRequestData = mountDataPacket(hash, t, h);
        int httpResponseCode = http.POST(httpRequestData);
        Serial.println("Dados enviados");
      }  
    }
}

class MyAdvertisedDeviceCallbacks : public BLEAdvertisedDeviceCallbacks
// Class for BLE callback reaction for advertisement package capture
{
    static String serviceUUID;
    // onResult(BLEAdvertisedDevice)
    //  Asserts that the intercepted package has a Service UUID, verify that
    //  it corresponds to the expected service uuid and proceeds to capture
    //  data if everthing is fine.
    void onResult(BLEAdvertisedDevice advertisedDevice)
    {
      if (advertisedDevice.haveServiceUUID())
      {
        BLEUUID devUUID = advertisedDevice.getServiceUUID();
        if (serviceUUID.equals(
          devUUID.toString().c_str())){
          initDataCaptureRoutine(advertisedDevice);
        }
        return;
      }
    }
};

String MyAdvertisedDeviceCallbacks::serviceUUID = "03d95df9-63c9-494d-a8b0-d86f2bdc06c3";

// go_to_sleep()
//  Method to toggle device operation mode from an active state to a deep sleep
//  state for 12 hours
void IRAM_ATTR go_to_sleep(){
  esp_sleep_enable_timer_wakeup(HOURS_TO_SLEEP * S_TO_HOUR_FACTOR * uS_TO_S_FACTOR);
  Serial.println("Entrando em Deep Sleep");
  esp_deep_sleep_start();
}

// initWifiConnection()
//  Configure wifi connection to a pre-known network.
//  Blocks device operation until is appropriately connected.
void initWifiConnection(){
  const char* ssid = "ssid";
  const char* password = "password";
  Serial.println("Scanning...");
  WiFi.begin(ssid, password);

  while(WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected to WiFi network with IP Address: ");
  Serial.println(WiFi.localIP());
}

// initBLE()
//  Configures BLE listener to scan environment every second.
//  If finds a BLE packet, reacts by calling MyAdvertisedDeviceCallbacks
//  activity.
void initBLE(){
  BLEDevice::init("");
  pBLEScan = BLEDevice::getScan();
  pBLEScan->setAdvertisedDeviceCallbacks(new MyAdvertisedDeviceCallbacks());
  pBLEScan->setActiveScan(true);
  pBLEScan->setInterval(1000);
  pBLEScan->setWindow(99);
}

// initOperationTimer()
//  Configures timer to activate an alarm after 12 hours.
//  This activation will call go_to_sleep() method.
void initOperationTimer(){
  timer = timerBegin(0, 80, true);
  timerWrite(timer, 0);

  timerAttachInterrupt(timer, &go_to_sleep, true);
  timerAlarmWrite(timer, HOURS_TO_SLEEP * S_TO_HOUR_FACTOR * uS_TO_S_FACTOR, true);

  timerAlarmEnable(timer);
}

// setup()
//  Initializes resources and operation parameters such as:
//    - Baud rate for serial port;
//    - Initialize DHT resources (Sensor for temperature and humidity)
//    - Initialize Wifi resources (By connecting to an available network)
//    - Initialize BLE resources (For preparing listening for an specific UUID)
//    - Initialize operation timer (For energy consumption mode toggler)
void setup()
{
  Serial.begin(115200);
  Serial.println("Iniciando em Active Mode");
  dht.begin();
  initWifiConnection();
  initBLE();
  initOperationTimer();
}

// loop()
//  Responsible for the main operation of the device: it'll scan the
//  environment for BLE packets every 5 seconds.
void loop()
{
  BLEScanResults foundDevices = pBLEScan->start(scanTime, false);
  pBLEScan->clearResults();
  delay(5000);
}
