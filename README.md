# ilioTway
Send your last tweet in PigLatin to your phone!

#Overview
This program takes in a ten-digit phone number in the US and a Twitter handle both from the keyboard. Next, the program retrieves the last tweet from the account with that Twitter handle. Then, the program Pig Latinizes the text in the tweet and uses the Twilio Platform to text the Pig Latinized tweet to the phone number that was inputted in the beginning of the program.

#Usage

##### 1. Since our program utilizes the Twilio Platform, you will need to make an account. 
###### a. Head to: https://www.twilio.com/try-twilio
###### b. Enter information as prompted. **It is important to select “Java” as the language** Click “Get Started”.

![pic1](/imgs/Picture1.png)

###### c. Twilio will ask for a phone number. Enter the number and you will receive a verification code or a phone call (whichever you click on).

![pic3](/imgs/Picture3.png)

###### d. A page asking for the verification code (sent to your phone number by call or text) will pop up. Enter the verification code (My code was 6 digits long).

##### 2. Now that the account has been created, some additional steps must be taken in order for the program to work.
###### a. Go to: https://www.twilio.com/user/account/phone-numbers/getting-started You should now be on the page below. Click “Get your first Twilio phone number”

![pic2](/imgs/Picture2.png)

###### b. A dialogue box will pop up. Click “Choose this number”. After waiting a few seconds, click “Done”.
##### 3. Now you need to set a class path.
##### 4. When prompted, input a ten-digit phone number. You will be prompted again for the Twitter handle. Input the full Twitter handle (including the “@” sign). (you should also add your API tokens)
##### 5. A Pig Latinized message will arrive at the number entered.
