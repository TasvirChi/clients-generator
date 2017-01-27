/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.borhan.services;

import java.util.List;

import android.util.Log;

import com.borhan.client.BorhanApiException;
import com.borhan.client.BorhanClient;
import com.borhan.client.services.BorhanBaseEntryService;
import com.borhan.client.services.BorhanFlavorAssetService;
import com.borhan.client.types.BorhanEntryContextDataParams;
import com.borhan.client.types.BorhanEntryContextDataResult;
import com.borhan.client.types.BorhanFilterPager;
import com.borhan.client.types.BorhanFlavorAsset;
import com.borhan.client.types.BorhanFlavorAssetFilter;
import com.borhan.client.types.BorhanFlavorAssetListResponse;

/**
 * Retrieve information and invoke actions on Flavor Asset
 */
public class FlavorAsset {

    /**
     * List Flavor Assets by filter and pager
     *
     * @param TAG constant in your class
     * @param entryId Entry id
     * @param pageindex The page number for which {pageSize} of objects should
     * be retrieved (Default is 1)
     * @param pageSize The number of objects to retrieve. (Default is 30,
     * maximum page size is 500)
     *
     * @return The list of all categories
     *
     * @throws BorhanApiException
     */
    public static List<BorhanFlavorAsset> listAllFlavorAssets(String TAG, String entryId, int pageIndex, int pageSize) throws BorhanApiException {
        // create a new ADMIN-session client
        BorhanClient client = AdminUser.getClient();//RequestsBorhan.getBorhanClient();

        BorhanFlavorAssetService flavorAssetService = client.getFlavorAssetService();

        // create a new filter to filter entries - not mandatory
        BorhanFlavorAssetFilter filter = new BorhanFlavorAssetFilter();
        filter.entryIdEqual = entryId;
        // create a new pager to choose how many and which entries should be recieved
        // out of the filtered entries - not mandatory
        BorhanFilterPager pager = new BorhanFilterPager();
        pager.pageIndex = pageIndex;
        pager.pageSize = pageSize;

        // execute the list action of the mediaService object to recieve the list of entries
        BorhanFlavorAssetListResponse listResponseFlavorAsset = flavorAssetService.list(filter);

        return listResponseFlavorAsset.objects;
    }

    /**
     * Get download URL for the asset
     *
     * @param TAG constant in your class
     * @param id asset id
     *
     * @return The asset url
     */
    public static String getUrl(String TAG, String id) throws BorhanApiException {
        // create a new ADMIN-session client
        BorhanClient client = AdminUser.getClient();//RequestsBorhan.getBorhanClient();

        // create a new mediaService object for our client
        BorhanFlavorAssetService mediaService = client.getFlavorAssetService();
        String url = mediaService.getUrl(id);
        Log.w(TAG, "URL for the asset: " + url);
        return url;
    }
    
    /**
     * Return flavorAsset lists from getContextData call
     * @param TAG
     * @param entryId
     * @param flavorTags
     * @return
     * @throws BorhanApiException
     */
    public static List<BorhanFlavorAsset> listAllFlavorsFromContext(String TAG, String entryId, String flavorTags) throws BorhanApiException {
    	 // create a new ADMIN-session client
        BorhanClient client = AdminUser.getClient();//RequestsBorhan.getBorhanClient();

        BorhanEntryContextDataParams params = new BorhanEntryContextDataParams();
        params.flavorTags = flavorTags;
        BorhanBaseEntryService baseEntryService = client.getBaseEntryService();
        BorhanEntryContextDataResult res = baseEntryService.getContextData(entryId, params);
        return res.flavorAssets;
    }
}
