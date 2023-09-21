# Basic-Android-App

Front-end App Specifications
- The mobile app is written natively for Android in Java
- The app works on a Google Pixel 3 emulator running Android Q (API 29)

My Favorite City
- Opens Google Maps and shows the location of my favorite city (excluding Vancouver),
with the name of the city clearly visible as the screen opens, without the need to make any
additional clicks or gestures.

Phone Details
- Displays on the screen the name of the city where the phone is located, phone
manufacturer, and phone model
  - On a real device, the phone manufacturer and model could be “Google” and “Pixel 3”
  - On an emulator, it could be: “Google” and “Android SDK built for X86”

Login and Server Info
- Opens the login screen of Google to authenticate the user
- Once the user is authenticated, it connects to the back-end and displays the following information:
  - Server public IP address (either IPV4 or IPV6)
  - Client IP address
  - Server local time (hh:mm:ss)
  - Client local time (hh:mm:ss)
  - Your name, obtained via a back-end API (first, last)
  - The name of the user logged into the app with their Google/Facebook credentials (first, last)

Wizard Animation, Screenshots, and Email
- User can control a sprite by pressing buttons, making it perform several actions
- User has option to press a button that takes a screenshot and sends data to MongoDB database
  - The screenshot is saved as a Bitmap that is then encoded to a string
- User has option to press another button that collects all the "screenshots" from the database. An email is also sent to recipient(s) of the user's choice on how many "screenshots" are on the database along with the action the sprite performed last
  - The strings collected from the database are converted back to Bitmaps that can be displayed on the screen

Back-end Server Specifications
- The back-end is implemented in Node.js
- Uses MongoDB as a database
- Hosted on AWS EC2 Virtual Machine
- Included API endpoints that
  1. return server IP address
  2. return server local time, when the API is called (hh:mm:ss)
  3. return my first and last name
  4. post encoded strings of Bitmaps to a MongoDB database
  5. return all data on the same MongoDB database
