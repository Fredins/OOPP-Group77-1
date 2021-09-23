package org.group77.mejl.model;

/**
 * This class sets the application directory and the correct separator
 * depending on the user's OS. It serves only as a helper for other
 * classes who may use this information.
 */
public class OSHandler {

    /**
     * @return Array of form: [App Directory, Separator]
     * @throws OSNotFoundException if OS is not MacOS, Windows or Linux.
     * @author Hampus Jernkrook
     * Get the right app directory and separator for the user's OS.
     */
    public static String[] getAppDirAndSeparator() throws OSNotFoundException {
        // The variables that will be returned:
        String appDir;
        String separator;

        // Get the operating system of the machine.
        String os = System.getProperty("os.name").toLowerCase();
        // Get the username on the user's machine.
        String userName = System.getProperty("user.name");
        // Set appDir according to os and username.
        // If os does not match mac/osx, windows or linux then throw exception.
        if (os.contains("mac")) {
            appDir = "/Users/" + userName + "/Library/Application Support/";
            separator = "/";
        } else if (os.contains("win")) {
            appDir = "C:\\Users\\" + userName + "\\AppData\\Local\\";
            separator = "\\";
        } else if (os.contains("nux")) {
            appDir = "/home/" + userName + "/.local/share/";
            separator = "/";
        } else {
            throw new OSNotFoundException("Your operating system is either not supported or not found.");
        }
        return new String[]{appDir, separator};

    }

}
