# Borhan Client Generator
The code in this repo is used to auto generate the Borhan client libraries for each supported language.

[![License](https://img.shields.io/badge/license-AGPLv3-blue.svg)](http://www.gnu.org/licenses/agpl-3.0.html)

## Deployment Instructions
The list of supported clients is [here] (https://github.com/borhan/clients-generator/blob/Kajam-11.18.0/config/generator.all.ini)

Download the API scheme XML from http://www.borhan.com/api_v3/api_schema.php.

To generate one client run:
```
$ php /opt/borhan/clients-generator/exec.php -x/path-to-xml/BorhanClient.xml $CLIENT_NAME
```

For example, to generate php53 run:
```
php /opt/borhan/clients-generator/exec.php -x/path-to-xml/BorhanClient.xml php53
```

To generate all available clients, run:
```
while read CLIENT;do php /opt/borhan/clients-generator/exec.php -x/path-to-xml/BorhanClient.xml $CLIENT;done < /opt/borhan/clients-generator/config/generator.all.ini
```

## Getting started with the API
To learn how to use the Borhan API, go to [developer.borhan.com](https://developer.borhan.com/)

## How you can help (guidelines for contributors) 
Thank you for helping Borhan grow! If you'd like to contribute please follow these steps:
* Use the repository issues tracker to report bugs or feature requests
* Read [Contributing Code to the Borhan Platform](https://github.com/borhan/platform-install-packages/blob/master/doc/Contributing-to-the-Borhan-Platform.md)
* Sign the [Borhan Contributor License Agreement](https://agentcontribs.borhan.org/)

## Where to get help
* Join the [Borhan Community Forums](https://forum.borhan.org/) to ask questions or start discussions
* Read the [Code of conduct](https://forum.borhan.org/faq) and be patient and respectful

## Get in touch
You can learn more about Borhan and start a free trial at: http://corp.borhan.com    
Contact us via Twitter [@Borhan](https://twitter.com/Borhan) or email: community@borhan.com  
We'd love to hear from you!

## License and Copyright Information
All code in this project is released under the [AGPLv3 license](http://www.gnu.org/licenses/agpl-3.0.html) unless a different license for a particular library is specified in the applicable library path.   

Copyright © Borhan Inc. All rights reserved.   
Authors and contributors: See [GitHub contributors list](https://github.com/borhan/clients-generator/graphs/contributors).  
