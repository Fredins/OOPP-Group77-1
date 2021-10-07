package org.group77.mailMe.model;

@FunctionalInterface
public interface ChangeObserver<T> {

  void changed(T newValue);
}
