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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.borhan.client.BorhanApiException;
import com.borhan.client.enums.BorhanUiConfCreationMode;
import com.borhan.client.services.BorhanUiConfService;
import com.borhan.client.types.BorhanUiConf;
import com.borhan.client.types.BorhanUiConfListResponse;
import com.borhan.client.IBorhanLogger;
import com.borhan.client.BorhanLogger;

public class UiConfServiceTest extends BaseTest {
	private IBorhanLogger logger = BorhanLogger.getLogger(UiConfServiceTest.class);

	// keeps track of test vids we upload so they can be cleaned up at the end
	protected List<Integer> testUiConfIds = new ArrayList<Integer>();
	
	protected BorhanUiConf addUiConf(String name) throws BorhanApiException {

		BorhanUiConfService uiConfService = client.getUiConfService();

		BorhanUiConf uiConf = new BorhanUiConf();
		uiConf.setName(name);
		uiConf.setDescription("Ui conf unit test");
		uiConf.setHeight(373);
		uiConf.setWidth(750);
		uiConf.setCreationMode(BorhanUiConfCreationMode.ADVANCED);
		uiConf.setConfFile("NON_EXISTING_CONF_FILE");
		
		// this uiConf won't be editable in the BMC until it gets a config added to it, I think
		
		BorhanUiConf addedConf = uiConfService.add(uiConf);
				
		this.testUiConfIds.add(addedConf.getId());
		
		return addedConf;
		
	}
	
	public void testAddUiConf() throws Exception {
		if (logger.isEnabled())
			logger.info("Starting ui conf add test");
		
		try {			
			startAdminSession();
			String name = "Test UI Conf (" + new Date() + ")";
			BorhanUiConf addedConf = addUiConf(name);
			assertNotNull(addedConf);
			
		} catch (BorhanApiException e) {
			if (logger.isEnabled())
				logger.error(e);
			fail(e.getMessage());
		}
		
	}
	
	public void testGetUiConf() throws Exception {
		if (logger.isEnabled())
			logger.info("Starting ui get test");
		
		try {			
			startAdminSession();
			String name = "Test UI Conf (" + new Date() + ")";
			BorhanUiConf addedConf = addUiConf(name);
			
			int addedConfId = addedConf.getId();
			BorhanUiConfService confService = this.client.getUiConfService();
			BorhanUiConf retrievedConf = confService.get(addedConfId);
			assertEquals(retrievedConf.getId(), addedConfId);
			
		} catch (BorhanApiException e) {
			if (logger.isEnabled())
				logger.error(e);
			fail(e.getMessage());
		}
		
	}
	
	public void testDeleteUiConf() throws Exception {
		if (logger.isEnabled())
			logger.info("Starting ui conf delete test");
		
		try {			
			startAdminSession();
			String name = "Test UI Conf (" + new Date() + ")";
			BorhanUiConf addedConf = addUiConf(name);
			
			int addedConfId = addedConf.getId();
			
			BorhanUiConfService confService = this.client.getUiConfService();
			
			confService.delete(addedConfId);
			
			try {
				confService.get(addedConfId);
				fail("Getting deleted ui-conf should fail");
			} catch (BorhanApiException bae) {
				// Wanted behavior
			} finally {
				// we whacked this one, so let's not keep track of it		
				this.testUiConfIds.remove(testUiConfIds.size() - 1);
			}
						
		} catch (BorhanApiException e) {
			if (logger.isEnabled())
				logger.error(e);
			fail(e.getMessage());
		}
	}

	public void testListUiConf() throws Exception {
		if (logger.isEnabled())
			logger.info("Starting ui conf list test");
		
		try {
			startAdminSession();
			BorhanUiConfService uiConfService = client.getUiConfService();
			assertNotNull(uiConfService);
			
			BorhanUiConfListResponse listResponse = uiConfService.list();
			assertNotNull(listResponse);
			
			for (BorhanUiConf uiConf : listResponse.getObjects()) {
				if (logger.isEnabled())
					logger.debug("uiConf id:" + uiConf.getId() + " name:" + uiConf.getName());
			}
			
		} catch (BorhanApiException e) {
			if (logger.isEnabled())
				logger.error(e);
			fail(e.getMessage());
		}
	}
	
	@Override
	protected void tearDown() throws Exception {
		
		super.tearDown();
		
		if (!doCleanup) return;
		
		if (logger.isEnabled())
			logger.info("Cleaning up test UI Conf entries after test");
		
		BorhanUiConfService uiConfService = this.client.getUiConfService();
		for (Integer id : this.testUiConfIds) {
			if (logger.isEnabled())
				logger.debug("Deleting UI conf " + id);
			try {
				uiConfService.delete(id);			
			} catch (Exception e) {
				if (logger.isEnabled())
					logger.error("Couldn't delete " + id, e);
			}
		} //next id
	}
}
