package org.group77.mailMe.oldModel;

/**
 * This exception class is to signal that the user's os could not be found or is incompatible with the application.
 */
public class InvalidOperatingSystemException extends Exception {
    /**
     * Constructor.
     * @param msg - the error message of this exception.
     */
    public InvalidOperatingSystemException(String msg) {
        super(msg);
    }
}
