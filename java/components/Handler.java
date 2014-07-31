package components;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import java.util.Collections;
import org.json.JSONArray;
import it.sauronsoftware.base64.Base64;
import java.util.List;
import java.util.Arrays;
import java.text.DecimalFormat;
import java.math.BigDecimal;
public class Handler{

	Data data;
	JSONObject handler;
	JSONObject params;
	Client client;
        
	public Handler(JSONObject obj, Client client, Data data) {
	
		params = obj.getJSONObject("_params");
		this.data = data;
		this.client = client;
		handler = new JSONObject();
		handler.put("_class", obj.getString("_class"));
		handler.put("_method", obj.getString("_method"));
		
	}
	
	public void getUsers() {
	
                //System.out.println("getting users");
		params.put("users", Data.users);
                params.put("notPlayingUsers", Data.getNotPlayingClients());
		handler.put("_params", params.toString());
		data.addHandler(handler);
	
	}
	
	public void connect() {
		System.out.println(params);
		Long user_id = params.getLong("userId");
                System.out.println("user connected and his user_id = " + user_id);
                Boolean alreadyExist = false;
                
                for(int i = 0; i < Data.getClients().size(); i++)
                {
                    Client client = Data.getClient(i);
                    if(user_id == client.id)
                    {
                        alreadyExist = true;
                        break;
                    }
                }
                
		if(Data.db.exists("SELECT * FROM sillaru_users WHERE id = " + user_id))
		{
                    if(!alreadyExist)
                    {
                        client.id = user_id;
						JSONObject user = Data.db.executeQuery("SELECT * FROM sillaru_users WHERE id = " + user_id, true);
						Data.db.executeUpdate("UPDATE sillaru_users SET online = 1 WHERE id = " + user_id); 
						params.put("users", Data.users);
						params.put(user);
						handler.put("_params", params.toString());
						data.addHandler(handler);
                    }
                    else  
                    {
						System.out.println("user exist, but already logged");
                    }
            }
			else
			{
						
				System.out.println("user doesn't exist");
			}
                
        }
}