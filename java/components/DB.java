package components;
import java.sql.*;
import java.util.Properties;
import org.json.JSONObject;
import org.json.JSONArray;
public class DB {

	Connection conn = null;
	//String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	String driver = "com.mysql.jdbc.Driver";
	//String host = "37.140.192.217";
	//String login = "u9931156_test10";
	//String password = "testtest10";
	String host = "127.0.0.1";
	//String login = "allonbet";
      //  String password = "hft4w378th78482";
        //String login = "sillaru2";
        //String password = "silla321";
    String login = "root";
    String password = "";
	Statement s = null;
	ResultSet rs = null;
	ResultSetMetaData md = null;
	public void connect(String db) 
	{
		try {
		
			Class.forName(driver).newInstance();
			Properties properties= new Properties();
			properties.setProperty("user", login);
			properties.setProperty("password", password);
			properties.setProperty("useUnicode","true");
			properties.setProperty("characterEncoding","UTF-8");
			conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + db, properties);
			s = conn.createStatement();
			PreparedStatement stmt = null;
			stmt = conn.prepareStatement("ALTER DATABASE " + Config.dbName + " CHARACTER SET 'utf8'");
			stmt.executeUpdate();
			stmt = conn.prepareStatement("SET NAMES 'utf8'");
			stmt.executeUpdate();
		
		} catch(Exception e) {
		
			System.out.println("Exception: " + e);
			e.printStackTrace();
		
		}
	}
	
	public void close()
	{
		try {
		rs.close();
		s.close();
		conn.close();
		} catch(Exception e) {
		
			System.out.println("Exception: " + e);
			e.printStackTrace();
		
		}
	}
	
		public synchronized Boolean findUser(String login, String password)
		{
				JSONArray rows = new JSONArray();
				PreparedStatement stmt = null;
				try {
					stmt = conn.prepareStatement("SELECT * FROM sillaru_users WHERE login = ? AND password = ?");
					stmt.setString(1, login);
					stmt.setString(2, password);
					rs = stmt.executeQuery();
					md = rs.getMetaData();
					int colCount = md.getColumnCount();
					int ch = 0;
					while(rs.next())
					{
						ch++;
						JSONObject row = new JSONObject();
						for (int i = 1; i <= colCount; i++)
						{
							String col_name = md.getColumnName(i);
							row.put(col_name, rs.getString(col_name));
						}
						rows.put(row);
					}
				
			
			} catch(Exception e) {
			
				System.out.println("Exception: " + e);
				e.printStackTrace();
			
			}
			if(rows.length() > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	
		public synchronized Boolean exists(String query)
		{
				JSONArray rows = new JSONArray();
				try {
					rs = s.executeQuery(query);
					md = rs.getMetaData();
					int colCount = md.getColumnCount();
					int ch = 0;
					while(rs.next())
					{
						ch++;
						JSONObject row = new JSONObject();
						for (int i = 1; i <= colCount; i++)
						{
							String col_name = md.getColumnName(i);
							row.put(col_name, rs.getString(col_name));
						}
						rows.put(row);
					}
				
			
			} catch(Exception e) {
			
				System.out.println("Exception: " + e);
				e.printStackTrace();
			
			}
			if(rows.length() > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	
	public synchronized JSONObject executeQuery(String query, Boolean onlyOne)
	{
			JSONArray rows = new JSONArray();
			JSONObject oneRow = new JSONObject();
			try {
				rs = s.executeQuery(query);
				md = rs.getMetaData();
				int colCount = md.getColumnCount();
				int ch = 0;
				while(rs.next())
				{
					ch++;
					JSONObject row = new JSONObject();
					for (int i = 1; i <= colCount; i++)
					{
						String col_name = md.getColumnName(i);
						row.put(col_name, rs.getString(col_name));
					}
					if(ch < 2)
					{
						oneRow = row;
					}
					rows.put(row);
				}
			
		
		} catch(Exception e) {
		
			System.out.println("Exception: " + e);
			e.printStackTrace();
		
		}
		JSONObject wrapper = new JSONObject();
		wrapper.put("rows", rows);
		if(onlyOne)
		{
			return oneRow;
		}
		else
		{
			return wrapper;
		}
	}
	
	public synchronized int executeUpdate(String query) {
	
		int res = 0;
		ResultSet generatedKeys;
		try {
		res = s.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
		
			if(res == 1 && s.getGeneratedKeys() != null)
			{
				generatedKeys = s.getGeneratedKeys();
				
				if(generatedKeys.next())
				{
					return generatedKeys.getInt(1);
				}
			}
		} catch(Exception e) {
		
			System.out.println("Exception: " + e);
			e.printStackTrace();
		
		}
		return res;
	}
	
	
}