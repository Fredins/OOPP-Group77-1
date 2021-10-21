package org.group77.mailMe.model.textFinding;

import org.group77.mailMe.model.Email;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for the sorting functionality based on emails' dates.
 * Tests both sorting based on new to old dates and old to new.
 *
 * @author Hampus Jernkrook
 */
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
                new Email(from, to, subject, content, null,
                        LocalDateTime.of(2019, Month.OCTOBER, 14, 8, 19)),
                new Email(from, to, subject, content, null,
                        LocalDateTime.of(2019, Month.DECEMBER, 24, 8, 0)),
                new Email("different_from@gmail.com", to, subject, content, null,//DIFFERENT FROM ADDRESS FROM ABOVE.
                        LocalDateTime.of(2019, Month.DECEMBER, 24, 8, 0)),
                // 2021
                new Email(from, to, subject, content, null,
                        LocalDateTime.of(2021, Month.OCTOBER, 14, 8, 19)),
                new Email(from, to, subject, content, null,
                        LocalDateTime.of(2021, Month.SEPTEMBER, 1, 8, 0)),
                new Email(from, to, subject, content, null,
                        LocalDateTime.of(2021, Month.MARCH, 31, 18, 20)),
                new Email(from, to, subject, content, null,
                        LocalDateTime.of(2021, Month.JANUARY, 23, 8, 54)),
                // 2020
                new Email(from, to, subject, content, null,
                        LocalDateTime.of(2020, Month.NOVEMBER, 14, 8, 19)),
                new Email(from, to, subject, content, null,
                        LocalDateTime.of(2020, Month.AUGUST, 18, 4, 30)),
                new Email(from, to, subject, content, null,
                        LocalDateTime.of(2020, Month.FEBRUARY, 28, 5, 20))
        };
        // expected order of new to old
        expectedNewToOld = new Email[]{
                // 2021
                emails[3], emails[4], emails[5], emails[6],
                // 2020
                emails[7], emails[8], emails[9],
                // 2019
                emails[1], emails[2], emails[0] // 1 and 2 have the same date. Order may be reversed, but should not be.
        };
        // expected order of old to new. Reverse order of new to old.
        expectedOldToNew = new Email[]{
                //2019
                emails[0], emails[1], emails[2], // 1 and 2 have the same date. Order may be reversed, but should not be.
                //2020
                emails[9], emails[8], emails[7],
                //2021
                emails[6], emails[5], emails[4], emails[3]
        };
    }

    // ===================================================
    // NEW TO OLD - NewToOldComparator
    // ===================================================
    @Test
    public void TestBeforeNewToOld() {
        // first email is before the second (second should come first in sorting). res should be 1.
        int res = new NewToOldComparator().compare(emails[0], emails[1]);
        Assertions.assertEquals(1, res);
    }

    @Test
    public void TestAfterNewToOld() {
        // fourth email is after the fifth (fourth first in sorting). res should be -1.
        int res = new NewToOldComparator().compare(emails[3], emails[4]);
        Assertions.assertEquals(-1, res);
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
    public void TestSortNewToOldSorter() {
        List<Email> res = new Sorter<Email>().sort(Arrays.asList(emails), new NewToOldComparator());
        Email[] actual = res.toArray(new Email[0]);
        Assertions.assertArrayEquals(expectedNewToOld, actual);
    }

    // ===================================================
    // NEW TO OLD - TextFinder
    // ===================================================
    @Test
    public void TestSortNewToOldTextFinder() {
        List<Email> res = new TextFinder().sortByNewToOld(Arrays.asList(emails));
        Email[] actual = res.toArray(new Email[0]);
        Assertions.assertArrayEquals(expectedNewToOld, actual);
    }

    // ===================================================
    // OLD TO NEW - NewToOldComparator reversed.
    // ===================================================
    @Test
    public void TestBeforeOldToNew() {
        // first email is before the second (first first in sorting). res should be -1.
        int res = new NewToOldComparator().reversed().compare(emails[0], emails[1]);
        Assertions.assertEquals(-1, res);
    }

    @Test
    public void TestAfterOldToNew() {
        // fourth email is after the fifth (fifth first in sorting). res should be 1.
        int res = new NewToOldComparator().reversed().compare(emails[3], emails[4]);
        Assertions.assertEquals(1, res);
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
    public void TestSortOldToNewSorter() {
        List<Email> res = new Sorter<Email>().sort(Arrays.asList(emails), new NewToOldComparator().reversed());
        Email[] actual = res.toArray(new Email[0]);
        Assertions.assertArrayEquals(expectedOldToNew, actual);
    }

    // ===================================================
    // OLD TO NEW - TextFinder
    // ===================================================
    @Test
    public void TestSortOldToNewTextFinder() {
        List<Email> res = new TextFinder().sortByOldToNew(Arrays.asList(emails));
        Email[] actual = res.toArray(new Email[0]);
        Assertions.assertArrayEquals(expectedOldToNew, actual);
    }


}
