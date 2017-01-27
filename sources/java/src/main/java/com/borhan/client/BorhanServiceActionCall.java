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

/**
 * A BorhanServiceActionCall is what the client queues to represent a request to the Borhan server.
 * 
 * @author jpotts
 *
 */
public class BorhanServiceActionCall {
	private String service;
    private String action;
    private BorhanParams params;
    private BorhanFiles files;
    
    public BorhanServiceActionCall(String service, String action, BorhanParams kparams) {
        this(service, action, kparams, new BorhanFiles());
    }

    public BorhanServiceActionCall(String service, String action, BorhanParams kparams, BorhanFiles kfiles) {
        this.service = service;
        this.action = action;
        this.params = kparams;
        this.files = kfiles;
    }

    public String getService() {
        return this.service;
    }

    public String getAction() {    
    	return this.action;
    }

    public BorhanParams getParams() {
        return this.params;
    }

    public BorhanFiles getFiles() {
        return this.files;
    }

    public BorhanParams getParamsForMultiRequest(int multiRequestNumber) throws BorhanApiException {
        BorhanParams multiRequestParams = new BorhanParams();
        
        params.add("service", service);
        params.add("action", action);
        multiRequestParams.add(Integer.toString(multiRequestNumber), params);
        
        return multiRequestParams;
    }

    public BorhanFiles getFilesForMultiRequest(int multiRequestNumber) {
    	
        BorhanFiles multiRequestFiles = new BorhanFiles();
        multiRequestFiles.add(Integer.toString(multiRequestNumber), files);
        return multiRequestFiles;
    }

}
