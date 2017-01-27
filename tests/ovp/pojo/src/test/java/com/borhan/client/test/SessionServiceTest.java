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

import java.io.IOException;

import com.borhan.client.BorhanApiException;
import com.borhan.client.enums.BorhanSessionType;
import com.borhan.client.types.BorhanMediaListResponse;

public class SessionServiceTest extends BaseTest {

	/**
	 * Test Open / close Session
	 * @throws IOException 
	 */
	public void testSession() throws Exception {

		try {
			
			// test open session
			startUserSession();
			assertNotNull(client.getSessionId());
			
			BorhanMediaListResponse response = client.getMediaService().list();
			assertNotNull(response);
			
			// Close session
			BaseTest.closeSession(client);
			
		} catch (BorhanApiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		// Test close connection
		try {
			client.getMediaService().list();
			fail("Listing entries without KS should fail");
		} catch (BorhanApiException e) {
			// Should fail since the connection is closed.
		}
		
	}
	
	public void testExpiredSession() {
		try {
			String KS = client.generateSession(testConfig.getAdminSecret(),
					"asdasd", BorhanSessionType.USER,
					testConfig.getPartnerId(), 60 * 60 * 24);
			client.setSessionId(KS);

			BorhanMediaListResponse response = client.getMediaService().list();
			assertNotNull(response);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		try {
			String KS = client.generateSession(testConfig.getAdminSecret(),
					"asdasd", BorhanSessionType.USER,
					testConfig.getPartnerId(), -60 * 60 * 24);
			client.setSessionId(KS);

			client.getMediaService().list();
			fail("Listing entries with invalid KS should fail");
		} catch (Exception e) {
			assertTrue(e instanceof BorhanApiException);
			String msg = ((BorhanApiException)e).getMessage();
			assertTrue(msg.contains("EXPIRED"));
		}

	}
}
