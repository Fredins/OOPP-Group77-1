package org.group77.mailMe.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

/**
 * @author Elin Hagman
 */
public class TestEmail {

    private static Email email;
    private static Email emailDefaultDate;
    private static Email emailSeveralRecipients;

    @BeforeEach
    public void setup() {
        email = new Email(
                "sender@gmail.com",
                new String[]{"recipient@gmail.com"},
                "subject",
                "content",
                null,
                LocalDateTime.of(2021, Month.OCTOBER,20,13,49));

        emailDefaultDate = new Email(
                "sender@gmail.com",
                new String[]{"recipient@gmail.com"},
                "subject 2",
                "content 2",
                null);

        emailSeveralRecipients = new Email(
                "sender@gmail.com",
                new String[]{"recipient1@gmail.com","recipient2@gmail.com"},
                "subject",
                "content",
                null,
                LocalDateTime.of(2021, Month.OCTOBER,20,13,49));
    }
    @Test
    public void testFrom() {
        Assertions.assertEquals("sender@gmail.com",email.from());
    }

    @Test
    public void testToOneRecipient() {
        Assertions.assertEquals("[recipient@gmail.com]", Arrays.toString(email.to()));
    }

    @Test
    public void testToSeveralRecipientsSize() {
        Assertions.assertEquals(2, emailSeveralRecipients.to().length);
    }

    @Test
    public void testToSeveralRecipients() {
        Assertions.assertEquals("[recipient1@gmail.com, recipient2@gmail.com]",Arrays.toString(emailSeveralRecipients.to()));
    }

    @Test
    public void testDate() {
        LocalDateTime expectedDate = LocalDateTime.of(2021, Month.OCTOBER,20,13,49);
        Assertions.assertEquals(expectedDate,email.date());
    }

    @Test
    public void testDefaultDate() {
        LocalDateTime now = LocalDateTime.now();

        // check that the default date is set to current time (does not test minute and seconds)
        Assertions.assertTrue(
                now.getYear() == emailDefaultDate.date().getYear()
                && now.getMonthValue() == emailDefaultDate.date().getMonthValue()
                && now.getDayOfMonth() == emailDefaultDate.date().getDayOfMonth()
                && now.getHour() == emailDefaultDate.date().getHour());
    }

    @Test
    public void testEquals() {
        Email copyEmail = new Email(
                "sender@gmail.com",
                new String[]{"recipient@gmail.com"},
                "subject",
                "content",
                null,
                LocalDateTime.of(2021, Month.OCTOBER,20,13,49)
                );

        Assertions.assertEquals(email, copyEmail);
    }
}
