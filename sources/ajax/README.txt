Borhan JavaScript API Client Library.

The library contain the following files:
 - example.html
 - jquery-3.1.0.min.js
 - BorhanClient.js - all client functionality without the services.
 - BorhanClient.min.js - BorhanClient.js minified.
 - BorhanFullClient.js - all client functionality including all services.
 - BorhanFullClient.min.js - BorhanFullClient.js minified.
 - Services files, e.g. BorhanAccessControlProfileService.js.
 - Minified services files, e.g. BorhanAccessControlProfileService.min.js.

If you're lazy developer and don't want to include each used service separately, 
or if you find yourself including many services as run time,
you might want to use the single BorhanFullClient.min.js that already contains all services.

If your application is using merely few services, it would be more efficient to include only BorhanClient.min.js
and the minified services files that you need.

