#include "WifiController.h"

WifiController* WifiController::instance = nullptr;

void WifiController::initWifiConnection()
{
    const char* ssid = SSID;
    const char* password = PASSWORD;
    Serial.println("Scanning...");
    WiFi.begin(ssid, password);

    while(WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }
    Serial.println("");
    Serial.print("Connected to WiFi network with IP Address: ");
    Serial.println(WiFi.localIP());

    http.begin(client, SERVER_ENDPOINT);
    http.addHeader("Content-Type", "application/json");
}

const char* WifiController::mountDataPacket(String hash, float t, float h)
{
    String nodeLocation = NODE_LOCATION;
    String hashJsonPart = "{\"hash\": \"" + hash + "\",";
    String locJsonPart = "\"location_identifier\": \"" + nodeLocation + "\",";
    String tempJsonPart =  "\"temperature\": " + String(t) + ",";
    String humdJsonPart = "\"humidity\": " + String(h) + "}";
    String jsonString = hashJsonPart + locJsonPart + tempJsonPart + humdJsonPart;

    const char* httpRequestData = jsonString.c_str();
    return httpRequestData;
}

WifiController::WifiController()
{
    initWifiConnection();
}

WifiController* WifiController::getInstance()
{
    if (instance == nullptr)
        instance = new WifiController();
    return instance;
}

void WifiController::sendData(String hash, float temperature, float humidity)
{
    const char* httpRequestData = mountDataPacket(hash, temperature, humidity);
    int httpResponseCode = http.POST(httpRequestData);
}
