package org.group77.mailMe.model.textFinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class implementing an algorithm for sorting a collection of elements.
 *
 * @param <T> - type of the elements in the collection.
 * @author Hampus Jernkrook
 */
public class Sorter <T> {
    public List<T> sort(List<T> list, Comparator<T> comp) {
        // make a copy of the input
        List<T> cp = new ArrayList<>();
        Collections.copy(cp, list);
        // sort the copy
        cp.sort(comp);
        //return copy instead of input list
        return cp;
    }
}
