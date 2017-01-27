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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.borhan.client.BorhanApiException;
import com.borhan.client.BorhanMultiResponse;
import com.borhan.client.enums.BorhanMediaType;
import com.borhan.client.types.BorhanBaseEntry;
import com.borhan.client.types.BorhanMediaEntry;
import com.borhan.client.types.BorhanMediaEntryFilterForPlaylist;
import com.borhan.client.types.BorhanUploadToken;
import com.borhan.client.types.BorhanUploadedFileTokenResource;
import com.borhan.client.utils.ParseUtils;


public class MultiRequestTest extends BaseTest{

	@SuppressWarnings("unchecked")
	public void testMultiRequest() throws Exception {
		
		startAdminSession();
		client.startMultiRequest();
		
		// 1. Ping (Bool : void)
		client.getSystemService().ping();
		
		// 2. Create Entry (Object : Object)
		BorhanMediaEntry entry = new BorhanMediaEntry();
		entry.setName("test (" + new Date() + ")");
		entry.setMediaType(BorhanMediaType.IMAGE);
		entry.setReferenceId(getUniqueString());
		InputStream fileData = TestUtils.getTestImage();
		entry = client.getMediaService().add(entry);
		assertNull(entry);
		
		// 3. Upload token (Object : Object)
		BorhanUploadToken uploadToken = new BorhanUploadToken();
		uploadToken.setFileName(testConfig.getUploadImage());
		uploadToken.setFileSize(fileData.available());
		BorhanUploadToken token = client.getUploadTokenService().add(uploadToken);
		assertNull(token);
		
		// 4. Add Content (Object : String, Object)
		BorhanUploadedFileTokenResource resource = new BorhanUploadedFileTokenResource();
		resource.setToken("{3:result:id}");
		entry = client.getMediaService().addContent("{2:result:id}", resource);
		assertNull(entry);
		
		// 5. upload (Object : String, file, boolean)
		uploadToken = client.getUploadTokenService().upload("{3:result:id}", fileData, testConfig.getUploadImage(), fileData.available(), false);
		
		BorhanMultiResponse multi = client.doMultiRequest();
		// 0
		assertNotNull(multi.get(0));
		assertTrue(ParseUtils.parseBool((String)multi.get(0)));
		// 1
		BorhanMediaEntry mEntry = (BorhanMediaEntry) multi.get(1);
		assertNotNull(mEntry);
		assertNotNull(mEntry.getId());
		// 2
		BorhanUploadToken mToken =(BorhanUploadToken) multi.get(2);
		assertNotNull(mToken);
		assertNotNull(mToken.getId());
		// 3
		assertTrue(multi.get(3) instanceof BorhanMediaEntry);
		// 4
		assertTrue(multi.get(4) instanceof BorhanUploadToken);
		
		// Multi request part II:
		client.startMultiRequest();
		
		// execute from filters (Array: Array, int)
		BorhanMediaEntryFilterForPlaylist filter = new BorhanMediaEntryFilterForPlaylist();
		filter.setReferenceIdEqual(mEntry.getReferenceId());
		ArrayList<BorhanMediaEntryFilterForPlaylist> filters = new ArrayList<BorhanMediaEntryFilterForPlaylist>();
		filters.add(filter);
		List<BorhanBaseEntry> res = client.getPlaylistService().executeFromFilters(filters, 5);
		assertNull(res);

		multi = client.doMultiRequest();
		List<BorhanBaseEntry> mRes = (List<BorhanBaseEntry>)multi.get(0);
		assertNotNull(mRes);
		assertEquals(1, mRes.size());
		
		client.getMediaService().delete(mEntry.getId());
	}
	
	
	/**
	 * This function tests that in a case of error in a multi request, the error is parsed correctly
	 * and it doesn't affect the rest of the multi-request.
	 * @throws BorhanApiException
	 */
	public void testMultiRequestWithError() throws Exception {
		
		startAdminSession();
		client.startMultiRequest();
		
		client.getSystemService().ping();
		client.getMediaService().get("Illegal String");
		client.getSystemService().ping();
		
		BorhanMultiResponse multi = client.doMultiRequest();
		assertNotNull(multi.get(0));
		assertTrue(ParseUtils.parseBool((String)multi.get(0)));
		assertTrue(multi.get(1) instanceof BorhanApiException);
		assertNotNull(multi.get(2));
		assertTrue(ParseUtils.parseBool((String)multi.get(2)));
		
	}
}
