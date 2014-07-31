package components;
 
import java.io.*;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Iterator;
public class Filer {
 
	private static String str;
	public static String read(String file) throws IOException {
		StringBuilder builder = new StringBuilder();
		try (InputStream input = new FileInputStream(file)) {
			BufferedReader reader = new BufferedReader(
				new InputStreamReader(input, "UTF-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
		}
		return builder.toString();
	}
	


	
	public static InputStream readImage(String file) throws IOException {
		InputStream input = new FileInputStream(file);
		
			return input;
	}
	

}