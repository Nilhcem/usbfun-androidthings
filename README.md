# USB fun - Android (Things)

A demonstration of how to connect an Arduino to an Android phone / Android Things board over USB.

Blog post:  
[http://nilhcem.com/android-things/usb-communications](http://nilhcem.com/android-things/usb-communications)

This project has 2 gradle modules: `mobile` and `things`

## Mobile module

**Description:**  
The Android smartphone sends events to the Arduino, so that it can play music.

**Arduino file:**  
Add the `mobile/pitches.zip` library, and flash the `mobile/arduino.ino` file.

### Schematic

![schematic-mobile][]

Also, connect the Arduino to an Android smartphone (USB-OTG + USB cables)


## Things module

**Description:**  
The Arduino sends events to the Android Things board which will log everything.

**Arduino files:**  
Flash the `things/arduino-hello.ino` file.

## License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[schematic-mobile]: https://raw.githubusercontent.com/Nilhcem/usbfun-androidthings/master/schematic_mobile.png
