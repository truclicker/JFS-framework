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
	private int Min = 1;
	private int Max = 99;
	public void run() {
      
		Data.numbersArray = new ArrayList();
		for(int j = Data.Min; j <= Data.Max; j++)
		{
			Data.numbersArray.add(j);
		}
		
		while(true)
		{
			
			try
			{
				if(Data.gameTime == 0)
				{
					Data.activeTimer = false;
					Handler.showMessageWhoNotPlay("1", false);
					Collections.shuffle(Data.numbersArray);
					String hashResult = Base64.encode(Data.numbersArray.toString());
                                        String numbers = Data.numbersArray.toString();
                                        try {
                                            MessageDigest md = MessageDigest.getInstance("MD5");
                                                md.update(numbers.getBytes());
                                                byte[] digest = md.digest();
                                                StringBuffer sb = new StringBuffer();
                                                for (byte b : digest) {
                                                        sb.append(String.format("%02x", b & 0xff));
                                                }
                                                Data.md5 = sb.toString();
                                        }
                                        catch(NoSuchAlgorithmException e)
                                        {
                                            System.out.println(e);
                                        }
                                        System.out.println("numbers are: " + Data.numbersArray.toString());
                                        JSONObject ticketPrice = Data.db.executeQuery("SELECT * FROM sillaru_data WHERE name = 'loto_ticket_price'", true);
                                        Data.db.executeUpdate("UPDATE rooms SET amount = " + Data.totalWin + " WHERE id = " + Data.roomId);
                                       
					int roomId = Data.db.executeUpdate("INSERT INTO rooms (price, result, amount) VALUES(" + ticketPrice.getDouble("value") + ", '" + hashResult + "', 0)");
					JSONObject res = Data.db.executeQuery("SELECT * FROM rooms WHERE id = " + roomId, true);
					Data.ticketPrice = res.getDouble("price");
                                        Data.gameTime = 30000;
                                        Data.messages = new JSONArray();
					Handler.timeOut(roomId);
					Data.totalWin = 0;
					Data.roomId = roomId;
                                         System.out.println("Data.roomId: " + Data.roomId);
					Thread.sleep(2000);
					Data.activeTimer = true;
					
				}
				if(Data.activeTimer)
				{
					Data.gameTime -= interval;
				}
				Thread.sleep(interval);
			
			}
			catch(InterruptedException e)
			{
				System.out.println(e);
			
			}
		}

		
	}
}