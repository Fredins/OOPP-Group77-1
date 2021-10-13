package org.group77.mailMe.model;

/**
 * @author Martin
 * lamda observer that is supposed to react to change
 * behaviour is determined on construction
 * e.g. subject.addObserver(newValue -> System.out.printLn("do something!"))
 */
@FunctionalInterface
public interface ChangeObserver<T> {

  /**
   * @author Martin
   * @param newValue the newValue after change
   */
  void changed(T newValue);
}
