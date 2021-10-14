package org.group77.mailMe.model.textFinding;

import org.group77.mailMe.model.data.Email;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

public class TestSorting {
    private static Email[] emails;
    private static Email[] expectedNewToOld;
    private static Email[] expectedOldToNew;

    @BeforeAll
    public static void Setup() {
        // just some dummy values
        String from = "lol@gmail.com";
        String[] to = new String[]{"lol@gmail.com"};
        String subject = "subject";
        String content = "content";
        // emails to use as input. The only thing changing is the date, for the most part.
        emails = new Email[]{
                // 2019
                new Email(from, to, subject, content,
                        LocalDateTime.of(2019, Month.OCTOBER, 14, 8, 19)),
                new Email(from, to, subject, content,
                        LocalDateTime.of(2019, Month.DECEMBER, 24, 8, 0)),
                new Email("different_from@gmail.com", to, subject, content, //DIFFERENT FROM ADDRESS FROM ABOVE.
                        LocalDateTime.of(2019, Month.DECEMBER, 24, 8, 0)),
                // 2021
                new Email(from, to, subject, content,
                        LocalDateTime.of(2021, Month.OCTOBER, 14, 8, 19)),
                new Email(from, to, subject, content,
                        LocalDateTime.of(2021, Month.SEPTEMBER, 1, 8, 0)),
                new Email(from, to, subject, content,
                        LocalDateTime.of(2021, Month.MARCH, 31, 18, 20)),
                new Email(from, to, subject, content,
                        LocalDateTime.of(2021, Month.JANUARY, 23, 8, 54)),
                // 2020
                new Email(from, to, subject, content,
                        LocalDateTime.of(2020, Month.NOVEMBER, 14, 8, 19)),
                new Email(from, to, subject, content,
                        LocalDateTime.of(2020, Month.AUGUST, 18, 4, 30)),
                new Email(from, to, subject, content,
                        LocalDateTime.of(2020, Month.FEBRUARY, 28, 5, 20))
        };
        // expected order of new to old
        // expected order of old to new
    }

    // ===================================================
    // NEW TO OLD - NewToOldComparator
    // ===================================================
    @Test
    public void TestBeforeNewToOld() {
        // first email is before the second. res should be -1.
        int res = new NewToOldComparator().compare(emails[0], emails[1]);
        Assertions.assertEquals(-1, res);
    }

    @Test
    public void TestAfterNewToOld() {
        // fourth email is after the fifth. res should be 1.
        int res = new NewToOldComparator().compare(emails[3], emails[4]);
        Assertions.assertEquals(1, res);
    }

    @Test
    public void TestSameDateNewToOld() {
        // second and third email has same date. res should be 0.
        int res = new NewToOldComparator().compare(emails[1], emails[2]);
        Assertions.assertEquals(0, res);
    }

    // ===================================================
    // NEW TO OLD - Sorter
    // ===================================================
    @Test
    public void TestSortNewToOld() {

    }

    // ===================================================
    // NEW TO OLD - TextFinder
    // ===================================================
    @Test
    public void TestSortNewToOld2() {

    }

    // ===================================================
    // OLD TO NEW - NewToOldComparator reversed.
    // ===================================================
    @Test
    public void TestBeforeOldToNew() {
        // first email is before the second. res should be 1.
        int res = new NewToOldComparator().reversed().compare(emails[0], emails[1]);
        Assertions.assertEquals(1, res);
    }

    @Test
    public void TestAfterOldToNew() {
        // fourth email is after the fifth. res should be -1.
        int res = new NewToOldComparator().reversed().compare(emails[3], emails[4]);
        Assertions.assertEquals(-1, res);
    }

    @Test
    public void TestSameDateOldToNew() {
        // second and third email has same date. res should be 0.
        int res = new NewToOldComparator().compare(emails[1], emails[2]);
        Assertions.assertEquals(0, res);
    }

    // ===================================================
    // OLD TO NEW - Sorter
    // ===================================================
    @Test
    public void TestSorOldToNew() {

    }

    // ===================================================
    // OLD TO NEW - TextFinder
    // ===================================================
    @Test
    public void TestSortOldToNew2() {

    }


}
