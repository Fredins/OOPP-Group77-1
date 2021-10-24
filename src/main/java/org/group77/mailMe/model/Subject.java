package org.group77.mailMe.model;

import java.util.*;

/**
 * @author Martin
 * Subject/Observable
 * notifies observers when the value is changed
 * which is determined by the internall Object::equals
 */
public class Subject<T> {
    private T value;
    private final List<ChangeObserver<T>> observers = new ArrayList<>();

    public Subject(T value) {
        this.value = value;
    }

    /**
     * @return T the value
     * @author Martin
     */
    public T get() {
        return value;
    }

    /**
     * @param value the new value
     * @author Martin
     * always sets the value
     * notify observers if the new value is not the same value or is null
     */
    public void set(T value) {
        if (this.value == null) {
            this.value = value;
            notifyObservers(value);
        } else if (!this.value.equals(value)) {
            this.value = value;
            notifyObservers(value);
        } else {
            this.value = value;
        }
    }

    /**
     * @param changeObserver observer
     * @author Martin
     * adds changeObserver
     */
    public void addObserver(ChangeObserver<T> changeObserver) {
        observers.add(changeObserver);
    }

    /**
     * @param changeObserver observer
     * @author Martin
     * removes changeObserver
     */
    public void removeObserver(ChangeObserver<T> changeObserver) {
        observers.remove(changeObserver);
    }

    /**
     * @author Martin
     * removes all changeObservers
     */
    public void removeAllObservers() {
        observers.clear();
    }

    /**
     * notify observers
     * @param newValue the new value after change
     * @author Martin
     */
    private void notifyObservers(T newValue){
        observers.forEach(obs -> obs.changed(newValue));
    }

}
