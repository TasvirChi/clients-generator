package com.borhan.services;

import java.util.List;

import android.util.Log;

import com.borhan.client.BorhanApiException;
import com.borhan.client.BorhanClient;
import com.borhan.client.services.BorhanCategoryService;
import com.borhan.client.types.BorhanCategory;
import com.borhan.client.types.BorhanCategoryFilter;
import com.borhan.client.types.BorhanCategoryListResponse;
import com.borhan.client.types.BorhanFilterPager;

/**
 * Add & Manage Categories *
 */
public class Category {

    /**
     * Get a list of all categories on the borhan server
     *
     * @param TAG constant in your class
     * @param pageindex The page number for which {pageSize} of objects should
     * be retrieved (Default is 1)
     * @param pageSize The number of objects to retrieve. (Default is 30,
     * maximum page size is 500)
     *
     * @return The list of all categories
     *
     * @throws BorhanApiException
     */
    public static List<BorhanCategory> listAllCategories(String TAG, int pageIndex, int pageSize) throws BorhanApiException {
        // create a new ADMIN-session client
        BorhanClient client = AdminUser.getClient();//RequestsBorhan.getBorhanClient();

        // create a new mediaService object for our client
        BorhanCategoryService categoryService = client.getCategoryService();

        // create a new filter to filter entries - not mandatory
        BorhanCategoryFilter filter = new BorhanCategoryFilter();
        //filter.mediaTypeEqual = mediaType;

        // create a new pager to choose how many and which entries should be recieved
        // out of the filtered entries - not mandatory
        BorhanFilterPager pager = new BorhanFilterPager();
        pager.pageIndex = pageIndex;
        pager.pageSize = pageSize;

        // execute the list action of the mediaService object to recieve the list of entries
        BorhanCategoryListResponse listResponse = categoryService.list(filter);

        // loop through all entries in the reponse list and print their id.
        Log.w(TAG, "Entries list :");
        int i = 0;
        for (BorhanCategory entry : listResponse.objects) {
            Log.w(TAG, ++i + " id:" + entry.id + " name:" + entry.name + " depth: " + entry.depth + " fullName: " + entry.fullName);
        }
        return listResponse.objects;
    }
}
