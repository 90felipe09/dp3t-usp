/***GENERAL INCLUDES***/

//MBed Includes

#include <events/mbed_events.h>

#include "mbed.h"
/***Definições do Arquétipo***/

int comm=1; // Selecionar: 0 para nenhum, 1 para BLE, 2 para Lorawan ou 3 para Lora
int sensors=0;

// Comm parece ser a variável que designa o modo de comunicação a se utilizar, nenhum, Bluetooth Low Energy, Low Radio Wan, Low Radio

/***Global Variables***/

float  sensor_get_0 = 0;
float  sensor_get_1 = 0;
float  sensor_get_2 = 0;
float  sensor_get_3 = 0;
float  sensor_get_4 = 0;

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
#include "ble/GapAdvertisingParams.h"
#include "ble/GapAdvertisingData.h"
#include "ble/GattServer.h"
#include "BLEProcess.h"

// P1_13 e similes são constantes de definição de pinagens.
DigitalOut led1(P1_13);
InterruptIn sensor(P1_11);
AnalogIn bateria (P0_2);
AnalogIn painel (P0_28);

//Sensors Includes

#include "BME280.h" // temperatura, humidade e pressão
#include "Si1133.h" //uv, iluminação
#include "BMX160.txt" 



/***DEFINES***/

//Sensors and Peripherals

BME280 sensor_amb(P0_13, P0_15, 0x77 << 1); 
Si1133 sensor_light(P0_13, P0_15);

/***Functions***/

void Sensor_Read(int sens){
       
         if (sens==1) {
             sensor_amb.initialize();
             sensor_get_0 =  sensor_amb.getTemperature();  
             printf("Temp = %f\n", sensor_get_0);             

              
             sensor_get_1 =  sensor_amb.getPressure();
             printf("Pres = %f\n", sensor_get_1);              
            
             
             sensor_get_2 =  sensor_amb.getHumidity();
             printf("Hum = %f\n", sensor_get_2);              
             
             sensor_amb.sleep();
             
             }
             
        if (sens==2) {
            if(sensor_light.open()){
                sensor_get_3 =  sensor_light.get_light_level();
                printf("Light Level = %f\n", sensor_get_3);             
            }
        }
            
        if (sens==3) {
            if(sensor_light.open()){              
                sensor_get_4 =  sensor_light.get_uv_index();
                printf("Uv Index = %f\n", sensor_get_4);
                }              
            } 
            
        if (sens==4) {
            
            BMX160_read_acc();
            
            }

        if (sens==5) {
            
            BMX160_read_gyr();
            
            }
        
        if (sens==6) {
            
            BMX160_read_mag();
            
            }

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

int Communication(int comm){
       
        if(comm==0){
            while (1){
                //Sensor_Read(sensors);
                Sensor_Read(4);
                Sensor_Read(5);
                Sensor_Read(6);
                }
            }     

        if(comm==1){
            
            BLE &ble_interface = BLE::Instance();
            events::EventQueue event_queue;
            MyService demo_service;
            BLEProcess ble_process(event_queue, ble_interface);     
            ble_process.on_init(callback(&demo_service, &MyService::start));
            ble_process.start();
            event_queue.dispatch_forever();           
            }

        return 0;
}

int main() {
    
    BMX160_config();
    config_adc();

    Communication (comm);   
    
    }