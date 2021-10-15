package org.group77.mailMe.model.exceptions;

public class CouldNotConnectToServerException extends Exception{
    public CouldNotConnectToServerException() {
        super("Could not connect to server.");
    }
}
