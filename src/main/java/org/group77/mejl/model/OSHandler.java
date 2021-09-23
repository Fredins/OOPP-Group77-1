    package org.group77.mejl.model;

    public class OSHandler {
    
    public String getAppPath(){
        return null;
    }
    
    public String getSeparator(){
        return null;
    }

        /**
         * Get the right app directory and separator for the user's OS.
         * @return Array of form: [App Directory, Separator]
         * @throws OSNotFoundException if OS is not MacOS, Windows or Linux.
         */
    private String[] getAppDirAndSeparator() throws OSNotFoundException {
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
