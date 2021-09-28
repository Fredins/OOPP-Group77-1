package org.group77.mejl;

import org.group77.mejl.model.Email;
import org.group77.mejl.model.TextFinder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class TestTextFinder {
    @Test
    public void testFindsBothUpperAndLowerCase() {
        List<String> to = Arrays.asList("TEST_nr_1@gmail.com",
                "TEST_nr_2@gmail.com",
                "TEST_nr_3@gmail.com"
        );
        List<Email> input = Arrays.asList(
                new Email("lol@gmail.com", to, "Testing", "contains SAUSAGE"),
                new Email("lol@gmail.com", to, "Testing", "contains sausage"),
                new Email("lol@gmail.com", to, "Testing", "contains no meat at all")
        );
        TextFinder searcher = new TextFinder();
        List<Email> res = searcher.search(input, "sausage");

        Email[] expected = new Email[]{(Email) input.toArray()[0], (Email) input.toArray()[1]};
        // get the content or res in an array
        Email[] actual = new Email[2];
        for (int i = 0; i < res.size(); i++) actual[i] = res.get(i);
        // check that the two arrays contain the same emails
        Assertions.assertArrayEquals(expected, actual);
    }
}
