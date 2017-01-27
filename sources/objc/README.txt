Package contents
=================
 - The Borhan client library base (BorhanClientBase, BorhanXmlParsers)
 - Auto generated core APIs (BorhanClient)
 - Auto generated plugin APIs (BorhanPlugins/*)
 - The 'ASIHttpRequest' open source library (http://allseeing-i.com/ASIHTTPRequest/)
 - Project files
 - Library test code and data files (BorhanClientTester/*)
 - Reference iPhone / iPad applications

Adding the BorhanClient to an Xcode 4 project
=============================================
1. Open the target project in XCode
2. Locate BorhanClient.xcodeproj in Finder
3. Drag BorhanClient.xcodeproj to the project in XCode
	IMPORTANT: this doesn't work correctly if the BorhanClient project is open in XCode
		Make sure to close all Xcode projects before dragging (specifically, don't drag
		the BorhanClient project from XCode - drag it from Finder)
	BorhanClient.xcodeproj should now appears under your project and be expandable
4. Click on your project and select Build Settings->Search Paths
5. Add the following paths to 'Header Search Paths'
	../BorhanClient/BorhanClient
	../BorhanClient/BorhanClient/ASIHTTPRequest
	../BorhanClient/BorhanClient/BorhanPlugins
	NOTE: these paths apply when your project is saved under the same folder as BorhanClient
		if it's not, adjust the paths appropriately
6. Click on your project and select Build Phases
7. Locate libBorhanClient.a under BorhanClient.xcodeproj->Products
8. Drag libBorhanClient.a to 'Link Binary With Libraries'
9. Add the following libraries to 'Link Binary With Libraries'
	libz.dylib
	libxml2.dylib
	CFNetwork.framework
	MobileCoreServices.framework
	SystemConfiguration.framework

Running the library test code
==============================
1. Open XCode
2. Open the BorhanClientTester project (File->Open)
3. Edit BorhanClientTester.m and fill out your partner id and admin secret (optionally, change the user id)
4. Choose the BorhanClientTester > iPhone simulator scheme
5. Build the project (Product->Build)
6. Hit play
7. Click the Go button in the test app

Note: The library was tested under iPhone Simulator V4.3, built under xCode V4.1


Running the demo application
==============================
1. Open XCode
2. Open the Borhan project under DemoApplication (File->Open)
3. Open Shared/Borhan-Info.plist
4. Type your BMC credentials under the UserEmail / UserPassword fields
5. Choose the Borhan > iPhone simulator scheme
6. Hit play

Running the demo application with DRM support
==============================================

1. Open Xcode 
2. Open the Borhan project under DemoApplication (File->Open)
3. Choose the "Borhan widevine" > Device (iPhone/iPad) scheme
4. Hit play
