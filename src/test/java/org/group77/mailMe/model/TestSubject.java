package org.group77.mailMe.model;

import org.group77.mailMe.model.data.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

import java.util.*;


public class TestSubject {

  @Test
  public void TestSet(){
    Subject<Folder> subject = new Subject<>(new Folder("inbox", null));
    subject.addObserver(newFolder -> Assertions.assertEquals(newFolder.name(), "archive"));
    subject.set(new Folder("archive", new ArrayList<>()));
  }

  @Test
  public void TestNull(){
    Subject<Folder> subject = new Subject<>(null);
    subject.addObserver(newFolder -> Assertions.assertEquals(newFolder.name(), "inbox"));
    subject.set(new Folder("inbox", null));
  }

  @Test
  public void TestGet() throws Exception {
    Subject<Folder> subject = new Subject<>(null);
    subject.addObserver(newFolder -> {
      throw new Exception("change handler triggered!");
    });

    Assertions.assertDoesNotThrow(subject::get);
  }


}
