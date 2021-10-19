package org.group77.mailMe.model.exceptions;

/**
 * Exception to be used if the user's OS cannot be found/identified.
 *
 * @author Hampus Jernkrook
 */
public class OSNotFoundException extends Exception {

    /**
     * Constructor
     *
     * @param msg - the error message of this exception.
     */
    public OSNotFoundException(String msg) {
        super(msg);
    }
}
