JFS-framework
============

Java-flash-socket-based-framework-with-mysql

This framework allows you to quickly create a java-flash application using sockets and mysql database

to query a row from database use this in java:

JSONObject user = Data.db.executeQuery("SELECT * FROM users WHERE id = " + user_id, true);

JSONArray users = Data.db.executeQuery("SELECT * FROM users", false).getJSONArray("rows");

the first parameter is the query
the second parameter is boolean, return a row(true) or return rows(false)

for example we have an flex application:

<?xml version="1.0" encoding="utf-8"?>
<s:Application 
    xmlns:fx="http://ns.adobe.com/mxml/2009"    
    xmlns:mx="library://ns.adobe.com/flex/mx"     
    xmlns:s="library://ns.adobe.com/flex/spark"
	applicationComplete="onAppCompleted();">

    <s:layout> 
        <s:BasicLayout/> 
    </s:layout>

	
	<fx:Script>
        <![CDATA[
			
			public cusSock:CustomSocket;
			public data:Data = new Data();
			private function onAppCompleted():void {
			
				cusSock = new CustomSocket('127.0.0.1', 255, this);
				this.data = cusSock.data;
			}
			
			
        ]]>
    </fx:Script>
	
		<mx:VBox>
			<s:Label text="test" />
		</mx:VBox>
</s:Application>

so after the connection, the connect function is executed in our Handler.as file, after that we can attach handlers that we want to be executed:

		public function connect(params:Object):void {
		
			
			handler = {};
			
			if(params.success)
			{
				data.userId = params.userId;
				
				handler = {};
				handler._class = "Handler";
				handler._method = "getUsers";
				handler._params = {};
				data.addHandler(handler);
				
			}
			else
			{
				handler._class = "Handler"; // here we are telling to execute a class named Handler in java
				handler._method = "connect"; // and here we are telling to execute a specific method from the above class
				handler._params = {userId: object.userId, login: object.userName}; // and here are the paremeters for the function
				data.addHandler(handler); // here we are adding to the handler
				data.sendHandlers(); // and here we send them, this function is executed only one time, after this it is working automatically
			}
		}


		public function getUsers(params:Object):void {
		
			data.playersCount = params.users.length;
			object.notPlayingPlayersCount = params.notPlayingUsers.length;
			handler = {};
			handler._class = "Handler";
			handler._method = "getUsers";
			handler._params = {};
			data.addHandler(handler);
		}
		
		
		
		NOTICE THAT ALL HANDLERS ARE SENDED ALL AT ONE TIME, this helps to avoid lags
		
		
		---------------------------------------------------------------------------------
		
		You can edit the PING of the data in the Client.java where Thread.sleep(100) - is by default
		
		
		ALSO AS you know the flash environment sends a request for a policy file that is included here, you can change it in the
		PolicyMultiThreadedFile.java
		
		The Pinger.java - is a class for doing something at some interval time(checkitout) its simple
		
		in the Starter.java you can set your server IP, notice that the same should be on the Actionscript side, the same is with the ports
		
		The database configuration is in the Config.java
		
		you can test the app by running it in java and flash(actionscript) and after the users connect, the usersCount will increase in each separate application
