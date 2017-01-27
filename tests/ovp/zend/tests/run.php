<?php

define('CONFIG_FILE', 'config.ini');

require_once(dirname(__FILE__).'/TestsAutoloader.php');
TestsAutoloader::register();

require_once(dirname(__FILE__).'/SampleLoggerImplementation.php');
require_once(dirname(__FILE__).'/Test/ZendClientTester.php');

$testerConfig = parse_ini_file(dirname(__FILE__).'/'.CONFIG_FILE);

// init borhan configuration
$config = new Borhan_Client_Configuration();
$config->serviceUrl = $testerConfig['serviceUrl'];
$config->curlTimeout = 120;
$config->setLogger(new SampleLoggerImplementation());

// init borhan client
$client = new Borhan_Client_Client($config);

// generate session
$ks = $client->generateSession($testerConfig['adminSecret'], $testerConfig['userId'], Borhan_Client_Enum_SessionType::ADMIN, $testerConfig['partnerId']);
$config->getLogger()->log('Borhan session (ks) was generated successfully: ' . $ks);
$client->setKs($ks);

// check connectivity
try
{
	$client->system->ping();
}
catch (Borhan_Client_Exception $ex)
{
	$config->getLogger()->log('Ping failed with api error: '.$ex->getMessage());
	die;
}
catch (Borhan_Client_ClientException $ex)
{
	$config->getLogger()->log('Ping failed with client error: '.$ex->getMessage());
	die;
}

// run the tester
$tester = new ZendClientTester($client, intval($testerConfig['partnerId']));
$tester->run();