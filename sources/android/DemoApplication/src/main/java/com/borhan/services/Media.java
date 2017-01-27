package com.borhan.services;

import java.io.File;
import java.util.List;

import android.util.Log;

import com.borhan.client.BorhanApiException;
import com.borhan.client.BorhanClient;
import com.borhan.client.enums.BorhanEntryType;
import com.borhan.client.enums.BorhanMediaType;
import com.borhan.client.services.BorhanMediaService;
import com.borhan.client.types.BorhanBaseEntry;
import com.borhan.client.types.BorhanFilterPager;
import com.borhan.client.types.BorhanMediaEntry;
import com.borhan.client.types.BorhanMediaEntryFilter;
import com.borhan.client.types.BorhanMediaListResponse;

/**
 * Media service lets you upload and manage media files (images / videos &
 * audio)
 */
public class Media {

    /**
     * Get a list of all media data from the borhan server
     *
     * @param TAG constant in your class
     * @param mediaType Type of entries
     * @param pageSize The number of objects to retrieve. (Default is 30,
     * maximum page size is 500)
     *
     * @throws BorhanApiException
     */
    public static List<BorhanMediaEntry> listAllEntriesByIdCategories(String TAG, BorhanMediaEntryFilter filter, int pageIndex, int pageSize) throws BorhanApiException {
        // create a new ADMIN-session client
        BorhanClient client = AdminUser.getClient();//RequestsBorhan.getBorhanClient();

        // create a new mediaService object for our client
        BorhanMediaService mediaService = client.getMediaService();

        // create a new pager to choose how many and which entries should be recieved
        // out of the filtered entries - not mandatory
        BorhanFilterPager pager = new BorhanFilterPager();
        pager.pageIndex = pageIndex;
        pager.pageSize = pageSize;

        // execute the list action of the mediaService object to recieve the list of entries
        BorhanMediaListResponse listResponse = mediaService.list(filter, pager);

        // loop through all entries in the reponse list and print their id.
        Log.w(TAG, "Entries list :");
        int i = 0;
        for (BorhanMediaEntry entry : listResponse.objects) {
            Log.w(TAG, ++i + " id:" + entry.id + " name:" + entry.name + " type:" + entry.type + " dataURL: " + entry.dataUrl);
        }
        return listResponse.objects;
    }

    /**
     * Get media entry by ID
     *
     * @param TAG constant in your class
     * @param entryId Media entry id
     *
     * @return Information about the entry
     *
     * @throws BorhanApiException
     */
    public static BorhanMediaEntry getEntrybyId(String TAG, String entryId) throws BorhanApiException {
        // create a new ADMIN-session client
        BorhanClient client = AdminUser.getClient();//RequestsBorhan.getBorhanClient();

        // create a new mediaService object for our client
        BorhanMediaService mediaService = client.getMediaService();
        BorhanMediaEntry entry = mediaService.get(entryId);
        Log.w(TAG, "Entry:");
        Log.w(TAG, " id:" + entry.id + " name:" + entry.name + " type:" + entry.type + " categories: " + entry.categories);
        return entry;
    }

    /**
     * Creates an empty media entry and assigns basic metadata to it.
     *
     * @param TAG constant in your class
     * @param category Category name which belongs to an entry
     * @param name Name of an entry
     * @param description Description of an entry
     * @param tag Tag of an entry
     *
     * @return Information about created the entry
     *
     *
     */
    public static BorhanMediaEntry addEmptyEntry(String TAG, String category, String name, String description, String tag) {

        try {
            BorhanClient client = AdminUser.getClient();

            Log.w(TAG, "\nCreating an empty Borhan Entry (without actual media binary attached)...");

            BorhanMediaEntry entry = new BorhanMediaEntry();
            entry.mediaType = BorhanMediaType.VIDEO;
            entry.categories = category;
            entry.name = name;
            entry.description = description;
            entry.tags = tag;

            BorhanMediaEntry newEntry = client.getMediaService().add(entry);
            Log.w(TAG, "\nThe id of our new Video Entry is: " + newEntry.id);
            return newEntry;
        } catch (BorhanApiException e) {
            e.printStackTrace();
            Log.w(TAG, "err: " + e.getMessage());
            return null;
        }
    }

    /**
     * Create an entry
     *
     * @param TAG constant in your class
     * @param String fileName File to upload.
     * @param String entryName Name for the new entry.
     *
     * @throws BorhanApiException
     */
    public static void addEntry(String TAG, String fileName, String entryName) throws BorhanApiException {
        // create a new USER-session client
        BorhanClient client = AdminUser.getClient();

        // upload the new file and recieve the token that identifies it on the borhan server
        File up = new File(fileName);
        String token = client.getBaseEntryService().upload(up);

        // create a new entry object with the required meta-data
        BorhanBaseEntry entry = new BorhanBaseEntry();
        entry.name = entryName;
        entry.categories = "Comedy";
        entry.type = BorhanEntryType.MEDIA_CLIP;

        // add the entry you created to the borhan server, by attaching it with the uploaded file
        BorhanBaseEntry newEntry = client.getBaseEntryService().addFromUploadedFile(entry, token);

        // newEntry now contains the information of the new entry that was just created on the server
        Log.w(TAG, "New entry created successfuly with ID " + newEntry.id);
    }
}
