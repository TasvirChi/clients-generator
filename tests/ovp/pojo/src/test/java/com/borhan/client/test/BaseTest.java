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


import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import junit.framework.TestCase;

import com.borhan.client.BorhanApiException;
import com.borhan.client.BorhanClient;
import com.borhan.client.BorhanConfiguration;
import com.borhan.client.enums.BorhanEntryStatus;
import com.borhan.client.enums.BorhanMediaType;
import com.borhan.client.enums.BorhanSessionType;
import com.borhan.client.services.BorhanMediaService;
import com.borhan.client.types.BorhanMediaEntry;
import com.borhan.client.types.BorhanUploadToken;
import com.borhan.client.types.BorhanUploadedFileTokenResource;
import com.borhan.client.IBorhanLogger;
import com.borhan.client.BorhanLogger;

public class BaseTest extends TestCase {
	protected BorhanTestConfig testConfig;
	
	protected BorhanConfiguration borhanConfig = new BorhanConfiguration();
	protected BorhanClient client;
	
	// keeps track of test vids we upload so they can be cleaned up at the end
	protected List<String> testIds = new ArrayList<String>();

	protected boolean doCleanup = true;

	private static IBorhanLogger logger = BorhanLogger.getLogger(BaseTest.class);

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		testConfig = new BorhanTestConfig();
		
		// Create client
		this.borhanConfig.setEndpoint(testConfig.getServiceUrl());
		this.client = new BorhanClient(this.borhanConfig);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
		if (!doCleanup) return;
		
		if (logger.isEnabled())
			logger.info("Cleaning up test entries after test");
		
		BorhanMediaService mediaService = this.client.getMediaService();
		for (String id : this.testIds) {
			if (logger.isEnabled())
				logger.info("Deleting " + id);
			try {
				getProcessedEntry(client, id);
				mediaService.delete(id);			
			} catch (Exception e) {
				if (logger.isEnabled())
					logger.error("Couldn't delete " + id, e);
				fail(e.getMessage());
			}
		} //next id
	}
	
	
	public void startUserSession() throws Exception{
		startSession(BorhanSessionType.USER);
	}
	
	public void startAdminSession() throws Exception{
		startSession(BorhanSessionType.ADMIN);
	}
	
	protected void startSession(BorhanSessionType type) throws Exception {
		
		String sessionId = client.generateSessionV2(testConfig.getAdminSecret(), testConfig.getUserId(), type, testConfig.getPartnerId(), 86400, "");
		if (logger.isEnabled()){
			logger.debug("Session id:" + sessionId);
		}
		
		client.setSessionId(sessionId);
	}
	
	public static void closeSession(BorhanClient client) throws BorhanApiException {
		client.getSessionService().end();
	}
	
	// Entry utils
	
	public BorhanMediaEntry addTestImage(BaseTest container, String name) throws BorhanApiException, IOException, FileNotFoundException
	{
		BorhanMediaEntry entry = new BorhanMediaEntry();
		entry.setName(name);
		entry.setMediaType(BorhanMediaType.IMAGE);
		entry.setReferenceId(getUniqueString());
		
		InputStream fileData = TestUtils.getTestImage();
		int fileSize = fileData.available();
		entry = client.getMediaService().add(entry);
		
		// Upload token
		BorhanUploadToken uploadToken = new BorhanUploadToken();
		uploadToken.setFileName(testConfig.getUploadImage());
		uploadToken.setFileSize(fileSize);
		BorhanUploadToken token = client.getUploadTokenService().add(uploadToken);
		assertNotNull(token);
		
		// Define content
		BorhanUploadedFileTokenResource resource = new BorhanUploadedFileTokenResource();
		resource.setToken(token.getId());
		entry = client.getMediaService().addContent(entry.getId(), resource);
		assertNotNull(entry);
		
		// upload
		uploadToken = client.getUploadTokenService().upload(token.getId(), fileData, testConfig.getUploadImage(), fileSize, false);
		if(container != null)
			container.testIds.add(entry.getId());
		return client.getMediaService().get(entry.getId());
	}
	
	protected String getUniqueString() {
		return UUID.randomUUID().toString();
	}
	public static BorhanMediaEntry getProcessedEntry(BorhanClient client, String id) throws Exception {
		return getProcessedEntry(client, id, false);
	}
	
	public static BorhanMediaEntry getProcessedEntry(BorhanClient client, String id,
			Boolean checkReady) throws BorhanApiException {
		int maxTries = 50;
		int sleepInterval = 30 * 1000;
		int counter = 0;
		BorhanMediaEntry retrievedEntry = null;
		BorhanMediaService mediaService = client.getMediaService();
		retrievedEntry = mediaService.get(id);
		while (checkReady && retrievedEntry.getStatus() != BorhanEntryStatus.READY) {

			counter++;

			if (counter >= maxTries) {
				throw new RuntimeException("Max retries (" + maxTries
						+ ") when retrieving entry:" + id);
			} else {
				if (logger.isEnabled())
					logger.info("On try: " + counter + ", clip not ready. waiting "
						+ (sleepInterval / 1000) + " seconds...");
				try {
					Thread.sleep(sleepInterval);
				} catch (InterruptedException ie) {
					throw new RuntimeException("Failed while waiting for entry");
				}
			}

			retrievedEntry = mediaService.get(id);
		}

		return retrievedEntry;
	}
}