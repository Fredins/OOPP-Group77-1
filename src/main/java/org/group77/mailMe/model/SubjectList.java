package org.group77.mailMe.model;

import java.util.*;
import java.util.stream.*;

public class SubjectList<T> {
  private final List<T> list;
  private final List<ChangeObserver<List<T>>> observers = new ArrayList<>();

  public SubjectList(List<T> list){
    this.list = list;
  }

  public List<T> get(){
    return list;
  }

  public final void add(T value){
    add(list.size(), value);
  }

  public final void add(int index, T value){
    list.add(index, value);
    notifyObserver();
  }

  public void addAll(Collection<T> collection){
    addAll(list.size() - 1, collection);
  }

  public void addAll(int index, Collection<T> collection){
    list.addAll(index, collection);
    notifyObserver();
  }

  public void clear(){
    list.clear();
    notifyObserver();
  }

  public void remove(T value){
    int index = list.indexOf(value);
    remove(index);
  }

  public void remove(int index){
    list.remove(index);
    notifyObserver();
  }

  public void replace(T value, T newValue){
   replace(list.indexOf(value), newValue);
  }

  public void replace(int index, T newValue){
    list.set(index, newValue);
    notifyObserver();
  }

  public void replaceAll(Collection<T> collection){
    list.clear();
    list.addAll(collection);
    notifyObserver();
  }

  public boolean isEmpty(){
    return list.isEmpty();
  }

  public Stream<T> stream(){
    return list.stream();
  }

  public void addObserver(ChangeObserver<List<T>> changeObserver){
    observers.add(changeObserver);
  }

  public void removeObserver(ChangeObserver<List<T>> changeObserver){
    observers.remove(changeObserver);
  }

  public void removeAllObservers(){
    observers.clear();
  }

  private void notifyObserver(){
    observers.forEach(obs -> obs.changed(list));
  }

}
