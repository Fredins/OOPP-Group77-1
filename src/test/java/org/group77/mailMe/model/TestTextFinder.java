package org.group77.mailMe.model;

import org.group77.mailMe.model.data.*;
import org.group77.mailMe.oldmodel.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for the TextFinder class.
 * @author Hampus Jernkrook
 */
public class TestTextFinder {
    @Test
    public void testFindsBothUpperAndLowerCase() {
        //setup
        String[]to = new String[]{"TEST_nr_1@gmail.com",
          "TEST_nr_2@gmail.com",
          "TEST_nr_3@gmail.com"
        };
        List<Email> input = Arrays.asList(
                new Email("lol@gmail.com", to, "Upper", "contains SAUSAGE"),
                new Email("lol@gmail.com", to, "Lower", "contains sausage"),
                new Email("lol@gmail.com", to, "Nothing", "contains no meat at all")
        );
        TextFinder searcher = new TextFinder();
        // convert input to array
        Email[] inputArray = input.toArray(new Email[0]);
        //specify what we expect: first and second elements.
        Email[] expected = new Email[]{inputArray[0], inputArray[1]};
        // search for sausage
        List<Email> res = searcher.search(input, "sausage");
        // get the content of output in an array
        Email[] actual = res.toArray(new Email[0]);
        // check that the two arrays contain the same emails
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void testNoSuchSubstring() {
        // setup
        String[] to = new String[]{"TEST_nr_1@gmail.com",
                "TEST_nr_2@gmail.com",
                "TEST_nr_3@gmail.com"
        };
        List<Email> input = Arrays.asList(
                new Email("lol@gmail.com", to, "Upper", "contains SAUSAGE"),
                new Email("lol@gmail.com", to, "Lower", "contains sausage"),
                new Email("lol@gmail.com", to, "Nothing", "contains no meat at all")
        );
        TextFinder searcher = new TextFinder();
        // search for a non-existing word
        List<Email> res = searcher.search(input, "object-oriented");
        // check that output is empty
        Assertions.assertEquals(0, res.size());
    }

    @Test
    public void testFindsSubstringInSubject() {
        // setup
        String[] to = new String[]{
            "TEST_nr_1@gmail.com",
              "TEST_nr_2@gmail.com",
              "TEST_nr_3@gmail.com"
        };
        List<Email> input = Arrays.asList(
                new Email("lol@gmail.com", to, "Upper", "contains SAUSAGE"),
                new Email("lol@gmail.com", to, "Lower", "contains sausage"),
                new Email("lol@gmail.com", to, "Nothing", "contains no meat at all")
        );
        TextFinder searcher = new TextFinder();
        // search for substring in first element
        List<Email> res = searcher.search(input, "up");
        // convert output to array
        Email[] actual = res.toArray(new Email[0]);
        // convert input to array
        Email[] inputArray = input.toArray(new Email[0]);
        // expect output to contain first element of input
        Email[] expected = new Email[]{inputArray[0]};
        // check that output is as expected
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFindsSubstringInAddress() {
        // set up
        String[] to = new String[]{"TEST_nr_1@gmail.com",
                "TEST_nr_2@gmail.com",
                "TEST_nr_3@gmail.com"
        };
        List<Email> input = Arrays.asList(
                new Email("lol@gmail.com", to, "Upper", "contains SAUSAGE"),
                new Email("lol@gmail.com", to, "Lower", "contains sausage"),
                new Email("lolyMcSwag@gmail.com", to, "Nothing", "contains no meat at all")
        );
        TextFinder searcher = new TextFinder();
        // search emails for part of last elements address
        List<Email> res = searcher.search(input, "yMcSwag");
        // convert output to array
        Email[] actual = res.toArray(new Email[0]);
        // convert input to array
        Email[] inputArray = input.toArray(new Email[0]);
        // expect the last element of input in output
        Email[] expected = new Email[]{inputArray[2]};
        // check that output contains the last element
        Assertions.assertArrayEquals(expected, actual);
    }


    @Test
    public void testFindsUpperCasedSubstring() {
        // set up
        String[] to = new String[]{"TEST_nr_1@gmail.com",
                "TEST_nr_2@gmail.com",
                "TEST_nr_3@gmail.com"
        };
        List<Email> input = Arrays.asList(
                new Email("lol@gmail.com", new String[]{"TEST_nr_1@gmail.com"}, "Upper", "contains SAUSAGE"),
                new Email("lol@gmail.com", to, "Lower", "contains sausage"),
                new Email("lol@gmail.com", new String[]{"TEST_nr_1@gmail.com"}, "Nothing", "contains no meat at all")
        );
        TextFinder searcher = new TextFinder();
        // search emails for part of second element's to, but upper-cased.
        List<Email> res = searcher.search(input, "NR_2");
        // convert output to array
        Email[] actual = res.toArray(new Email[0]);
        // convert input to array
        Email[] inputArray = input.toArray(new Email[0]);
        // expect the second element of input in output
        Email[] expected = new Email[]{inputArray[1]};
        // check that output contains the last element
        Assertions.assertArrayEquals(expected, actual);
    }
}
