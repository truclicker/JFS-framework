package components;
import java.io.*; 
import java.net.*; 

public class PolicyMultiThreadedServer implements Runnable {
	Socket csocket;
    BufferedReader readerIn; 
    private String flashPolicy = "<?xml version=\"1.0\"?>\n" +
"<!DOCTYPE cross-domain-policy SYSTEM \"/xml/dtds/cross-domain-policy.dtd\">\n" +
"<cross-domain-policy>\n" +
"   <allow-access-from domain=\"*\" to-ports=\"*\" />\n" +
"</cross-domain-policy>";
	PolicyMultiThreadedServer(Socket csocket) {
		this.csocket = csocket;
	}
   
   
   public void run() {
      try {
         PrintStream pstream = new PrintStream(csocket.getOutputStream());
		 readerIn = new BufferedReader(new InputStreamReader(csocket.getInputStream()));
		 //String flashPolicy = Filer.read("components/flashPolicy.txt");
                 
         pstream.print(flashPolicy + "\0");
      }
      catch (IOException e) {
         System.out.println(e);
      }
   }
}