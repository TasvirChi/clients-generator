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
	 */
	public void testSetFieldValueShouldNotPass() throws Exception {

		startAdminSession();

		final String testString = "Borhan test string";
		final int testInt = 42;
		final BorhanNullableBoolean testEnumAsInt = BorhanNullableBoolean.FALSE_VALUE;
		final BorhanContainerFormat testEnumAsString = BorhanContainerFormat.ISMV;

		BorhanThumbParams params = new BorhanThumbParams();
		params.setName(testString);
		params.setDescription(testString);
		params.setDensity(testInt);
		params.setIsSystemDefault(testEnumAsInt);
		params.setFormat(testEnumAsString);

		// Regular update works
		params = client.getThumbParamsService().add(params);

		assertEquals(testString, params.getDescription());
		assertEquals(testInt, params.getDensity());
		assertEquals(testEnumAsInt, params.getIsSystemDefault());
		assertEquals(testEnumAsString, params.getFormat());

		// Null value not passed
		BorhanThumbParams params2 = new BorhanThumbParams();
		params2.setDescription(null);
		params2.setDensity(Integer.MIN_VALUE);
		params2.setIsSystemDefault(null);
		params2.setFormat(null);

		params2 = client.getThumbParamsService().update(params.getId(), params2);
		assertEquals(testString, params2.getDescription());
		assertEquals(testInt, params2.getDensity());
		assertEquals(testEnumAsInt, params2.getIsSystemDefault());
		assertEquals(testEnumAsString, params2.getFormat());

		client.getThumbParamsService().delete(params.getId());
	}

	
	/**
	 * Tests that when we ask to set parameters to null, we indeed set them to null
	 * The parameter types that are tested : String
	 */
	public void testSetFieldsToNullString() throws Exception {

		startAdminSession();

		final String testString = "Borhan test string";

		BorhanThumbParams params = new BorhanThumbParams();
		params.setName(testString);
		params.setDescription(testString);

		// Regular update works
		params = client.getThumbParamsService().add(params);

		assertEquals(testString, params.getDescription());

		// Set to null
		BorhanThumbParams params2 = new BorhanThumbParams();
		params2.setDescription("__null_string__");

		params2 = client.getThumbParamsService().update(params.getId(), params2);
		assertNull(params2.getDescription());

		client.getThumbParamsService().delete(params.getId());
		
	}
	
	/**
	 * Tests that when we ask to set parameters to null, we indeed set them to null
	 * The parameter types that are tested : int
	 */
	public void testSetFieldsToNullInt() throws Exception {

		startAdminSession();
		final int testInt = 42;

		BorhanConversionProfile profile = new BorhanConversionProfile();
		profile.setName("Borhan test string");
		profile.setFlavorParamsIds("0");
		profile.setStorageProfileId(testInt);

		// Regular update works
		profile = client.getConversionProfileService().add(profile);

		assertEquals(testInt, profile.getStorageProfileId());

		// Set to null
		BorhanConversionProfile profile2 = new BorhanConversionProfile();
		profile2.setStorageProfileId(Integer.MAX_VALUE);

		profile2 = client.getConversionProfileService().update(profile.getId(), profile2);
		assertEquals(Integer.MIN_VALUE, profile2.getStorageProfileId());

		client.getConversionProfileService().delete(profile.getId());
		
		
	}
	
	/**
	 * Tests that array update is working - 
	 * tests empty array, Null array & full array.
	 */
	public void testArrayConversion() throws Exception {
		
		BorhanSiteRestriction resA = new BorhanSiteRestriction();
		resA.setSiteRestrictionType(BorhanSiteRestrictionType.RESTRICT_SITE_LIST);
		resA.setSiteList("ResA");
		BorhanCountryRestriction resB = new BorhanCountryRestriction();
		resB.setCountryList("IllegalCountry");
		
		ArrayList<BorhanBaseRestriction> restrictions = new ArrayList<BorhanBaseRestriction>();
		restrictions.add(resA);
		restrictions.add(resB);
		
		BorhanAccessControl accessControl = new BorhanAccessControl();
		accessControl.setName("test access control");
		accessControl.setRestrictions(restrictions);
		
		startAdminSession();
		accessControl = client.getAccessControlService().add(accessControl);
		
		assertNotNull(accessControl.getRestrictions());
		assertEquals(2, accessControl.getRestrictions().size());
		
		// Test null update - shouldn't update
		BorhanAccessControl accessControl2 = new BorhanAccessControl();
		accessControl2.setRestrictions(null); 
		accessControl2 = client.getAccessControlService().update(accessControl.getId(), accessControl2);
		
		assertEquals(2, accessControl2.getRestrictions().size());
		
		// Test update Empty array - should update
		BorhanAccessControl accessControl3 = new BorhanAccessControl();
		accessControl3.setRestrictions(new ArrayList<BorhanBaseRestriction>()); 
		accessControl3 = client.getAccessControlService().update(accessControl.getId(), accessControl3);
		
		assertEquals(0, accessControl3.getRestrictions().size());

		// Delete entry
		client.getAccessControlService().delete(accessControl.getId());
	}

}
