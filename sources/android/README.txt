
Package contents
=================
 - The Borhan client library base (BorhanClientBase, BorhanObjectBase...)
 - Auto generated core APIs (BorhanClient...)
 - Required JAR files
 - Project files
 - Library test code and data files (BorhanClientTester/*)
 - Reference application (DemoApplication/*)

Running the test code
======================
1. Import the projects into Eclipse - 
	a. right click in the Package Explorer
	b. Import...
	c. Android->Existing Android Code Into Workspace
	d. Select the root dir containing all 3 android projects (BorhanClient, BorhanClientTester and DemoApplication)
	e. Make sure all 3 projects are selected, click ok
	f. Wait until the projects are automatically compiled (initially some errors will appear, 
		until the BorhanClient is compiled, they should go away automatically)
2. Edit BorhanClientTester/src/com.borhan.client.test/BorhanTestConfig and fill out your Borhan account information
3. Right click on BorhanClientTester/src/com.borhan.client.test/BorhanTestSuite
4. Run As->Android JUnit Test


Running the demo application
=============================
1. Import the projects into Eclipse (see above)
2. Edit Borhan/src/com.borhan.activity/Settings.java
3. Search for etEmail.setText and etPassword.setText
4. Set the default user / password to the credentials of you Borhan BMC account
5. Hit the play button
