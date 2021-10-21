package org.group77.mailMe.model;

import java.util.*;
import java.util.stream.*;

/**
 * @author Martin
 * Subject/Observable for Objects in a list
 * notifies observers when the list is mutated
 */
public class SubjectList<T> {
    private final List<T> list; //
    private final List<ChangeObserver<List<T>>> observers = new ArrayList<>();

    public SubjectList(List<T> list) {
        this.list = list;
    }

    /**
     * @return the list of values
     * @author Martin
     */
    public List<T> get() {
        return list;
    }

    /**
     * @param value new value
     * @author Martin
     */
    public final void add(T value) {
        add(list.size(), value);
    }

    /**
     * @param index the index where the value is inserted
     * @param value new value
     * @author Martin
     */
    public final void add(int index, T value) {
        list.add(index, value);
        notifyObserver();
    }

    /**
     * @param collection collection to be added
     * @author Martin
     */
    public void addAll(Collection<T> collection) {
        addAll(list.size() - 1, collection);
    }

    /**
     * @param index      the index where the collection is inserted
     * @param collection the collection to be added
     * @author Martin
     */
    public void addAll(int index, Collection<T> collection) {
        list.addAll(index, collection);
        notifyObserver();
    }

    /**
     * @author Martin
     */
    public void clear() {
        list.clear();
        notifyObserver();
    }

    /**
     * @param value the value to be removed
     * @author Martin
     */
    public void remove(T value) {
        int index = list.indexOf(value);
        remove(index);
    }

    /**
     * @param index the index of object to be removed
     * @author Martin
     */
    public void remove(int index) {
        list.remove(index);
        notifyObserver();
    }

    /**
     * @param value    value in current list
     * @param newValue replacement value
     * @author Martin
     */
    public void replace(T value, T newValue) {
        replace(list.indexOf(value), newValue);
    }

    /**
     * @param index    index of value in current list
     * @param newValue replacement value
     * @author Martin
     */
    public void replace(int index, T newValue) {
        list.set(index, newValue);
        notifyObserver();
    }

    /**
     * @param collection all the new replacements elements
     * @author Martin
     */
    public void replaceAll(Collection<T> collection) {
        list.clear();
        list.addAll(collection);
        notifyObserver();
    }

    /**
     * @author Martin
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * @author Martin
     */
    public Stream<T> stream() {
        return list.stream();
    }

    /**
     * @param changeObserver observer
     *                       add a observer
     * @author Martin
     */
    public void addObserver(ChangeObserver<List<T>> changeObserver) {
        observers.add(changeObserver);
    }

    /**
     * @param changeObserver observer
     *                       remove a observer
     * @author Martin
     */
    public void removeObserver(ChangeObserver<List<T>> changeObserver) {
        observers.remove(changeObserver);
    }

    /**
     * @author Martin
     * removes all observers
     */
    public void removeAllObservers() {
        observers.clear();
    }

    /**
     * @author Martin
     * notifes observers
     */
    private void notifyObserver() {
        observers.forEach(obs -> obs.changed(list));
    }

}
