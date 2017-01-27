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
import java.util.ArrayList;

import com.borhan.client.enums.BorhanContainerFormat;
import com.borhan.client.enums.BorhanNullableBoolean;
import com.borhan.client.enums.BorhanSiteRestrictionType;
import com.borhan.client.types.BorhanAccessControl;
import com.borhan.client.types.BorhanBaseRestriction;
import com.borhan.client.types.BorhanConversionProfile;
import com.borhan.client.types.BorhanCountryRestriction;
import com.borhan.client.types.BorhanSiteRestriction;
import com.borhan.client.types.BorhanThumbParams;

public class MediaServiceFieldsTest extends BaseTest {

	/**
	 * Tests that when we set values to their matching "NULL" their value isn't passed to the server.
	 * The parameter types that are tested : 
	 * String, int, EnumAsInt, EnumAsString.
	 * @throws IOException 
	 */
	public void testSetFieldValueShouldNotPass() throws Exception {

		startAdminSession();

		final String testString = "Borhan test string";
		final int testInt = 42;
		final BorhanNullableBoolean testEnumAsInt = BorhanNullableBoolean.FALSE_VALUE;
		final BorhanContainerFormat testEnumAsString = BorhanContainerFormat.ISMV;

		BorhanThumbParams params = new BorhanThumbParams();
		params.name = testString;
		params.description = testString;
		params.density = testInt;
		params.isSystemDefault = testEnumAsInt;
		params.format = testEnumAsString;

		// Regular update works
		params = client.getThumbParamsService().add(params);

		assertEquals(testString, params.description);
		assertEquals(testInt, params.density);
		assertEquals(testEnumAsInt, params.isSystemDefault);
		assertEquals(testEnumAsString, params.format);

		// Null value not passed
		BorhanThumbParams params2 = new BorhanThumbParams();
		params2.description = null;
		params2.density = Integer.MIN_VALUE;
		params2.isSystemDefault = null;
		params2.format = null;

		params2 = client.getThumbParamsService().update(params.id, params2);
		assertEquals(testString, params2.description);
		assertEquals(testInt, params2.density);
		assertEquals(testEnumAsInt, params2.isSystemDefault);
		assertEquals(testEnumAsString, params2.format);

		client.getThumbParamsService().delete(params.id);
	}

	
	/**
	 * Tests that when we ask to set parameters to null, we indeed set them to null
	 * The parameter types that are tested : String
	 * @throws IOException 
	 */
	public void testSetFieldsToNullString() throws Exception {

		startAdminSession();

		final String testString = "Borhan test string";

		BorhanThumbParams params = new BorhanThumbParams();
		params.name = testString;
		params.description = testString;

		// Regular update works
		params = client.getThumbParamsService().add(params);

		assertEquals(testString, params.description);

		// Set to null
		BorhanThumbParams params2 = new BorhanThumbParams();
		params2.description = "__null_string__";

		params2 = client.getThumbParamsService().update(params.id, params2);
		assertNull(params2.description);

		client.getThumbParamsService().delete(params.id);
		
	}
	
	/**
	 * Tests that when we ask to set parameters to null, we indeed set them to null
	 * The parameter types that are tested : int
	 * @throws IOException 
	 */
	public void testSetFieldsToNullInt() throws Exception {

		startAdminSession();
		final int testInt = 42;

		BorhanConversionProfile profile = new BorhanConversionProfile();
		profile.name = "Borhan test string";
		profile.flavorParamsIds = "0";
		profile.storageProfileId = testInt;

		// Regular update works
		profile = client.getConversionProfileService().add(profile);

		assertEquals(testInt, profile.storageProfileId);

		// Set to null
		BorhanConversionProfile profile2 = new BorhanConversionProfile();
		profile2.storageProfileId = Integer.MAX_VALUE;

		profile2 = client.getConversionProfileService().update(profile.id, profile2);
		assertEquals(Integer.MIN_VALUE, profile2.storageProfileId);

		client.getConversionProfileService().delete(profile.id);
		
		
	}
	
	/**
	 * Tests that array update is working - 
	 * tests empty array, Null array & full array.
	 */
	public void testArrayConversion() throws Exception {
		
		BorhanSiteRestriction resA = new BorhanSiteRestriction();
		resA.siteRestrictionType = BorhanSiteRestrictionType.RESTRICT_SITE_LIST;
		resA.siteList = "ResA";
		BorhanCountryRestriction resB = new BorhanCountryRestriction();
		resB.countryList = "IllegalCountry";
		
		ArrayList<BorhanBaseRestriction> restrictions = new ArrayList<BorhanBaseRestriction>();
		restrictions.add(resA);
		restrictions.add(resB);
		
		BorhanAccessControl accessControl = new BorhanAccessControl();
		accessControl.name = "test access control";
		accessControl.restrictions = restrictions;
		
		startAdminSession();
		accessControl = client.getAccessControlService().add(accessControl);
		
		assertNotNull(accessControl.restrictions);
		assertEquals(2, accessControl.restrictions.size());
		
		// Test null update - shouldn't update
		BorhanAccessControl accessControl2 = new BorhanAccessControl();
		accessControl2.name = "updated access control";
		accessControl2.restrictions = null; 
		accessControl2 = client.getAccessControlService().update(accessControl.id, accessControl2);
		
		assertEquals(2, accessControl2.restrictions.size());
		
		// Test update Empty array - should update
		BorhanAccessControl accessControl3 = new BorhanAccessControl();
		accessControl3.name = "reset access control";
		accessControl3.restrictions = new ArrayList<BorhanBaseRestriction>(); 
		accessControl3 = client.getAccessControlService().update(accessControl.id, accessControl3);
		
		assertEquals(0, accessControl3.restrictions.size());

		// Delete entry
		client.getAccessControlService().delete(accessControl.id);
	}

}
