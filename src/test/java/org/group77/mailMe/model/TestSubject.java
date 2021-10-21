package org.group77.mailMe.model;

import org.junit.jupiter.api.*;

import java.util.*;


public class TestSubject {

  /**
   * @author Martin
   */
  @Test
  public void TestNoNullPointerException(){
    Subject<Folder> subject = new Subject<>(null);
    Assertions.assertDoesNotThrow(() -> subject.addObserver(newFolder -> {} ));
  }

  /**
   * @author Martin
   */
  @Test
  public void TestSet(){
    Subject<Folder> subject = new Subject<>(new Folder("inbox", null));
    subject.addObserver(newFolder -> Assertions.assertEquals(newFolder.name(), "archive"));
    subject.set(new Folder("archive", new ArrayList<>()));
  }

  /**
   * @author Martin
   */
  @Test
  public void TestNullSet(){
    Subject<Folder> subject = new Subject<>(null);
    subject.addObserver(newFolder -> Assertions.assertEquals(newFolder.name(), "inbox"));
    subject.set(new Folder("inbox", null));
  }

  /**
   * @author Martin
   */
  @Test
  public void TestSetNull(){
    Subject<Folder> subject = new Subject<>(new Folder("inbox", null));
    subject.addObserver(Assertions::assertNull);
    subject.set(null);
  }

  /**
   * @author Martin
   */
  @Test
  public void TestSetSameVal(){
    Folder folder = new Folder("inbox", null);
    Subject<Folder> subject = new Subject<>(folder);
    String[] arr = new String[]{"not changed"};
    subject.addObserver(newFolder -> arr[0] = "changed" );
    subject.set(folder);
    Assertions.assertEquals(arr[0], "not changed");
  }

  /**
   * @author Martin
   */
  @Test
  public void TestGet(){
    Folder folder = new Folder("inbox", null);
    Subject<Folder> subject = new Subject<>(folder);
    Assertions.assertEquals(subject.get(), folder);
  }

  /**
   * @author Martin
   */
  @Test
  public void TestGetDoesNotTrigger(){
    Subject<Folder> subject = new Subject<>(null);
    String[] arr = new String[]{"not changed"};
    subject.addObserver(newFolder -> arr[0] = "changed" );
    Assertions.assertEquals(arr[0], "not changed");
  }

  /**
   * @author Martin
   */
  @Test
  public void removeObserver(){
    Subject<Folder> subject = new Subject<>(null);
    String[] arr = new String[]{"not changed"};
    ChangeObserver<Folder> observer = newFolder -> arr[0] = "changed";
    subject.addObserver(observer);
    subject.removeObserver(observer);
    Assertions.assertEquals(arr[0], "not changed");
  }

  /**
   * @author Martin
   */
  @Test
  public void removeAllObservers(){
    Subject<Folder> subject = new Subject<>(null);
    String[] arr = new String[]{"not changed"};
    ChangeObserver<Folder> observer = newFolder -> arr[0] = "changed";
    ChangeObserver<Folder> observer1 = newFolder -> arr[0] = "changed";
    subject.addObserver(observer);
    subject.addObserver(observer1);
    subject.removeAllObservers();
    Assertions.assertEquals(arr[0], "not changed");
  }
}
