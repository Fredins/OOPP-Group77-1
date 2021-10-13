package org.group77.mailMe.model;

import java.util.*;
import java.util.stream.*;

/**
 * @author Martin
 * Subject/Observable for Objects in a list
 * notifies observers when the list is mutated
 */
public class SubjectList<T> {
  private final List<T> list;
  private final List<ChangeObserver<List<T>>> observers = new ArrayList<>();

  public SubjectList(List<T> list){
    this.list = list;
  }

  /**
   * @author Martin
   * @return the list of values
   */
  public List<T> get(){
    return list;
  }

  /**
   * @author Martin
   * @param value new value
   */
  public final void add(T value){
    add(list.size(), value);
  }

  /**
   * @author Martin
   * @param index the index where the value is inserted
   * @param value new value
   */
  public final void add(int index, T value){
    list.add(index, value);
    notifyObserver();
  }

  /**
   * @author Martin
   * @param collection collection to be added
   */
  public void addAll(Collection<T> collection){
    addAll(list.size() - 1, collection);
  }

  /**
   * @author Martin
   * @param index the index where the collection is inserted
   * @param collection the collection to be added
   */
  public void addAll(int index, Collection<T> collection){
    list.addAll(index, collection);
    notifyObserver();
  }

  /**
   * @author Martin
   */
  public void clear(){
    list.clear();
    notifyObserver();
  }

  /**
   * @author Martin
   * @param value the value to be removed
   */
  public void remove(T value){
    int index = list.indexOf(value);
    remove(index);
  }

  /**
   * @author Martin
   * @param index the index of object to be removed
   */
  public void remove(int index){
    list.remove(index);
    notifyObserver();
  }

  /**
   * @author Martin
   * @param value value in current list
   * @param newValue replacement value
   */
  public void replace(T value, T newValue){
   replace(list.indexOf(value), newValue);
  }

  /**
   * @author Martin
   * @param index index of value in current list
   * @param newValue replacement value
   */
  public void replace(int index, T newValue){
    list.set(index, newValue);
    notifyObserver();
  }

  /**
   * @author Martin
   * @param collection all the new replacements elements
   */
  public void replaceAll(Collection<T> collection){
    list.clear();
    list.addAll(collection);
    notifyObserver();
  }

  /**
   * @author Martin
   */
  public boolean isEmpty(){
    return list.isEmpty();
  }

  /**
   * @author Martin
   */
  public Stream<T> stream(){
    return list.stream();
  }

  /**
   * @author Martin
   * @param changeObserver observer
   * add a observer
   */
  public void addObserver(ChangeObserver<List<T>> changeObserver){
    observers.add(changeObserver);
  }

  /**
   * @author Martin
   * @param changeObserver observer
   * remove a observer
   */
  public void removeObserver(ChangeObserver<List<T>> changeObserver){
    observers.remove(changeObserver);
  }

  /**
   * @author Martin
   * removes all observers
   */
  public void removeAllObservers(){
    observers.clear();
  }

  /**
   * @author Martin
   * notifes observers
   */
  private void notifyObserver(){
    observers.forEach(obs -> obs.changed(list));
  }

}
