package org.group77.mailMe.model;

public class CouldNotConnectToServerException extends Exception{
    public CouldNotConnectToServerException() {
        super("Could not connect to server.");
    }
}
