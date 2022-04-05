Heavy enhancements to old GeoCoding repo; further details can be found inside.
As I made this for testing purposes only, and to get better on the usage of this IDE, I apoligize for dumb mistakes/useless/bad implementations.
Protocol is as following:


NOTE: APP NOT MADE FOR PROFESSIONAL PURPOSES - ENTIRELY MADE FOR FUN/PASSION.

for context, reference: openweathermap.org

PROTOCOL:
-- GEOCODING SECTION (OPENWEATHER API) --

Example of API call:
"http://api.openweathermap.org/geo/1.0/direct?q={CITY NAME}&limit=5&appid={API key}"
URL returns a json file.

(CAN EITHER USE GOOGLE MAPS API, WHICH RETURNS A XML FILE).

"LIMIT" field shows every city in the world called "CITY NAME".
Field with Value "5" shows a maximun of five cities named "CITY NAME".

Values to take from the JSON: Latitude and Longitude values.

When user pushes the "Locate button" the afromentioned values should display on screen.
GeoCoding section ends.

-- GEOCODING SECTION (GOOGLE MAPS API) --

Example of API call:
https://maps.googleapis.com/maps/api/geocode/xml?address={CITY NAME}&key={API KEY}

Google Maps API grants the choice of the format.
The example returns an xml file.

Likewise, when the button is pressed, Latitude and Longitude values should display on screen.
