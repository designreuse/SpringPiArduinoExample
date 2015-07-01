#include <Wire.h>
#include <OneWire.h>
#include <DallasTemperature.h>

// SENSOR
const int temperatureSensor = 2;
OneWire oneWire(temperatureSensor);
DallasTemperature sensors(&oneWire);

const int humiditySensor = 13;
OneWire oneWire2(humiditySensor);
DallasTemperature sensors2(&oneWire2);

const int waterLowSensor = 5;
const int waterHighSensor = 4;
const int mixingLowSensor = 3;
const int mixingHighSensor = 6;

// LED
const int waterInLed = 8;
const int waterSpreadLed = 12;
const int mixingInLed = 9;
const int mixingLed = 10;
const int mixingSpreadLed = 11;

// STATES
int generalState = 0; // 0 = waiting, 1 = spreadingWater, 2 = mixing, 3 = spreading mix.
boolean irrigate = false;

// VALUES
int humidityValue = 0;
int waterTemperature = 0; 

boolean fertilizer = false;
int percentage = 0;

void setup() {
  // I2C Configuration
  Wire.begin(0x04);             // I2C bus address.
  Wire.onReceive(receiveEvent); // Receive interruption/event.
  Wire.onRequest(requestEvent); // Receive interruption/event.
  
  sensors.begin();
  
  // initialize inputs:
  pinMode(waterLowSensor, INPUT);
  pinMode(waterHighSensor, INPUT);
  pinMode(mixingLowSensor, INPUT);
  pinMode(mixingHighSensor, INPUT);
  // initialize outputs:
  pinMode(waterInLed, OUTPUT);
  pinMode(waterSpreadLed, OUTPUT);
  pinMode(mixingInLed, OUTPUT);
  pinMode(mixingLed, OUTPUT);
  pinMode(mixingSpreadLed, OUTPUT);

  beep();
}

int beeped = 0;

void loop() {
  
  int waterHighSensorState = digitalRead(waterHighSensor);
  int waterLowSensorState = 0;
  int mixingHighSensorState = 0;
  int mixingLowSensorState = 0;
  unsigned long finalMillis = 0;
  unsigned long irrigateTime = 0;
  unsigned long currentMillis = 0;

  stateWaterIn();

  if((irrigate == true) && (waterHighSensorState == HIGH) && (generalState == 0)){
        
    generalState = 1;
    irrigate = false;
    
    do{
    
      do{

        stateWaterIn();
        digitalWrite(waterSpreadLed, LOW);
        digitalWrite(mixingInLed, LOW);

        waterLowSensorState = digitalRead(waterLowSensor);      
      }while(waterLowSensorState == LOW);

      stateWaterIn();
      digitalWrite(waterSpreadLed, HIGH);
      digitalWrite(mixingInLed, HIGH);
      
      mixingHighSensorState = digitalRead(mixingHighSensor);      
    }while(mixingHighSensorState == LOW);    
    
    digitalWrite(waterSpreadLed, LOW);
    digitalWrite(mixingInLed, LOW);
    
    if(fertilizer == true){
      
      generalState = 2;
      
      finalMillis = millis() + 5000;
      
      do{
        
        currentMillis = millis();
        
        stateWaterIn();
        
        digitalWrite(mixingLed, HIGH);
      }while(currentMillis < finalMillis);      
      
      digitalWrite(mixingLed, LOW);
    }
    
    generalState = 3;
    
    irrigateTime = millis() + (2000 + (percentage * 250));
    
    do{
    
      currentMillis = millis();
      
      stateWaterIn();
        
      mixingLowSensorState = digitalRead(mixingLowSensor); 
      digitalWrite(mixingSpreadLed, HIGH);
    }while((currentMillis < irrigateTime) && (mixingLowSensorState == HIGH));
    
    digitalWrite(mixingSpreadLed, LOW);
    generalState = 0;
  }
}

void stateWaterIn(){

  int waterHighSensorState = digitalRead(waterHighSensor);

  if (waterHighSensorState == LOW) {

    digitalWrite(waterInLed, HIGH);
  } else {
    
    digitalWrite(waterInLed, LOW);  
  }
}

void requestEvent(){
  
  byte i2cResponse[9];
  
  sensors.requestTemperatures();
  waterTemperature = sensors.getTempCByIndex(0);

  sensors2.requestTemperatures();
  humidityValue = sensors2.getTempCByIndex(0);

  i2cResponse[0] = (byte) generalState;
  
  i2cResponse[1] = (byte) (waterTemperature >> 24);
  i2cResponse[2] = (byte) (waterTemperature >> 16);
  i2cResponse[3] = (byte) (waterTemperature >> 8);
  i2cResponse[4] = (byte) waterTemperature;

  i2cResponse[5] = (byte) (humidityValue >> 24);
  i2cResponse[6] = (byte) (humidityValue >> 16);
  i2cResponse[7] = (byte) (humidityValue >> 8);
  i2cResponse[8] = (byte) humidityValue;      

  Wire.write(i2cResponse, 9);
}

void receiveEvent(int howMany){
    
  byte msg[2];
  
  int pos = 0;
    
  while(Wire.available()>0){

    msg[pos] = Wire.read();
    pos++;
  }
  
  if(msg[0] == 1){
    
    fertilizer = true;
  } else {
    
    fertilizer = false;
  }
  
  percentage = msg[1];
  
  irrigate = true;
}

void beep() {
  digitalWrite(waterInLed, HIGH);
  digitalWrite(waterSpreadLed, HIGH);
  digitalWrite(mixingInLed, HIGH);
  digitalWrite(mixingLed, HIGH);
  digitalWrite(mixingSpreadLed, HIGH);
  delayMicroseconds(1000);
  digitalWrite(waterInLed, LOW);
  digitalWrite(waterSpreadLed, LOW);
  digitalWrite(mixingInLed, LOW);
  digitalWrite(mixingLed, LOW);
  digitalWrite(mixingSpreadLed, LOW);  
}
