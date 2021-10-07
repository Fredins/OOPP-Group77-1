package org.group77.mailMe.model;

import java.util.*;

public class Subject<T> {
  private T value;
  private final List<ChangeObserver<T>> observers = new ArrayList<>();

  public Subject(T value){
    this.value = value;
  }

  public T get(){
    return value;
  }

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

  public void addObserver(ChangeObserver<T> changeObserver){
    observers.add(changeObserver);
  }

  public void removeObserver(ChangeObserver<T> changeObserver){
    observers.remove(changeObserver);
  }

  public void removeAllObservers(){
    observers.clear();
  }

}
