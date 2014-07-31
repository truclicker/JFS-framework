package library {
 
	public class UtilFunctions
	{

	 
		public static function randomRange(max:Number, min:Number = 0):Number
		{
			return Math.floor(Math.random() * (max - min) + min);
		}
		
		public static function msFormat(n:uint,delimiter:String=":"):String
		{
			var h:uint = Math.floor(n / 3600000) % 24;
			var m:uint = Math.floor(n / 60000) % 60;
			var s:uint = Math.floor(n / 1000) % 60;
			var hs:String = h.toString();
			var ms:String = m.toString();
			var ss:String = s.toString();
			if (h < 10)
			{
				hs = "0" + hs;
			}
			if (m < 10)
			{
				ms = "0" + ms;
			}
			if (s < 10)
			{
				ss = "0" + ss;
			}
			return hs + delimiter + ms + delimiter + ss;
		}
	}
}