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

  public Subject(T value){
    this.value = value;
  }

  /**
   * @author Martin
   * @return T the value
   */
  public T get(){
    return value;
  }

  /**
   * @author Martin
   * always sets the value
   * notify observers if the new value is not the same value or is null
   * @param value the new value
   */
  public void set(T value){
    if (this.value == null) {
      this.value = value;
      observers.forEach(obs -> obs.changed(value));
    }else if(!this.value.equals(value)){
      this.value = value;
      observers.forEach(obs -> obs.changed(value));
    }else{
      this.value = value;
    }
  }

  /**
   * @author Martin
   * adds changeObserver
   * @param changeObserver observer
   */
  public void addObserver(ChangeObserver<T> changeObserver){
    observers.add(changeObserver);
  }

  /**
   * @author Martin
   * removes changeObserver
   * @param changeObserver observer
   */
  public void removeObserver(ChangeObserver<T> changeObserver){
    observers.remove(changeObserver);
  }

  /**
   * @author Martin
   * removes all changeObservers
   */
  public void removeAllObservers(){
    observers.clear();
  }

}
