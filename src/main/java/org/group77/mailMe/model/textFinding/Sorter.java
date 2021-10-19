package org.group77.mailMe.model.textFinding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Class implementing an algorithm for sorting a collection of elements.
 *
 * @param <T> - type of the elements in the collection.
 * @author Hampus Jernkrook
 */
class Sorter<T> {
    /**
     * Sort the given list by the total ordering enforced by the comparator.
     *
     * @param list - the list to be sorted.
     * @param comp - the comparator specifying the total order of elements of the list.
     * @return A deep copy of the input list, sorted by the comparator's total order.
     * @author Hampus Jernkrook
     */
    public List<T> sort(List<T> list, Comparator<T> comp) {
        // make a copy of the input
        List<T> cp = new ArrayList<>(list);
        // sort the copy
        cp.sort(comp);
        //return copy instead of input list
        return cp;
    }
}
