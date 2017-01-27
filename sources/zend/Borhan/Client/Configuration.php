<?php
// ===================================================================================================
//                           _  __     _ _
//                          | |/ /__ _| | |_ _  _ _ _ __ _
//                          | ' </ _` | |  _| || | '_/ _` |
//                          |_|\_\__,_|_|\__|\_,_|_| \__,_|
//
// This file is part of the Borhan Collaborative Media Suite which allows users
// to do with audio, video, and animation what Wiki platfroms allow them to do with
// text.
//
// Copyright (C) 2006-2011  Borhan Inc.
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
// @ignore
// ===================================================================================================
/**
 * @package Borhan
 * @subpackage Client
 */
class Borhan_Client_Configuration
{
	private $logger;

	public $serviceUrl    				= "http://www.borhan.com/";
	public $format        				= Borhan_Client_ClientBase::BORHAN_SERVICE_FORMAT_XML;
	public $curlTimeout   				= 120;
	public $startZendDebuggerSession 	= false;
	public $userAgent					= '';
	public $proxyHost                   = null;
	public $proxyPort                   = null;
	public $proxyType                   = 'HTTP';
	public $proxyUser                   = null;
	public $proxyPassword               = '';
	public $verifySSL 					= true;
	public $sslCertificatePath			= null;
	public $requestHeaders				= array();
	
	/**
	 * Set logger to get borhan client debug logs
	 *
	 * @param Borhan_Client_ILogger $log
	 */
	public function setLogger(Borhan_Client_ILogger $log)
	{
		$this->logger = $log;
	}

	/**
	 * Gets the logger (Internal client use)
	 *
	 * @return Borhan_Client_ILogger
	 */
	public function getLogger()
	{
		return $this->logger;
	}
}
