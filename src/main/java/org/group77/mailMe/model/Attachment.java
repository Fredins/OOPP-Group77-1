package org.group77.mailMe.model;

import java.io.*;

/**
 * Representation of an email attachment.
 * @author Martin Fredin
 */
public record Attachment(
        String name, // name of the attachment
        byte[] content, //byte representation of the content of the attachment
        File file // the abstract path representing this attachment's location on the machine
) implements Serializable {
}
