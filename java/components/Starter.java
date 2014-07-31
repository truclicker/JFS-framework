package components;
import java.io.*; 
import java.net.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
public class Starter implements Runnable {

	public Boolean isPolicy;
        public static String ip = "127.0.0.1";
	public static int port = 255;
	public static int policyPort = 843;
	
	public Starter(Boolean isPolicy) { 
		this.isPolicy = isPolicy;
	}
   
   
   public void run()  {
                 
		if(isPolicy)
		{
                    
			try
			{
			
				InetAddress addr = InetAddress.getByName(ip);
				ServerSocket socket = new ServerSocket(policyPort, 0, addr);
                                
				while (true) {
                                        
					Socket ssocket = socket.accept();
                                        System.out.println("here");
					//System.out.println("Connected to policy server");
					new Thread(new PolicyMultiThreadedServer(ssocket)).start();
				}
			}
			catch (Exception e)
			{
				System.out.println(e);
			}
		}
		else
		{
			
			try
			{
				InetAddress addr = InetAddress.getByName(ip);
				ServerSocket socket = new ServerSocket(port, 0, addr);
				int oldsize = socket.getReceiveBufferSize();
				socket.setReceiveBufferSize(oldsize * 2);
				Data.db.connect(Config.dbName);
				Data.clients = new ArrayList<Client>();
				new Thread(new Pinger()).start();
				Data.activeTimer = true;
                               
				while (true) {
					Socket ssocket = socket.accept();
					Data data = Data.init();
					//System.out.println("Connected to real server");
					Client client = new Client(ssocket, data);
					Thread t = new Thread(client);
					client.thread = t;
					client.id = t.getId();
					t.start();
					
					//System.out.println(Data.getThreads().size());
				}
			}
			catch (Exception e)
			{ 
				System.out.println(e); 
			}
			
		}
		
		
   }
}