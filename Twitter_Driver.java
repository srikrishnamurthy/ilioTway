import twitter4j.*;       //set the classpath to lib\twitter4j-core-4.0.2.jar
import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;
import com.twilio.sdk.*;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import com.twilio.sdk.resource.instance.*;
import org.apache.http.*;
import org.apache.http.message.*;

public class Twitter_Driver
{
   private static PrintStream consolePrint;
   public static String ACCOUNT_SID = "{GET YOUR OWN SID}";
   public static String AUTH_TOKEN = "{GET YOUR OWN AUTH TOKEN}";
   
   public static void main (String []args) throws TwitterException, IOException, TwilioRestException
   {
      consolePrint = System.out;
      
      // PART 1
      // set up classpath and properties file
      
      System.out.println("Enter your Twilio phone number (no spaces)");
      Scanner sc = new Scanner(System.in);
      String Tnumber = sc.nextLine();
      number = "+1" + number;
      
      System.out.println("Enter your ACCOUNT_SID (no spaces)");
      Scanner sc = new Scanner(System.in);
      ACCOUNT_SID = sc.nextLine();
      
      System.out.println("Enter your AUTH_TOKEN (no spaces)");
      Scanner sc = new Scanner(System.in);
      AUTH_TOKEN = sc.nextLine();
      
      System.out.println("Enter the phone number you wish to send the message to (no spaces)");
      Scanner sc = new Scanner(System.in);
      String number = sc.nextLine();
      number = "+1" + number;
      
      System.out.println("Enter the twitter handle");
      String handle = sc.nextLine();
      RoseTwitter bigBird = new RoseTwitter(consolePrint);
      String lastTweet = bigBird.getLastTweet(handle);
      //create message to tweet, then call the tweetOut method
      TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
      String res_to_text = "";
      String[] roses = lastTweet.split(" ");
      
      for(int i = 0; i < roses.length; i++){
         res_to_text += PigLatin.pig(roses[i]) + " ";
      }      
      
    // Build a filter for the MessageList
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new BasicNameValuePair("Body", res_to_text));
      params.add(new BasicNameValuePair("To", number));
      params.add(new BasicNameValuePair("From", Tnumber));
   
      MessageFactory messageFactory = client.getAccount().getMessageFactory();
      Message message = messageFactory.create(params);
      System.out.println(message.getSid());
         
   }//end main         
         
}//end driver        
         
class RoseTwitter 
{
   private Twitter twitter;
   private PrintStream consolePrint;
   private List<Status> statuses;
   private List<String> sortedTerms;
   
   public RoseTwitter(PrintStream console){
      // Makes an instance of Twitter - this is re-useable and thread safe.
      twitter = TwitterFactory.getSingleton(); //connects to Twitter and performs authorizations
      consolePrint = console;
      statuses = new ArrayList<Status>();
      sortedTerms = new ArrayList<String>();   
   }
   
   /******************  Part 1 *******************/
   public String getLastTweet(String handle) throws TwitterException, IOException{
      statuses.clear();
      sortedTerms.clear();
      PrintStream fileout = new PrintStream(new FileOutputStream("tweets.txt")); // Creates file for dedebugging purposes
      Paging page = new Paging (1,1);
      statuses.addAll(twitter.getUserTimeline(handle,page)); 
      
      int numberTweets = statuses.size();
      fileout.println("Number of tweets = " + numberTweets);
      
      fileout = new PrintStream(new FileOutputStream("garbageOutput.txt"));
   
      int count=1;
      String s = null;
      for (Status j: statuses)
      {
         s = j.getText();
         count++;
      }
      return s;	
   }  
}  

class PigLatin
{
   public static String pig(String s)
   {
      if(s.length() < 1)
         return s;
      int pos = -1;
      boolean alterpunct = false;
      String result;
      char[] strAr = s.toCharArray();
      String vowelStr = "aeiouAEIOU";
      char[] vowelAr = vowelStr.toCharArray();
      String vowelStrU = "aeioAEIO";
      char[] vowelArU = vowelStrU.toCharArray();
      
      //check whether punctuation is first or not
      if(hasFirstPunct(s))
      {
         s = s.substring(1) + s.substring(0,1);
         alterpunct = true;
      }
      
      if(checkVowel(vowelAr, s) || checkY(s))
      { 
         //starts with consonant or punctuation
         if(!checkVowelPos(vowelAr, s))
         {
            //"QU" case
            String temp = s.toLowerCase();
            if(temp.indexOf("qu") > -1)
            {
               int n = temp.indexOf("qu");
               for(int i = 0; i < s.length(); i++)
               {
                  for(int j = 0; j < vowelArU.length; j++)
                     if(s.charAt(i) == vowelArU[j])
                     {
                        pos = s.indexOf(vowelArU[j]);
                        break;
                     }
                  if(pos > -1)
                     break;  
               }
               if(pos < n && pos > -1)
               {
                  if(hasFirstCap(s))                
                     result = capConsonant(s, pos);
                  else
                     result = s.substring(pos) + s.substring(0,pos) + "ay";
                  if(hasPunct(result))          
                     result = punctCon(result, alterpunct);
                  return result;
               }
               else
               {
                  if(hasFirstCap(s))               
                     result = capConsonant(s, pos);
                  else
                     result = s.substring(n+2) + s.substring(0,n+2) + "ay";
                  if(hasPunct(result))          
                     result = punctCon(result, alterpunct);
                  return result;
               }
            }
            //dealing with Y
            else if(temp.indexOf('y') > -1)
            {
               //if Y is first letter, treat as consonant
               if(temp.indexOf('y') == 0)
               {
                  for(int i = 0; i < s.length(); i++)
                  {
                     for(int j = 0; j < vowelAr.length; j++)
                        if(s.charAt(i) == vowelAr[j])
                        {
                           pos = s.indexOf(vowelAr[j]);
                           break;
                        }
                     if(pos > -1)
                        break;  
                  }
                  if(hasFirstCap(s))             
                     result = capConsonant(s, pos);
                  else
                     result = s.substring(pos) + s.substring(0,pos) + "ay";
                  if(hasPunct(result))          
                     result = punctCon(result, alterpunct);
                  return result;
               }
               
               //if Y appears later, treat as vowel
               else
               {
                  int n = temp.indexOf("y", 1);
                  for(int i = 0; i < s.length(); i++)
                  {
                     for(int j = 0; j < vowelAr.length; j++)
                        if(s.charAt(i) == vowelAr[j])
                        {
                           pos = s.indexOf(vowelAr[j]);
                           break;
                        }
                     if(pos > -1)
                        break;  
                  } 
                  if(pos > -1 && pos < n)
                  {
                     if(hasFirstCap(s))                
                        result = capConsonant(s, pos);
                     else
                        result = s.substring(pos) + s.substring(0,pos) + "ay";
                     if(hasPunct(result))
                        result = punctCon(result, alterpunct);
                     return result;
                  }
                  else
                  {
                     if(hasFirstCap(s))             
                        result = capConsonant(s, n);
                     else
                        result = s.substring(n) + s.substring(0,n) + "ay";
                     if(hasPunct(result))           
                        result = punctCon(result, alterpunct);
                     return result;
                  }
               }
            }
            //regular case
            else
            {
               for(int i = 0; i < s.length(); i++)
               {
                  for(int j = 0; j < vowelAr.length; j++)
                     if(s.charAt(i) == vowelAr[j])
                     {
                        pos = s.indexOf(vowelAr[j]);
                        break;
                     }
                  if(pos > -1)
                     break;  
               }           
               if(hasFirstCap(s))                
                  result = capConsonant(s, pos);
               else
                  result = s.substring(pos) + s.substring(0,pos) + "ay";
               if(hasPunct(result))           
                  result = punctCon(result, alterpunct);
               return result;
            }
         }
         //starts with vowel
         else
         {
            result = s + "way";
            if(hasPunct(result))       
               result = punctCon(result, alterpunct);
            return result;
         }
      }
      else
      {
         return "INVALID";
      }
   }
   
   public static boolean checkVowel(char[] vowelAr, String s)
   {
      int pos = -1;
      for(int i = 0; i < s.length(); i++)
      {
         for(int j = 0; j < vowelAr.length; j++)
            if(s.charAt(i) == vowelAr[j])
            {
               pos = s.indexOf(vowelAr[j]);
               return true;
            }
         if(pos > -1)
            break;
      }
      return false;
   }
   
   public static boolean checkVowelPos(char[] vowelAr, String s)
   {
      for(int i = 0; i < vowelAr.length; i++)
      {
         if(s.indexOf(vowelAr[i]) == 0)
            return true;
      }
      return false;
   }
   
   public static boolean checkY(String s)
   {
      if(s.indexOf('y') > -1 || s.indexOf('Y') > -1)
         return true;
      return false;
   }
   
   public static boolean hasFirstCap(String s)
   {
      char car = s.charAt(0);
      if(Character.isUpperCase(car))
         return true;
      return false;
   }
   
   public static String capConsonant(String s, int pos)
   {
      String front = s.substring(0, 1);
      front = front.toLowerCase();
      String result = s.substring(pos) + front + s.substring(1,pos) + "ay";
   
      String next = result.substring(0, 1);
      next = next.toUpperCase();
      result = next + result.substring(1);
      return result;
   }
   
   public static boolean hasFirstPunct(String s)
   {
      String punctuation = ".,?!:;\"(){}<>";
      char[] punctAr = punctuation.toCharArray();
      
      char car = s.charAt(0);
      
      for(int i = 6; i < punctAr.length; i++)
         if(car == punctAr[i])
            return true;
   
      return false;
   }
   
   public static String punctCon(String s, boolean alterpunct) //works without excess punctuation (like 2 !'s)
   //Takes the Latinized String and straightens out the punctuation//
   //0-5 external
   //6-14 internal
   {
      String punctuation = ".,?!:;\"(){}<>";
      char[] punctAr = punctuation.toCharArray();
      int pos = -1;
      int arrayJ = 0; //keeps track of position of which mark
      String pt5 = ""; //keeps track of actual punctuation
      char temp = ' '; //keeps track of actual punctuation
      String result;
      int count6 = 0; //keeps count of bounding punctuation
      
      //find(0-5) and move first
      for(int i = 0; i < s.length(); i++)
      {
         for(int j = 0; j < punctAr.length; j++)
            if(s.charAt(i) == punctAr[j])
            {
               pos = s.indexOf(punctAr[j]);
               arrayJ = j;
               break;
            }
         if(pos > -1)
            break;  
      }
      if(arrayJ < 6)
      {
         temp = punctAr[arrayJ];
         pt5 = Character.toString(temp);
         result = s.substring(0,pos) + s.substring(pos+1) + pt5;
      }
      else
      {
         result = s;
      }
      
      //return s;
         
      if(hasIntPunct(s))
      {
         pos = -1;
         for(int i = 0; i < s.length(); i++)
         {
            for(int j = 6; j < punctAr.length; j++)
               if(s.charAt(i) == punctAr[j])
               {
                  pos = s.indexOf(punctAr[j]);
                  break;
               }
            if(pos > -1)
               break;
         }
         for(int i = 0; i < s.length(); i++)
         {
            for(int j = 6; j < punctAr.length; j++)
               if(s.charAt(i) == punctAr[j])
                  count6++;
         }
      }
      else
      {
         return result;
      }
      temp = punctAr[arrayJ];
      pt5 = Character.toString(temp);
      pos = s.indexOf(pt5);
      if(count6 > 1)
      {
         result = result.charAt(pos+1) + result.substring(0,pos) + result.substring(pos+2) + result.charAt(pos);
      }
      else
      {
         if(alterpunct)
            result = result.charAt(pos) + result.substring(0,pos) + result.substring(pos+1);
         else
            result = result.substring(0,pos) + result.substring(pos+1) + result.charAt(pos);
         
      }
      return result;
   }
   
   public static boolean hasPunct(String s)
   {
      String punctuation = ".,?!:;\"(){}<>";
      char[] punctAr = punctuation.toCharArray();
      
      for(int i = 0; i < s.length(); i++)
      {
         for(int j = 0; j < punctAr.length; j++)
            if(s.charAt(i) == punctAr[j])
               return true;
      }
      return false;
   }
   
   public static boolean hasIntPunct(String s)
   {
      String punctuation = ".,?!:;\"(){}<>";
      char[] punctAr = punctuation.toCharArray();
      
      for(int i = 0; i < s.length(); i++)
      {
         for(int j = 6; j < punctAr.length; j++)
            if(s.charAt(i) == punctAr[j])
               return true;
      }
      return false;
   }

}