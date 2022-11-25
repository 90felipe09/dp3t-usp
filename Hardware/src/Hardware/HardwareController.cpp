#include "HardwareController.h"

HardwareController* HardwareController::instance = nullptr;

HardwareController::HardwareController()
{
    Serial.begin(BAUD_RATE);
    dht = DHTController::getInstance();
    wifi = WifiController::getInstance();
}

HardwareController* HardwareController::getInstance()
{
    if (instance == nullptr)
        instance = new HardwareController();
    return instance;
}

void IRAM_ATTR HardwareController::goToSleep()
{
    uint64_t timeBetweenCycles = uS_TO_mS_FACTOR * mS_TO_S_FACTOR;
    timeBetweenCycles *= HOURS_TO_SLEEP * S_TO_HOUR_FACTOR;
    esp_sleep_enable_timer_wakeup(timeBetweenCycles);
    Serial.println("Entrando em Deep Sleep");
    esp_deep_sleep_start();
}

void HardwareController::initOperationTimer()
{
    timer = timerBegin(0, 80, true);
    timerWrite(timer, 0);

    timerAttachInterrupt(timer, &HardwareController::goToSleep, true);
    uint64_t timeBetweenCycles = uS_TO_mS_FACTOR * mS_TO_S_FACTOR;
    timeBetweenCycles *= HOURS_TO_SLEEP * S_TO_HOUR_FACTOR;
    timerAlarmWrite(timer, timeBetweenCycles, true);
    timerAlarmEnable(timer);
}

void HardwareController::activateCapture(String hash)
{
    float temperature = dht->getTemperature();
    float humidity = dht->getHumidity();

    wifi->sendData(hash, temperature, humidity);
}
