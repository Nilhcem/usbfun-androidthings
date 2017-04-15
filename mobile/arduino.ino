#include "pitches.h"

char data;
int buzzerPin = 8;
int duration = 500;
int notes[] = { NOTE_C5, NOTE_D5, NOTE_E5, NOTE_F5, NOTE_G5, NOTE_A5, NOTE_B5, NOTE_C6, NOTE_D6, NOTE_E6, NOTE_F6, NOTE_G6 };
String notesStr[] = { "C5", "D5", "E5", "F5", "G5", "A5", "B5", "C6", "D6", "E6", "F6", "G6" };

void setup() {
  Serial.begin(115200);
  Serial.println("Arduino is ready!");
}

void loop() {
  if (Serial.available()) {
    data = Serial.read();
    if (data >= 0 && data < sizeof(notes) / sizeof(char *)) {
      tone(buzzerPin, notes[data], duration);
    }

    Serial.println(notesStr[data]);
  }
}
