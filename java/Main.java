import components.*;
import java.io.*;
import java.net.*;

public class Main {

	public static void main(String[] args) {
	
		Main main = new Main();
		main.init(); 
	
	}
	
	public void init() {
	
		//new Thread(new CustomHttp()).start();
		new Thread(new Starter(false)).start();
		new Thread(new Starter(true)).start();
	}


}