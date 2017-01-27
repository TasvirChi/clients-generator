<?php
/* set this path to the location of Zend/Loader/StandardAutoloader.php, 
 * the client library can be used with any other php5.3 namespace style autoloaders (for example symfony2 & doctrine2)
*/
define('CONFIG_FILE', 'config.ini');

use Borhan\Client\Configuration as BorhanConfiguration;
use Borhan\Client\Client as BorhanClient;
use Borhan\Client\Enum\SessionType as BorhanSessionType;
use Borhan\Client\ApiException;
use Borhan\Client\ClientException;

// load zend framework 2
require_once(dirname(__FILE__).'/ClassLoader/ClassLoader.php');
$loader = new Symfony\Component\ClassLoader\ClassLoader();
// register Borhan namespace
$loader->addPrefix('Borhan', dirname(__FILE__).'/../library');
$loader->addPrefix('Test', dirname(__FILE__));
$loader->register();

$testerConfig = parse_ini_file(dirname(__FILE__).'/'.CONFIG_FILE);

// init borhan configuration
$config = new BorhanConfiguration();
$config->setServiceUrl($testerConfig['serviceUrl']);
$config->setCurlTimeout(120);
$config->setLogger(new \Test\SampleLoggerImplementation());

// init borhan client
$client = new BorhanClient($config);

// generate session
$ks = $client->generateSession($testerConfig['adminSecret'], $testerConfig['userId'], BorhanSessionType::ADMIN, $testerConfig['partnerId']);
$config->getLogger()->log('Borhan session (ks) was generated successfully: ' . $ks);
$client->setKs($ks);

// check connectivity
try
{
	$client->getSystemService()->ping();
}
catch (ApiException $ex)
{
	$config->getLogger()->log('Ping failed with api error: '.$ex->getMessage());
	die;
}
catch (ClientException $ex)
{
	$config->getLogger()->log('Ping failed with client error: '.$ex->getMessage());
	die;
}

// run the tester
$tester = new \Test\Zend2ClientTester($client, intval($testerConfig['partnerId']));
$tester->run();