# Basic-Android-App

Front-end App Specifications
• The mobile app is written natively for Android in Java.
• The app works on a Google Pixel 3 emulator running Android Q (API 29).

My Favorite City
• Opens Google Maps and shows the location of my favorite city (excluding Vancouver),
with the name of the city clearly visible as the screen opens, without the need to make any
additional clicks or gestures.

Phone Details
• Displays on the screen the name of the city where the phone is located, phone
manufacturer, and phone model.
  o On a real device, the phone manufacturer and model could be “Google” and “Pixel 3”.
  o On an emulator, it could be: “Google” and “Android SDK built for X86”.

Login and Server Info
• Opens the login screen of Google or Facebook to authenticate the user
• Once the user is authenticated, it connects to the back-end and displays the following
information:
  o Server public IP address (either IPV4 or IPV6)
  o Client IP address
  o Server local time (hh:mm:ss)
  o Client local time (hh:mm:ss)
  o Your name, obtained via a back-end API (first, last)
  o The name of the user logged into the app with their Google/Facebook credentials
(first, last)

Camera and Details
• (Yet to be implemented)

Back-end Server Specifications (Yet to be implemented)
• The back-end is implemented in Node.js
  o Uses MongoDB as database
• Hosted on a cloud infrastructure
• On the server, create three APIs, to return
  1. Server IP address
  2. Server local time, when the API is called (hh:mm:ss)
  3. Your first and last name
