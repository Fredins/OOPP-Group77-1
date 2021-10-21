package org.group77.mailMe.services.emailServiceProvider;

/**
 * Exception to wrap exceptions arising from methods connecting against the ESP server
 * or to simply signal that connection with server was refused.
 * @author Martin
 */
public class ServerException extends Exception {
  public ServerException() {
    super("Could not connect to server.");
  }

  /**
   * Wraps the given exception into a ServerException and uses the wrapped
   * exception's message.
   * @param e - the exception to wrap.
   */
  public ServerException(Exception e) {
    super(e);
  }
}
