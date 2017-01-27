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
package com.borhan.client.test;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.w3c.dom.Element;

import com.borhan.client.BorhanApiException;
import com.borhan.client.BorhanClient;
import com.borhan.client.BorhanConfiguration;
import com.borhan.client.BorhanObjectFactory;
import com.borhan.client.BorhanParams;
import com.borhan.client.types.BorhanMediaEntry;
import com.borhan.client.types.BorhanMediaListResponse;



public class ErrorTest extends BaseTest {

	public void testInvalidServiceId() {
		this.borhanConfig.setEndpoint("http://2.2.2.2");
		this.borhanConfig.setTimeout(2000);
		
		try {
			this.client = new BorhanClient(this.borhanConfig);
			client.getSystemService().ping();
			fail("Ping to invalid end-point should fail");
		} catch (Exception e) {
			// Expected behavior
		}
	}
	
	public void testInvalidServerDnsName() {
		this.borhanConfig.setEndpoint("http://www.nonexistingborhan.com");
		this.borhanConfig.setTimeout(2000);
		
		try {
			this.client = new BorhanClient(this.borhanConfig);
			client.getSystemService().ping();
			fail("Ping to invalid end-point should fail");
		} catch (Exception e) {
			// Expected behavior
		}
	}
	
	@SuppressWarnings("serial")
	private class BorhanClientMock extends BorhanClient {
		
		String resultToReturn;

		public BorhanClientMock(BorhanConfiguration config, String res) {
			super(config);
			resultToReturn = res;
		}
		
		@Override
		protected String executeMethod(HttpClient client, PostMethod method) {
			return resultToReturn;
		}

		@Override
		protected HttpClient createHttpClient() {
			return null;
		}
		
		@Override
		protected void closeHttpClient(HttpClient client) {
			return;
		}
	}

	/**
	 * Tests case in which XML format is completely ruined
	 */
	public void testXmlParsingError() throws BorhanApiException {
		BorhanClientMock mockClient = new BorhanClientMock(this.borhanConfig, "<xml>");
		mockClient.queueServiceCall("system", "ping", new BorhanParams());
		try {
			mockClient.doQueue();
			fail("Invalid XML response should fail");
		} catch (BorhanApiException e) {
			assertEquals("Failed while parsing response.", e.getMessage());
		}
	}
	
	/**
	 * Tests case in which the response has xml format, but no object type as expected
	 */
	public void testTagInSimpleType() throws BorhanApiException {
		BorhanClientMock mockClient = new BorhanClientMock(this.borhanConfig, "<xml><result><sometag></sometag></result></xml>");
		mockClient.queueServiceCall("system", "ping", new BorhanParams());
		Element resultXmlElement = mockClient.doQueue();
		try {
			BorhanObjectFactory.create(resultXmlElement, null);
			fail("Invalid XML response should fail");
		} catch (Exception e) {
			// Expected behavior
		}
	}
	
	/**
	 * Tests case in which the response has xml format, but no object
	 */
	public void testEmptyObjectOrException() throws BorhanApiException {
		BorhanClientMock mockClient = new BorhanClientMock(this.borhanConfig, "<xml><result></result></xml>");
		mockClient.queueServiceCall("system", "ping", new BorhanParams());
		Element resultXmlElement = mockClient.doQueue();
		try {
			BorhanObjectFactory.create(resultXmlElement, null);
			fail("Invalid XML response should fail");
		} catch (Exception e) {
			// Expected behavior
		}
	}
	
	public void testTagInObjectDoesntStartWithType() throws BorhanApiException {
		BorhanClientMock mockClient = new BorhanClientMock(this.borhanConfig, "<xml><result><id>1234</id></result></xml>");
		mockClient.queueServiceCall("system", "ping", new BorhanParams());
		Element resultXmlElement = mockClient.doQueue();
		try {
			BorhanObjectFactory.create(resultXmlElement, null);
			fail("Invalid XML response should fail");
		} catch (Exception e) {
			// Expected behavior
		}
	}
	
	public void testCharsInsteadOfObject() throws BorhanApiException {
		BorhanClientMock mockClient = new BorhanClientMock(this.borhanConfig, "<xml><result>1234</result></xml>");
		mockClient.queueServiceCall("system", "ping", new BorhanParams());
		Element resultXmlElement = mockClient.doQueue();
		try {
			BorhanObjectFactory.create(resultXmlElement, null);
			fail("Invalid XML response should fail");
		} catch (Exception e) {
			// Expected behavior
		}
	}
	
	public void testUnknownObjectType() throws BorhanApiException {
		BorhanClientMock mockClient = new BorhanClientMock(this.borhanConfig, "<xml><result><objectType>UnknownObjectType</objectType></result></xml>");
		mockClient.queueServiceCall("system", "ping", new BorhanParams());
		Element resultXmlElement = mockClient.doQueue();
		try {
			BorhanObjectFactory.create(resultXmlElement, null);
			fail("Invalid XML response should fail");
		} catch (Exception e) {
			assertEquals("Invalid object type", e.getMessage());
		}
	}
	
	public void testNonBorhanObjectType() throws BorhanApiException {
		BorhanClientMock mockClient = new BorhanClientMock(this.borhanConfig, "<xml><result><objectType>NSString</objectType></result></xml>");
		mockClient.queueServiceCall("system", "ping", new BorhanParams());
		Element resultXmlElement = mockClient.doQueue();
		try {
			BorhanObjectFactory.create(resultXmlElement, null);
			fail("Invalid XML response should fail");
		} catch (Exception e) {
			assertEquals("Invalid object type", e.getMessage());
		}
	}
	
	public void testArrayOfUknownEntry() throws BorhanApiException {
		String testXml = "<xml><result><objectType>BorhanMediaListResponse</objectType><objects>" +
				"<item><objectType>NonExistingclass</objectType><id>test1</id><name>test1</name></item>" +
				"<item><objectType>NonExistingclass</objectType><id>test2</id><name>test2</name></item>" +
				"</objects><totalCount>2</totalCount></result></xml>";
		
		BorhanClientMock mockClient = new BorhanClientMock(this.borhanConfig, testXml);
		mockClient.queueServiceCall("system", "ping", new BorhanParams()); // Just since we need something in the queue
		Element resultXmlElement = mockClient.doQueue();
		try {
			BorhanMediaListResponse res = (BorhanMediaListResponse) BorhanObjectFactory.create(resultXmlElement, null);
			assertEquals(2, res.getTotalCount());
			BorhanMediaEntry entry1 = res.getObjects().get(0);
			BorhanMediaEntry entry2 = res.getObjects().get(1);
			assertTrue(entry1.getId().equals("test1"));
			assertTrue(entry1.getName().equals("test1"));
			assertTrue(entry2.getId().equals("test2"));
			assertTrue(entry2.getName().equals("test2"));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
