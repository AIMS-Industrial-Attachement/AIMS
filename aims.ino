#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include<FirebaseHttpClient.h>
#include <DHT.h>  
#include<time.h>

#define FIREBASE_HOST "aims-56e59.firebaseio.com"
#define FIREBASE_AUTH "aVoA4BzLLdLeTeCYpMkcmBiuX3huPOMPdmvcWWnc"
#define WIFI_SSID "Innovative Soft"  
#define WIFI_PASSWORD "027214172"
#define DHTTYPE DHT22   // DHT 22
#define Relay_ch_1  16   //NodeMCUESP8266  D0 
#define Relay_ch_2  5   ///NodeMCUESP8266 D1 
#define Relay_ch_3  4   ///NodeMCUESP8266 D2 
#define Relay_ch_4  0   ///NodeMCUESP8266 D3 
#define DHTPIN 2          // Node MCU ESP8266 (D4)
//Put fingerprint genertaed from here in HTTPClient.h file of this "aims-56e59.firebaseio.com" link, https://www.grc.com/fingerprints.htm


DHT dht(DHTPIN, DHTTYPE);
int timeZone = 6*3600;
int dst = 0;
float Temperature;
float Humidity;

void setup() {
  
  Serial.begin(9600);
  
  //Sensor...
  pinMode(DHTPIN, INPUT);
  dht.begin(); 
  
  //Relay Pin...
  pinMode(Relay_ch_1,OUTPUT);
  pinMode(Relay_ch_2,OUTPUT);
  pinMode(Relay_ch_3,OUTPUT);
  pinMode(Relay_ch_4,OUTPUT);

// connect to wifi...
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println("");
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  
  //Time Connection...
  configTime(timeZone, dst, "pool.ntp.org","time.nist.gov");
  Serial.println("\nWaiting for time");
  while(!time(nullptr)){
    Serial.print("*");
  }
  Serial.println("time responsed");
}
void loop() {
  
  //Sensor Data Reading...
  Temperature = dht.readTemperature(); // Gets the values of the temperature
  Humidity = dht.readHumidity(); 
  
  // Gets the values of the humidity 
  if((String)Humidity=="nan" || (String)Temperature=="nan"){
    Firebase.setString("Readings/Temperature", "0.0");
    Firebase.setString("Readings/Humidity", "00.00");
    Firebase.setString("Readings/dht", "0");
  }
  else
  {
    Firebase.setString("Readings/dht", "1");
    Serial.print("Temperature: ");
    Serial.println(Temperature);
    Firebase.setString("Readings/Temperature", (String)Temperature);
    Serial.print("Humidity: ");
    Serial.println(Humidity);
    Firebase.setString("Readings/Humidity", (String)Humidity);
  }
  
  //Relay Control...
  String ch1_sts = Firebase.getString("Readings/ch_1");
  String ch2_sts = Firebase.getString("Readings/ch_2");
  String ch3_sts = Firebase.getString("Readings/ch_3");
  String ch4_sts = Firebase.getString("Readings/ch_4");
  //channel 1...
  if(ch1_sts=="1")
    digitalWrite(Relay_ch_1,HIGH);
  else
    digitalWrite(Relay_ch_1,LOW);
  //Channel 2....
  if(ch2_sts=="1")
    digitalWrite(Relay_ch_2,HIGH);
  else
    digitalWrite(Relay_ch_2,LOW);
  //Channel 3...
  if(ch3_sts=="1")
    digitalWrite(Relay_ch_3,HIGH);
  else
    digitalWrite(Relay_ch_3,LOW);
  //Channel 4...
  if(ch4_sts=="1")
    digitalWrite(Relay_ch_4,HIGH);
  else
    digitalWrite(Relay_ch_4,LOW);


  ///Emergency.....
  if(Temperature>=40.00){
    digitalWrite(Relay_ch_1,LOW);
    digitalWrite(Relay_ch_2,LOW);
    digitalWrite(Relay_ch_3,LOW);
    digitalWrite(Relay_ch_4,LOW);
    Firebase.setString("Readings/ch_1", "0");
    Firebase.setString("Readings/ch_2", "0");
    Firebase.setString("Readings/ch_3", "0");
    Firebase.setString("Readings/ch_4", "0"); 
  }

  //Time Setup...
  time_t now = time(nullptr);
  struct tm* p_tm = localtime(&now);
  int day = (int)p_tm->tm_mday;
  int month = (int)p_tm->tm_mon+1;
  int year = (int)p_tm->tm_year + 1900; 
  String date = (String)day + "/" + (String)month + "/" + (String)year;
  Firebase.setString("Readings/day",  date); 
  Serial.println(date);
  int hr = (int)p_tm->tm_hour;
  int mnt = (int)p_tm->tm_min;
  String Time_ = (String)hr + ":" + (String)mnt;
  Firebase.setString("Readings/time",  Time_); 
  Serial.println(Time_);

  //Liquied Level Sensor...
  int liquied = analogRead(A0);
  Serial.println(liquied);
  Firebase.setString("Readings/w_level",  (String)liquied); 
 
  // handle error...
  if (Firebase.failed()) {
  Serial.print("setting /number failed:");
  Serial.println(Firebase.error());  
  return;
  }
}
