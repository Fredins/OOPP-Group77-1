package org.group77.mailMe.model.textFinding;

import org.group77.mailMe.model.data.Email;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for the Filter class' filter routine.
 *
 * @author Hampus Jernkrook
 */
public class TestFilter {
    private static String[] to1;
    private static String[] to2;
    private static String[] to3;
    private static List<Email> emails;
    private final Filter<Email,String> filter = new Filter<>();
    private final InFromPredicate inFromPredicate = new InFromPredicate();
    private final InToPredicate inToPredicate = new InToPredicate();
    private final MaxDatePredicate maxDatePredicate = new MaxDatePredicate();

    @BeforeAll
    public static void setup() {
        to1 = new String[]{"TEST_nr_1@gmail.com",
                "lol@gmail.com"
        };
        to2 = new String[]{"TEST_nr_2@outlook.com",
                "lol@hotmail.com"
        };
        to3 = new String[]{"TEST_nr_3@live.com",
                "haha@gmail.com"
        };
        emails = Arrays.asList(
                new Email("mailme@gmail.com", to1, "First", "contains SAUSAGE", null),
                new Email("memail@live.com", to2, "Second", "contains sausage", null),
                new Email("lol@hotmail.com", to3, "third", "contains no meat at all", null)
        );
    }

    // ===================================================
    // TO
    // ===================================================

    @Test
    public void testFindsNothingInToIfNotThere() {
        // search for a word not in any of the to:s
        List<Email> res = filter.filter(emails, inToPredicate, "HELLO");
        // assert that no email is in the result.
        Assertions.assertEquals(0, res.size());
    }

    @Test
    public void testFindsTo() {
        // find the first and last element of the input.
        List<Email> res = filter.filter(emails, inToPredicate, "gmail");
        Email[] expected = new Email[]{
                emails.get(0),
                emails.get(2)
        };
        //convert result to array
        Email[] actual = res.toArray(new Email[0]);
        //check that first and 4th elements are in both.
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFindsUppercasedTo() {
        // find the first and second elements of the input
        List<Email> res = filter.filter(emails, inToPredicate, "LOL");
        Email[] expected = new Email[]{
                emails.get(0),
                emails.get(1)
        };
        Email[] actual = res.toArray(new Email[0]);
        Assertions.assertArrayEquals(expected, actual);
    }

    // ===================================================
    // FROM
    // ===================================================
    @Test
    public void testFindsNothingIfFromNotThere() {
        // search for a word not in the from
        List<Email> res = filter.filter(emails, inFromPredicate, "stop");
        // assert that no email is in the result.
        Assertions.assertEquals(0, res.size());
    }

    @Test
    public void testFindsFrom() {
        // find the first and second emails of the input
        List<Email> res = filter.filter(emails, inFromPredicate, "me");
        Email[] expected = new Email[]{
                emails.get(0),
                emails.get(1)
        };
        //convert result to array
        Email[] actual = res.toArray(new Email[0]);
        //check that first and second elements are in both.
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFindsUppercasedFrom() {
        // find the last element of the input
        List<Email> res = filter.filter(emails, inFromPredicate, "LOL");
        Email[] expected = new Email[]{
                emails.get(2)
        };
        //convert result to array
        Email[] actual = res.toArray(new Email[0]);
        //check that first and 4th elements are in both.
        Assertions.assertArrayEquals(expected, actual);
    }


    // Running the same test on TextFinder, which calls the previously used Filter-class.
    @Test
    public void testTextFinder() {
        // find the last element of the input
        TextFinder textFinder = new TextFinder();
        List<Email> res = textFinder.filterOnFrom(emails, "LOL");
        Email[] expected = new Email[]{
                emails.get(2)
        };
        //convert result to array
        Email[] actual = res.toArray(new Email[0]);
        //check that first and 4th elements are in both.
        Assertions.assertArrayEquals(expected, actual);
    }


    // ===================================================
    // MAX DATE
    // ===================================================



}