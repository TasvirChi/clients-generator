The test API sample shows a simple client setup and session creation call.
For good practice, it is always best to keep the secret key hidden in the server and recieve the borhan session (aka KS) via flashvars or a different external method.

To setup the sample to compile, copy the com folder to the root of the sample code, open the BorhanClientSample.fla in Flash IDE (CS4 and above) and compile.
You should see an error saying you need to define the partner id and secret api key. 
Open the BorhanClientSample.as file and edit the following lines, adding your Borhan partner information:
		private const API_SECRET:String = "YOUR_USER_SECRET";
		private const BORHAN_PARTNER_ID:int = 54321;
Compile again. You should get a message in the trace window indicating the session create call was successful and the actual KS returned from the Borhan server.

If you are using a Borhan self hosted server, open the BorhanClientSample.as file and uncomment and modify the following line (change the url to your Borhan server domain):
//configuration.domain = "http://www.myborhandomain.com";