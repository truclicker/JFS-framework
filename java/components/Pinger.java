package components;
import java.io.*; 
import java.net.*;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Collections;
import it.sauronsoftware.base64.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class Pinger implements Runnable {

   
	private int interval = 1000;
	public void run() {
      
		
		while(true)
		{
			
			try
			{
				// do something
				
				Thread.sleep(interval);
			
			}
			catch(InterruptedException e)
			{
				System.out.println(e);
			
			}
		}

		
	}
}