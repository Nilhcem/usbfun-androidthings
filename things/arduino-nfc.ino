#include <SPI.h>
#include <PN532_SPI.h>
#include "PN532.h"

PN532_SPI pn532spi(SPI, 10);
PN532 nfc(pn532spi);

void setup(void) {
  Serial.begin(115200);

  nfc.begin();
  uint32_t versiondata = nfc.getFirmwareVersion();
  if (!versiondata) {
    Serial.print("Didn't find PN53x board. Halt");
    while (1);
  }
  nfc.setPassiveActivationRetries(0xFF);
  nfc.SAMConfig();

  Serial.println("Waiting for an ISO14443A card");
}

void loop(void) {
  boolean success;
  uint8_t uid[] = { 0, 0, 0, 0, 0, 0, 0 };
  uint8_t uidLength;

  success = nfc.readPassiveTargetID(PN532_MIFARE_ISO14443A, &uid[0], &uidLength);
  if (success) {
    for (int8_t i=uidLength - 1; i >= 0; i--) {
      Serial.print(uid[i], HEX);
    }
    Serial.println("");
    delay(1000);
  }
}
