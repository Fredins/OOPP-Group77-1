package org.group77.mailMe.model;

import org.group77.mailMe.model.data.*;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.stream.*;

public class TestSubjectList {

  /**
   * @author Martin
   */
  @Test
  public void TestGet(){
    List<Folder> folders = List.of(new Folder("inbox", null));
    SubjectList<Folder> subjectList = new SubjectList<>(folders);
    Assertions.assertEquals(subjectList.get(), folders);
  }

  /**
   * @author Martin
   */
  @Test
  public void TestAddValue(){
    List<Folder> folders = new ArrayList<>(
      List.of(new Folder("inbox", null)));
    SubjectList<Folder> subjectList = new SubjectList<>(folders);
    Folder folder = new Folder("archive", null);
    subjectList.add(folder);
    Assertions.assertTrue(subjectList.get().containsAll(
      Stream.concat(Stream.of(folder), folders.stream()).collect(Collectors.toList())
    ));
  }

  /**
   * @author Martin
   */
  @Test
  public void TestAddValueIndex(){
    List<Folder> folders = new ArrayList<>(
      List.of(new Folder("inbox", null)));
    SubjectList<Folder> subjectList = new SubjectList<>(folders);
    Folder folder = new Folder("archive", null);
    subjectList.add(1, folder);
    Assertions.assertEquals(subjectList.get().get(1), folder);
  }

  /**
   * @author Martin
   */
  @Test
  public void TestAdd(){
    List<Folder> folders = new ArrayList<>(
      List.of(new Folder("inbox", null)));
    SubjectList<Folder> subjectList = new SubjectList<>(folders);
    subjectList.addObserver(newFolders -> Assertions.assertEquals(subjectList.get().size(), 2));
    subjectList.add(new Folder("archive", null));
  }

  /**
   * @author Martin
   */
  @Test
  public void TestAddAll(){
    List<Folder> folders = new ArrayList<>(List.of(new Folder("inbox", null)));
    SubjectList<Folder> subjectList = new SubjectList<>(folders);
    List<Folder> folders1 = new ArrayList<>(
      List.of(new Folder("archive", null), new Folder("sent", null)));
    subjectList.addObserver(newFolders -> Assertions.assertEquals(subjectList.get().size(), 3));
    subjectList.addAll(folders1);
  }

  /**
   * @author Martin
   */
  @Test
  public void TestClear(){
    List<Folder> folders = new ArrayList<>(List.of(new Folder("inbox", null)));
    SubjectList<Folder> subjectList = new SubjectList<>(folders);
    subjectList.addObserver(newFolders -> Assertions.assertTrue(newFolders.isEmpty()));
    subjectList.clear();
  }

  /**
   * @author Martin
   */
  @Test
  public void TestRemove(){
    Folder folder = new Folder("inbox", null);
    List<Folder> folders = new ArrayList<>(List.of(folder));
    SubjectList<Folder> subjectList = new SubjectList<>(folders);
    subjectList.addObserver(newFolders -> Assertions.assertTrue(newFolders.isEmpty()));
    subjectList.remove(folder);
  }

  /**
   * @author Martin
   */
  @Test
  public void TestRemoveIndex(){
    List<Folder> folders = new ArrayList<>(List.of(new Folder("inbox", null)));
    SubjectList<Folder> subjectList = new SubjectList<>(folders);
    subjectList.addObserver(newFolders -> Assertions.assertTrue(newFolders.isEmpty()));
    subjectList.remove(0);
  }

  /**
   * @author Martin
   */
  @Test
  public void TestReplace(){
    Folder inbox = new Folder("inbox", null);
    Folder archive = new Folder("archive", null);
    SubjectList<Folder> subjectList = new SubjectList<>(new ArrayList<>(List.of(inbox)));
    subjectList.addObserver(newFolders -> Assertions.assertEquals(newFolders.get(0), archive));
    subjectList.replace(inbox, archive);
  }


  /**
   * @author Martin
   */
  @Test
  public void TestReplaceAll(){
    List<Folder> folders = new ArrayList<>(
      List.of(new Folder("inbox", null), new Folder("archive", null)));
    List<Folder> folders1 = new ArrayList<>(List.of(new Folder("arcive", null)));
    SubjectList<Folder> subjectList = new SubjectList<>(folders);
    subjectList.addObserver(newFolders -> Assertions.assertEquals(subjectList.get().get(0), folders1.get(0)));
    subjectList.replaceAll(folders1);
  }

  /**
   * @author Martin
   */
  @Test
  public void TestIsEmpty(){
    SubjectList<Folder> subjectList = new SubjectList<>(new ArrayList<>());
    Assertions.assertTrue(subjectList.isEmpty());
  }

  /**
   * @author Martin
   */
  @Test
  public void TestStream(){
    List<Folder> folders = new ArrayList<>(
      List.of(new Folder("inbox", null), new Folder("archive", null)));
    SubjectList<Folder> subjectList = new SubjectList<>(folders);
    Assertions.assertTrue(subjectList.stream().allMatch(folders::contains));
  }

  /**
   * @author Martin
   */
  @Test
  public void TestRemoveObserver(){
    SubjectList<Folder> subjectList = new SubjectList<>(new ArrayList<>(List.of(new Folder("inbox", null))));
    String[] arr = new String[]{"not changed"};
    ChangeObserver<List<Folder>> observer = newFolders -> arr[0] = "changed";
    subjectList.addObserver(observer);
    subjectList.removeObserver(observer);
    subjectList.clear();
    Assertions.assertEquals(arr[0], "not changed");
  }

  /**
   * @author Martin
   */
  @Test
  public void TestRemoveAllObserver(){
    SubjectList<Folder> subjectList = new SubjectList<>(new ArrayList<>(List.of(new Folder("inbox", null))));
    String[] arr = new String[]{"not changed"};
    ChangeObserver<List<Folder>> observer = newFolders -> arr[0] = "changed";
    ChangeObserver<List<Folder>> observer1 = newFolders -> arr[0] = "changed";
    subjectList.addObserver(observer);
    subjectList.addObserver(observer1);
    subjectList.removeAllObservers();
    subjectList.clear();
    Assertions.assertEquals(arr[0], "not changed");
  }
}
