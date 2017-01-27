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
package com.borhan.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.borhan.client.enums.BorhanEnumAsInt;
import com.borhan.client.enums.BorhanEnumAsString;

/**
 * Helper class that provides a collection of Borhan parameters (key-value
 * pairs).
 * 
 * @author jpotts
 * 
 */
public class BorhanParams extends JSONObject implements Serializable  {

	public String toQueryString() throws BorhanApiException {
		return toQueryString(null);
	}

	public String toQueryString(String prefix) throws BorhanApiException {

		StringBuffer str = new StringBuffer();
		Object value;
		String key;
		for (Object keyObject : keySet()) {
			key = (String) keyObject;
			if (str.length() > 0) {
				str.append("&");
			}

			try {
				value = get(key);
			} catch (JSONException e) {
				throw new BorhanApiException(e.getMessage());
			}

			if (prefix != null) {
				key = prefix + "[" + key + "]";
			}
			if (value instanceof BorhanParams) {
				str.append(((BorhanParams) value).toQueryString(key));
			} else {
				str.append(key);
				str.append("=");
				str.append(value);
			}
		}

		return str.toString();
	}

	public void add(String key, int value) throws BorhanApiException {
		if (value == BorhanParamsValueDefaults.BORHAN_UNDEF_INT) {
			return;
		}

		if (value == BorhanParamsValueDefaults.BORHAN_NULL_INT) {
			putNull(key);
			return;
		}

		try {
			put(key, value);
		} catch (JSONException e) {
			throw new BorhanApiException(e.getMessage());
		}
	}

	public void add(String key, long value) throws BorhanApiException {
		if (value == BorhanParamsValueDefaults.BORHAN_UNDEF_LONG) {
			return;
		}
		if (value == BorhanParamsValueDefaults.BORHAN_NULL_LONG) {
			putNull(key);
			return;
		}

		try {
			put(key, value);
		} catch (JSONException e) {
			throw new BorhanApiException(e.getMessage());
		}
	}

	public void add(String key, double value) throws BorhanApiException {
		if (value == BorhanParamsValueDefaults.BORHAN_UNDEF_DOUBLE) {
			return;
		}
		if (value == BorhanParamsValueDefaults.BORHAN_NULL_DOUBLE) {
			putNull(key);
			return;
		}

		try {
			put(key, value);
		} catch (JSONException e) {
			throw new BorhanApiException(e.getMessage());
		}
	}

	public void add(String key, String value) throws BorhanApiException {
		if (value == null) {
			return;
		}

		if (value.equals(BorhanParamsValueDefaults.BORHAN_NULL_STRING)) {
			putNull(key);
			return;
		}

		try {
			put(key, value);
		} catch (JSONException e) {
			throw new BorhanApiException(e.getMessage());
		}
	}

	public void add(String key, BorhanObjectBase object)
			throws BorhanApiException {
		if (object == null)
			return;

		try {
			put(key, object.toParams());
		} catch (JSONException e) {
			throw new BorhanApiException(e.getMessage());
		}
	}

	public <T extends BorhanObjectBase> void add(String key, ArrayList<T> array)
			throws BorhanApiException {
		if (array == null)
			return;

		if (array.isEmpty()) {
			BorhanParams emptyParams = new BorhanParams();
			try {
				emptyParams.put("-", "");
				put(key, emptyParams);
			} catch (JSONException e) {
				throw new BorhanApiException(e.getMessage());
			}
		} else {
			JSONArray arrayParams = new JSONArray();
			for (BorhanObjectBase baseObj : array) {
				arrayParams.put(baseObj.toParams());
			}
			try {
				put(key, arrayParams);
			} catch (JSONException e) {
				throw new BorhanApiException(e.getMessage());
			}
		}
	}

	public <T extends BorhanObjectBase> void add(String key,
			HashMap<String, T> map) throws BorhanApiException {
		if (map == null)
			return;

		if (map.isEmpty()) {
			BorhanParams emptyParams = new BorhanParams();
			try {
				emptyParams.put("-", "");
				put(key, emptyParams);
			} catch (JSONException e) {
				throw new BorhanApiException(e.getMessage());
			}
		} else {
			BorhanParams mapParams = new BorhanParams();
			for (String itemKey : map.keySet()) {
				BorhanObjectBase baseObj = map.get(itemKey);
				mapParams.add(itemKey, baseObj);
			}
			try {
				put(key, mapParams);
			} catch (JSONException e) {
				throw new BorhanApiException(e.getMessage());
			}
		}
	}

	public <T extends BorhanObjectBase> void add(String key,
			BorhanParams params) throws BorhanApiException {
		try {
			if (params instanceof BorhanParams && has(key)
					&& get(key) instanceof BorhanParams) {
				BorhanParams existingParams = (BorhanParams) get(key);
				existingParams.putAll((BorhanParams) params);
			} else {
				put(key, params);
			}
		} catch (JSONException e) {
			throw new BorhanApiException(e.getMessage());
		}
	}

	public Iterable<String> keySet() {
		return new Iterable<String>() {
			@SuppressWarnings("unchecked")
			public Iterator<String> iterator() {
				return keys();
			}
		};
	}

	private void putAll(BorhanParams params) throws BorhanApiException {
		for (Object key : params.keySet()) {
			String keyString = (String) key;
			try {
				put(keyString, params.get(keyString));
			} catch (JSONException e) {
				throw new BorhanApiException(e.getMessage());
			}
		}
	}

	public void add(BorhanParams objectProperties) throws BorhanApiException {
		putAll(objectProperties);
	}

	protected void putNull(String key) throws BorhanApiException {
		try {
			put(key + "__null", "");
		} catch (JSONException e) {
			throw new BorhanApiException(e.getMessage());
		}
	}

	/**
	 * Pay attention - this function does not check if the value is null.
	 * neither it supports setting value to null.
	 */
	public void add(String key, boolean value) throws BorhanApiException {
		try {
			put(key, value);
		} catch (JSONException e) {
			throw new BorhanApiException(e.getMessage());
		}
	}

	/**
	 * Pay attention - this function does not support setting value to null.
	 */
	public void add(String key, BorhanEnumAsString value)
			throws BorhanApiException {
		if (value == null)
			return;

		add(key, value.getHashCode());
	}

	/**
	 * Pay attention - this function does not support setting value to null.
	 */
	public void add(String key, BorhanEnumAsInt value)
			throws BorhanApiException {
		if (value == null)
			return;

		add(key, value.getHashCode());
	}

	public boolean containsKey(String key) {
		return has(key);
	}

	public void clear() {
		for (Object key : keySet()) {
			remove((String) key);
		}
	}

	public BorhanParams getParams(String key) throws BorhanApiException {
		if (!has(key))
			return null;

		Object value;
		try {
			value = get(key);
		} catch (JSONException e) {
			throw new BorhanApiException(e.getMessage());
		}
		if (value instanceof BorhanParams)
			return (BorhanParams) value;

		throw new BorhanApiException("Key value [" + key
				+ "] is not instance of BorhanParams");
	}

}
