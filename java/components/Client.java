package components;
import java.io.*; 
import java.net.*;
import org.json.JSONObject;
import org.json.JSONArray;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Client implements Runnable {

	private Socket csocket;
	private BufferedReader readerIn;
	public Thread thread;
	public Long id;
	public Data data;
	private Boolean ready;
	private PrintWriter pstream;
	Handler handler;
	
	Client(Socket csocket, Data data) {
	
		this.csocket = csocket;
		this.data = data;
		//connect(this);
		try
		{
			pstream = new PrintWriter(new OutputStreamWriter(csocket.getOutputStream(), StandardCharsets.UTF_8), true);
			readerIn = new BufferedReader(new InputStreamReader(csocket.getInputStream(), "UTF-8"));
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
	
	public void connectResponse(String res) {
	
		pstream.println(res);
		pstream.flush();
	
	}
   
   public void individualResponse(String res) {
   
		pstream.println(res);
		pstream.flush();
	
   }
	public void disconnect(Client client) {
   
		Data.removeClient(client);
                Handler.getPhotos2(client, true);
	}

   public void allResponse(String res) {
   
		for(Client client : data.getClients())
		{
			client.individualResponse(res);
		}
	}
   
   public void run() {
      try {
		 
		while (true)
		{
			String str = readerIn.readLine();
			if(str != null)
			{
					JSONObject obj = new JSONObject(str.trim());
					JSONArray handlers = obj.getJSONArray("handlers");
					for(int i = 0; i < handlers.length(); i++)
					{
						JSONObject handler = (JSONObject)handlers.get(i);
						String _class = handler.getString("_class").trim();
						String _method = handler.getString("_method").trim();
						Class c = Class.forName("components." + _class);
						Method m = c.getMethod(_method);
						Object t = c.getConstructor(JSONObject.class, Client.class, Data.class).newInstance(handler, this, data);
						m.invoke(t);
					}
					Thread.sleep(100); // Задержка перед тем как отправить(delay before send answer)
					data.sendHandlers(this);
			}
			else
			{
				csocket.close();
				disconnect(this);
				break;
			}
		}
      }
		catch (IOException e) {
			try{
				csocket.close();
                                disconnect(this);
			}
			catch(IOException x)
			{
				System.out.println(x);
                                disconnect(this);
			}
		}
		catch(InterruptedException e)
		{
			System.out.println(e.toString());
                        disconnect(this);
		}
		catch(NoSuchMethodException e) {
			System.out.println(e.toString());
                        disconnect(this);
		}
		catch(InvocationTargetException e) {
			System.out.println(e.getTargetException());
                        disconnect(this);
		}
		catch (InstantiationException x) {
			x.printStackTrace();
                        disconnect(this);
		}
		catch (IllegalAccessException x) {
			x.printStackTrace();
                        disconnect(this);
		}
		catch(ClassNotFoundException e) {
			System.out.println(e.toString());
                        disconnect(this);
		}
   }
}