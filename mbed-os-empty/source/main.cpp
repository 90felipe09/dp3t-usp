/***GENERAL INCLUDES***/
//appUUID: 03d95df9-63c9-494d-a8b0-d86f2bdc06c3
//parcel: 03d95df9-63c9-494d-a8b0-d86f2bdc06c3
//MBed Includes

#include <events/mbed_events.h>

#include "mbed.h"
/***Definições do Arquétipo***/
int sensors=0;

// Comm parece ser a variável que designa o modo de comunicação a se utilizar, nenhum, Bluetooth Low Energy, Low Radio Wan, Low Radio

/***Global Variables***/

float  temperatureRawMeasure = 0;
float  pressureRawMeasure = 0;
float  humidityRawMeasure = 0;

uint32_t sens00 = 0;
uint32_t sens01 = 0;
uint32_t sens02 = 0;
int cont=0;
int read_sens=0;

/***SPECIFIC INCLUDES***/

//BLE Includes

    
#include "platform/Callback.h"
#include "events/EventQueue.h"
#include "platform/NonCopyable.h"

#include "ble/BLE.h"
#include "ble/Gap.h"
#include "ble/GattClient.h"
//#include "ble/GapAdvertisingParams.h"
//#include "ble/GapAdvertisingData.h"
#include "ble/GattServer.h"
#include "BLEProcess.h"

#include <Gap.h>

// P1_13 e similes são constantes de definição de pinagens.
DigitalOut led1(P1_13);
InterruptIn sensor(P1_11);
AnalogIn bateria (P0_2);
AnalogIn painel (P0_28);

//Sensors Includes

#include "BME280.h" // temperatura, humidade e pressão

/***DEFINES***/

//Sensors and Peripherals

BME280 sensor_amb(P0_13, P0_15, 0x77 << 1); 

/***Functions***/

void Read_THP(){
    sensor_amb.initialize();
    temperatureRawMeasure =  sensor_amb.getTemperature();  
    printf("Temp = %f\n", temperatureRawMeasure);             

    pressureRawMeasure =  sensor_amb.getPressure();
    printf("Pres = %f\n", pressureRawMeasure);              

    humidityRawMeasure =  sensor_amb.getHumidity();
    printf("Hum = %f\n", humidityRawMeasure);              
}

void blink_led (void){
    for(int i=0; i<10; i++){
        led1=1;
        wait(0.1);
        led1=0;
        }
    }

void config_adc(void){
    
    NRF_SAADC->CH[0].PSELP = 1;
    NRF_SAADC->CH[0].PSELN = 0;
    NRF_SAADC->CH[4].PSELP = 5;
    NRF_SAADC->CH[4].PSELN = 0;
    
    NRF_SAADC->RESOLUTION = 2;
    
    //NRF_SAADC->RESULT.MAXCNT = 1;
    NRF_SAADC->OVERSAMPLE = 0;
    NRF_SAADC->SAMPLERATE = SAADC_SAMPLERATE_MODE_Task;
    
    NRF_SAADC->ENABLE = 1;
    
    //Calibrate de SAADC
    NRF_SAADC->TASKS_CALIBRATEOFFSET = 1;
    while(NRF_SAADC->EVENTS_CALIBRATEDONE == 0);
    NRF_SAADC->EVENTS_CALIBRATEDONE = 0;
    while (NRF_SAADC->STATUS == SAADC_STATUS_STATUS_Busy);
    
    // Start the SAADC and wait for the started event.
    NRF_SAADC->TASKS_START = 1;
    while(NRF_SAADC->EVENTS_STARTED == 0);
    NRF_SAADC->EVENTS_STARTED = 0;
    
    NRF_SAADC->TASKS_SAMPLE = 1;
    
    // Stop the SAADC, since it's not used anymore.
    NRF_SAADC->TASKS_STOP = 1;
    while (NRF_SAADC->EVENTS_STOPPED == 0);
    NRF_SAADC->EVENTS_STOPPED = 0;
    NVIC_ClearPendingIRQ(SAADC_IRQn);
    
    }

void read_adc1 (AnalogIn pin1, uint32_t result){
    
    float meas1;
    float meas_old1=0;
    int porcentagem1;
    
    for(int i=0; i<10 ; i++) {
        
        meas1 = pin1.read(); // Converts and read the analog input value (value from 0.0 to 1.0)
        meas1 = meas1 * 3300; // Change the value to be in the 0 to 3300 range
        
        if (abs(meas_old1 - meas1) > 5){
        meas_old1 = meas1;
        }
        
        if (meas1 <= 2200){
            porcentagem1=0; 
            }
        else if (meas1 >= 3000){
            porcentagem1=100;
            }
        else {
            porcentagem1 = (meas1 - 2200) *0.125 ;
            }

        printf("measure1 = %.0f mV ( %d%c) \n", meas1, porcentagem1, '%');
        meas_old1 = meas1;

        wait(0.2); // 100 ms
        }  
    
//    result= meas;
    sens00=meas1; 
    
    }

void read_adc2 (AnalogIn pin2, uint32_t result){
    
    float meas2;
    float meas_old2=0;
    int porcentagem2;
    
    for(int i=0; i<10 ; i++) {
        
        meas2 = pin2.read(); // Converts and read the analog input value (value from 0.0 to 1.0)
        meas2 = meas2 * 3300; // Change the value to be in the 0 to 3300 range

        if (abs(meas_old2 - meas2) > 5){
        meas_old2 = meas2;
        }
        
        if (meas2 == 0){
            porcentagem2=0; 
            }
        else if (meas2 >= 3300){
            porcentagem2=100;
            }
        else {
            porcentagem2 = (meas2 - 0) *0.03 ;
            }

        printf("measure2 = %.0f mV ( %d%c) \n", meas2, porcentagem2, '%');
        meas_old2 = meas2;

        wait(0.2); // 100 ms
        }  
    
//    result= meas;
    sens01=meas2;  
    
    }

void read_IoT_sensors (void){
    
    sensor.fall(&blink_led);
    read_adc1 (bateria, sens00);
    read_adc2 (painel, sens01);
    sens02 = sensor;
    }
#include "BLE.txt"

void scanCallback(const Gap::AdvertisementCallbackParams_t *params){
#if DUMP_ADV_DATA
    for(unsigned index = 0; index < params-> advertisingDataLen; index++){
        printf("%02x ", params->advertisingData[index]);
        read_IoT_sensors();
    }
#endif
}

void BleScannerConfig(){ 
    BLE &ble_interface = BLE::Instance();
    ble::Gap& gap = ble_interface.gap();
    
    // phy_t: Tipo de configuração para camada física em OSI;
    //  LE_1M significa que a taxa de transferência seria de 1Mbit/s
    //  Usa-se a taxa de 2Mbit/s com LE_2M para ter transferências mais rápidas
    //  e e minimizar o tempo de comunicação, o que leva a uma maior economia de bateria.
    //  Algo importante em IoT. O preço: Menor sensibilidade. No entanto é um preço aceitável,
    //  uma vez que se estará em ambiente público recebendo a todo momento novos dados.
    ble::phy_t phy = ble::phy_t::LE_2M;

    // window e interval são todos do tipo Duration, por isso possuem os mesmo métodos
    // scan_window_t: Determina o duty cycle que se escutará um pacote;
    //  ao usar o min, especificamos que queremos 200ms do período ouvindo.
    BLE::scan_window_t scanWindowDuration = ble::scan_window_t::min();

    //  scan_interval_t: Determina o intervalo de duração de um ciclo.
    //      Como tanto o intervalo como a janela estão em 200ms, teremos 100% de duty cycle.
    BLE::scan_interval_t scanIntervalDuration = ble::scan_interval_t::min();

    //  O Scan ativo responde a todo pacote que recebe. Não será necessário, por isso usaremos o passivo.
    //      Isto irá nos poupar bateria.
    bool activeScanning = false;

    //  Este elemento é passivo. Por isso o seu endereço não será lido em nenhum momneto.
    //      Podemos configurá-lo como aleatório.
    ble::own_address_type_t ownAddressType = ble::own_address_type_t::RANDOM;

    // Só leremos pacotes com o uuid especificado.
    ble::scanning_filter_policy_t scanningFilterPolicy = ble::scanning_filter_policy_t::FILTER_ADVERTISING;

    ble::ScanParameters params = new ble::ScanParameters(
        phy, 
        scanWindowDuration, 
        scanIntervalDuration, 
        activeScanning, 
        ownAddressType, 
        scanningFilterPolicy);

    gap.setScanParameters(param);
    // versão mais simples:
    // ble.init();
    // ble.setScanParams(500,200);

    ble::whitelist_t whitelist;
    entry_t *entries = {"03d95df9-63c9-494d-a8b0-d86f2bdc06c3"}
    whitelist::addresses = ble::whitelist_t::entry_t();
    gap.setWhiteList(entries);
    gap.startScan(scanCallback);
    while(true){
        ble.waitForEvent();
    }
}



int main() {
    config_adc();

    BleScannerConfig ();   

    
}