package org.group77.mailMe.model.textFinding;

import org.group77.mailMe.model.Email;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for the TextFinder's, Filter's and InAnyTextFieldPredicate's search routine.
 *
 * @author Hampus Jernkrook
 */
public class TestSearching {
    private static String[] to;
    private static List<Email> input;
    private static final InAnyTextFieldPredicate inAnyTextFieldPredicate = new InAnyTextFieldPredicate();
    private static final Filter<Email, String> filter = new Filter<>();

    @BeforeAll
    public static void Setup() {
        to = new String[]{
                "TEST_nr_1@gmail.com",
                "TEST_nr_2@gmail.com",
                "TEST_nr_3@gmail.com"
        };
        input = Arrays.asList(
                new Email("lol@gmail.com", to, "Upper", "contains SAUSAGE", null, null),
                new Email("lol@gmail.com", to, "Lower", "contains sausage", null, null),
                new Email("lol@gmail.com", to, "Nothing", "contains no meat at all", null, null)
        );
    }

    //===========================
    //  InAnyTextFieldPredicate
    //===========================
    @Test
    public void TestFindsUpperCasedTos() {
        // TEST_nr_2 is in the to:s
        Assertions.assertTrue( inAnyTextFieldPredicate.test(
                input.get(0),
                "test_nr_2"
        ));
    }

    @Test
    public void TestFindsCapitalizedSubject() {
        Assertions.assertTrue(
                inAnyTextFieldPredicate.test(
                        input.get(2),
                        "not"
                )
        );
    }

    //===========================
    //  Filter
    //===========================
    @Test
    public void TestFindsLowerCasedSubstring() {
        // search for sausage
        List<Email> res = filter.filter(input, inAnyTextFieldPredicate, "sausage");
        // get the content of output in an array
        Email[] actual = res.toArray(new Email[0]);
        //specify what we expect: first and second elements.
        Email[] expected = new Email[]{input.get(0), input.get(1)};
        // check that the two arrays contain the same emails
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void TestNoSuchSubstring() {
        // search for a non-existing word
        List<Email> res = filter.filter(input, inAnyTextFieldPredicate,"object-oriented");
        // check that output is empty
        Assertions.assertEquals(0, res.size());
    }

    @Test
    public void TestFindsSubstringInSubject() {
        // search for substring in first element of emails list
        List<Email> res = filter.filter(input, inAnyTextFieldPredicate,"up");
        // convert output to array
        Email[] actual = res.toArray(new Email[0]);
        // expect output to contain first element of input
        Email[] expected = new Email[]{input.get(0)};
        // check that output is as expected
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFindsSubstringInAddress() {
        List<Email> input = Arrays.asList(
                new Email("lol@gmail.com", to, "Upper", "contains SAUSAGE", null, null),
                new Email("lol@gmail.com", to, "Lower", "contains sausage", null, null),
                new Email("lolyMcSwag@gmail.com", to, "Nothing", "contains no meat at all", null, null)
        );
        // search emails for part of last elements address
        List<Email> res = filter.filter(input, inAnyTextFieldPredicate,"yMcSwag");
        // convert output to array
        Email[] actual = res.toArray(new Email[0]);
        // expect the last element of input in output
        Email[] expected = new Email[]{input.get(2)};
        // check that output contains the last element
        Assertions.assertArrayEquals(expected, actual);
    }


    @Test
    public void testFindsUpperCasedSubstring() {
        List<Email> input = Arrays.asList(
                new Email("lol@gmail.com", new String[]{"TEST_nr_1@gmail.com"}, "Upper", "contains SAUSAGE", null, null),
                new Email("lol@gmail.com", to, "Lower", "contains sausage", null, null),
                new Email("lol@gmail.com", new String[]{"TEST_nr_1@gmail.com"}, "Nothing", "contains no meat at all", null, null)
        );
        // search emails for part of second element's to, but upper-cased.
        List<Email> res = filter.filter(input, inAnyTextFieldPredicate,"NR_2");
        // convert output to array
        Email[] actual = res.toArray(new Email[0]);
        // expect the second element of input in output
        Email[] expected = new Email[]{input.get(1)};
        // check that output contains the last element
        Assertions.assertArrayEquals(expected, actual);
    }

    //===========================
    //  TextFinder
    //===========================
    @Test
    public void TestFindsBothUpperAndLowerCased() {
        // get first and second emails of input
        List<Email> res = new TextFinder().search(input, "sausage");
        Email[] expected = new Email[]{
                input.get(0),
                input.get(1)
        };
        Email[] actual = res.toArray(new Email[0]);
        Assertions.assertArrayEquals(expected, actual);
    }
}
