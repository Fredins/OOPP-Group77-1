package org.group77.mailMe.model.textFinding;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * Class implementing an algorithm for filtering a collection of elements.
 *
 * @param <T> - type of the elements in the collection.
 * @param <U> - type of the other argument supplied to the BiPredicate.
 * @author Hampus Jernkrook
 */
class Filter<T, U> {
    /**
     * Filters a collection based on the given predicate.
     *
     * @param list      - list with elements of type T.
     * @param predicate - predicate for types T and U. Must have a method test(list,u).
     * @param u         - object of type U required for predicate.test(list, u).
     * @return the filtered list.
     * @author Hampus Jernkrook
     */
    public List<T> filter(List<T> list, BiPredicate<T, U> predicate, U u) {
        // init list of elements left after filtering process.
        List<T> res = new ArrayList<>();
        // for each element in list, add it to the result if the predicate holds.
        for (T t : list) {
            if (predicate.test(t,u)) {
                res.add(t);
            }
        }
        return res;
    }
}
