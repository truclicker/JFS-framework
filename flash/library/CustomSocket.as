package library {

	import flash.errors.*;
	import flash.events.*;
	import flash.net.Socket;
	import flash.system.Security;
	import flash.utils.getDefinitionByName;
	import flash.net.navigateToURL;
	import flash.net.URLRequest;

	public class CustomSocket extends Socket {
		private var response:String = "";
		private var object:Object;
		private var responder:Responder;
		public var data:Data;
		public function CustomSocket(host:String = null, port:uint = 0, object:Object = null) {
			super();
			this.object = object;
			data = new Data(object);
			configureListeners();
			//Security.loadPolicyFile("xmlsocket://" + host + ":" + port);
			
			if (host && port)  {
				super.connect(host, port);
			}
		}

		private function configureListeners():void {
			addEventListener(Event.CLOSE, closeHandler);
			addEventListener(Event.CONNECT, connectHandler);
			addEventListener(IOErrorEvent.IO_ERROR, ioErrorHandler);
			addEventListener(SecurityErrorEvent.SECURITY_ERROR, securityErrorHandler);
			addEventListener(ProgressEvent.SOCKET_DATA, socketDataHandler);
		}
		
		public function writeln(str:String):void {
			str += "\n";
			try {
				writeUTFBytes(str);
			}
			catch(e:IOError) {
				trace2(e.toString());
			}
		}

		public function sendRequest(str:String = ''):void {
			//trace2("sendRequest");
			response = "";
			//trace2(str);
			writeln(str);
			flush();
		}
		
		private function trace2(str:String):void {
		
			
				object.trace2(str);
			
		
		}

		private function readResponse():void {
			var str:String = readUTFBytes(bytesAvailable);
			response += str;
					//trace2(response);
					try
					{
						var objects:Object = JSON.parse(response);
						response = "";
						for(var i:int = 0; i < objects.handlers.length; i++)
						{
							var handler:Object = objects.handlers[i];
							var ClassReference:Class = getDefinitionByName('library.' + handler._class) as Class;
							var classObject:Object = new ClassReference(object, data);
							classObject[handler._method](JSON.parse(handler._params));
						}
						data.sendHandlers();
					}
					catch(err:Error)
					{
						//trace2("response: " + response);
						//trace2("CustomSocket point error");
						//trace2(err.getStackTrace());
					}
			
		}
		
		private function closeHandler(event:Event):void {
			trace2("closeHandler: " + event);
			trace2(response.toString());
			object.showMessage('Соединение потеряно :(');
			navigateToURL(new URLRequest('http://mlm-loto.com'), '_self');
		}

		private function connectHandler(event:Event):void {
			trace2("connectHandler: " + event);
			var handler:Handler = new Handler(object, data);
			handler.connect({});
			trace2('Соединение прошло успешно');
		}

		private function ioErrorHandler(event:IOErrorEvent):void {
			trace2("ioErrorHandler: " + event);
			navigateToURL(new URLRequest('http://mlm-loto.com'), '_self');
			trace2('Невозможно подсоединится к серверу :(');
		}

		private function securityErrorHandler(event:SecurityErrorEvent):void {
			trace2("securityErrorHandler: " + event);
			navigateToURL(new URLRequest('http://mlm-loto.com'), '_self');
			trace2('Невозможно подсоединится к серверу :(');
		}

		private function socketDataHandler(event:ProgressEvent):void {
			//trace2("socketDataHandler: " + event);
			readResponse();
		}
	}
}