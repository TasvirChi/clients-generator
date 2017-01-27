package com.borhan.utils;

import java.util.Comparator;

import com.borhan.client.types.BorhanCategory;
import com.borhan.client.types.BorhanFlavorAsset;
import com.borhan.client.types.BorhanMediaEntry;

/**
 * The class performs a sort
 */
public class Sort<T> implements Comparator<T> {

    private String filter = "name";
    private String direction = "compareTo";

    /**
     * Constructor Description of Sort<T>
     *
     * @param filter Specify which field to sort
     * @param direction Specifies the sort direction
     */
    public Sort(String filter, String direction) {
        this.filter = filter;
        this.direction = direction;
    }

    /**
     * Compares its two arguments for order. Returns a negative integer, zero,
     * or a positive integer as the first argument is less than, equal to, or
     * greater than the second.
     *
     * @param paramT1 the first object to be compared.
     * @param paramT2 the second object to be compared.
     *
     * @return a negative integer, zero, or a positive integer as the first
     * argument is less than, equal to, or greater than the second.
     *
     * @throws ClassCastException - if the arguments' types prevent them from
     * being compared by this Comparator.
     */
    @Override
    public int compare(T paramT1, T paramT2) {

        int res = 0;
        if (paramT1 instanceof BorhanMediaEntry && paramT2 instanceof BorhanMediaEntry) {
            if (this.filter.equals("name")) {
                res = ((BorhanMediaEntry) paramT1).name.compareTo(((BorhanMediaEntry) paramT2).name);
            }
            if (this.filter.equals("plays") && this.direction.equals("compareTo")) {
                res = new Integer(((BorhanMediaEntry) paramT1).plays).compareTo(new Integer(((BorhanMediaEntry) paramT2).plays));
            } else {
                res = ((BorhanMediaEntry) paramT2).plays - ((BorhanMediaEntry) paramT1).plays;
            }
            if (this.filter.equals("createdAt")) {
                res = new Integer(((BorhanMediaEntry) paramT1).createdAt).compareTo(new Integer(((BorhanMediaEntry) paramT2).createdAt));
            }
        }
        if (paramT1 instanceof BorhanCategory && paramT2 instanceof BorhanCategory) {
            res = ((BorhanCategory) paramT1).name.compareTo(((BorhanCategory) paramT2).name);
        }
        if (paramT1 instanceof BorhanFlavorAsset && paramT2 instanceof BorhanFlavorAsset) {
            res = ((BorhanFlavorAsset) paramT2).bitrate - ((BorhanFlavorAsset) paramT1).bitrate;
        }
        return res;
    }
}
