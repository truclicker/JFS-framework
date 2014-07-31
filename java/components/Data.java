package components;
import java.io.*; 
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONArray;
import java.math.BigDecimal;
import java.lang.Math;

public class Data{

	public static ArrayList clients;
	public static DB db = new DB();
	public static Data init() {
	
                JSONObject ticketPrice = Data.db.executeQuery("SELECT * FROM sillaru_data WHERE name = 'loto_ticket_price'", true);
                Data.ticketPrice = ticketPrice.getDouble("value");
		Data data = new Data();
		return data;
	}
	Data()
	{
		response = new JSONObject();
		response.put("handlers", new JSONArray());
	}
	
	public synchronized void addHandler(JSONObject handler) {
	
		response.getJSONArray("handlers").put(handler);
	}
	
	public synchronized static void addHandlerAll(JSONObject handler) {
	
		for(int i = 0; i < clients.size(); i++)
		{
			
			Client client = (Client)clients.get(i);
			client.data.addHandler(handler);
		
		}
	
	}
	
	public void sendHandlers(Client client) {
	
		String res = getData();
		client.individualResponse(res);
		response.put("handlers", new JSONArray());
	}
	
	public static JSONArray getNotPlayingClients() {
	
		JSONArray retClients = new JSONArray();
		for(int i = 0; i < clients.size(); i++)
		{
			Client client = (Client)clients.get(i);
			if(client.data.playing == false)
			{
				retClients.put(client);
			}
		}
		
		return retClients;
	}
	
	
	public static void addClient(Client client, Long id) {
	
		JSONObject user = new JSONObject();
		user.put("id", id);
		client.id = id;
		users.put(user);
		clients.add(client);
	}
        
	public static int clientIsOnline(Long id) {
	
		for(int i = 0; i < clients.size(); i++)
		{
			Client client = (Client)clients.get(i);
			if(client.id.equals(id))
			{
				return i;
			}
		}
                
                return -1;
                
	}
        
	public static Client getClient(int id) {
	
		return (Client)clients.get(id);
                
	}
	
	public static void removeClient(Client client) {
	
		for(int i = 0; i < users.length(); i++)
		{
			JSONObject user = (JSONObject)users.get(i);
			if(user.getInt("id") == client.id)
			{
				users.remove(i);
                                Data.db.executeUpdate("UPDATE sillaru_users SET online = 0 WHERE id = " + client.id);
				break;
			}
		
		}
		
		clients.remove(client);
	}
	
	public static ArrayList<Client> getClients() {
	
		return clients;
	}
	
	
   
   
   public String getUserCount() {
   
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("class", "Handler");
		jsonObj.put("method", "setUsercount");
		JSONObject params = new JSONObject();
		params.put("playersCount", clients.size());
		jsonObj.put("params", params.toString());
		return jsonObj.toString();
	}
	
	public String getData() {
	
		return response.toString();
	}
	
}