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

const char* ssid = "ssid";
const char* password = "password";

String serverName = "http://192.168.0.x:8008/store";

String nodeLocation = "Praça da sé";

#define DHTPIN 4
#define DHTTYPE DHT11

DHT dht(DHTPIN, DHTTYPE);
hw_timer_t *timer = NULL;

int scanTime = 5; //In seconds
BLEScan *pBLEScan;

String serviceUUID = "03d95df9-63c9-494d-a8b0-d86f2bdc06c3";

const char* mountDataPacket(String hash, float t, float h){
  String hashJsonPart = "{\"hash\": \"" + hash + "\",";
  String locJsonPart = "\"location_identifier\": \"" + nodeLocation + "\",";
  String tempJsonPart =  "\"temperature\": " + String(t) + ",";
  String humdJsonPart = "\"humidity\": " + String(h) + "}";
  String jsonString = hashJsonPart + locJsonPart + tempJsonPart + humdJsonPart;

  const char* httpRequestData = jsonString.c_str();
  return httpRequestData;
}

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
{
    void onResult(BLEAdvertisedDevice advertisedDevice)
    {
      if (advertisedDevice.haveServiceUUID())
      {
        BLEUUID devUUID = advertisedDevice.getServiceUUID();
        if (serviceUUID.equals(devUUID.toString().c_str())){
          initDataCaptureRoutine(advertisedDevice);
        }
        return;
      }
    }
};

void IRAM_ATTR go_to_sleep(){
  esp_sleep_enable_timer_wakeup(HOURS_TO_SLEEP * S_TO_HOUR_FACTOR * uS_TO_S_FACTOR);
  Serial.println("Entrando em Deep Sleep");
  esp_deep_sleep_start();
}

void initWifiConnection(){
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

void initBLE(){
  BLEDevice::init("");
  pBLEScan = BLEDevice::getScan(); //create new scan
  pBLEScan->setAdvertisedDeviceCallbacks(new MyAdvertisedDeviceCallbacks());
  pBLEScan->setActiveScan(true); //active scan uses more power, but get results faster
  pBLEScan->setInterval(1000);
  pBLEScan->setWindow(99); // less or equal setInterval value
}

void initOperationTimer(){
  timer = timerBegin(0, 80, true); // conta a cada 1 uS
  timerWrite(timer, 0); // Reseta o timer

  timerAttachInterrupt(timer, &go_to_sleep, true); // Caso o timer estoure, chama go_to_sleep
  timerAlarmWrite(timer, HOURS_TO_SLEEP * S_TO_HOUR_FACTOR * uS_TO_S_FACTOR, true); // Espera 12 horas para chamar go_to_sleep

  timerAlarmEnable(timer);  // Alarme inicia
}

void setup()
{
  Serial.begin(115200);
  Serial.println("Iniciando em Active Mode");
  dht.begin();
  initWifiConnection();
  initBLE();
  initOperationTimer();
}

void loop()
{
  BLEScanResults foundDevices = pBLEScan->start(scanTime, false);
  pBLEScan->clearResults(); // delete results fromBLEScan buffer to release memory
  delay(5000);
}
