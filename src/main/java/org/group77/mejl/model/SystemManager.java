package org.group77.mejl.model;

import java.io.*;

public class SystemManager {
    // path to the root directory of this application's files.
    // Will depend on OS.
    private String appDir;

    /** //TODO make getDataDir() into separate method to make it testable?
     *      // Then have setAppDir as a separate method?
     * Method to determine both the data directory of the user's os and the root directory of this
     * application's files.
     * App root will be <data directory of user's OS>/Group77. //TODO change from Group77 to appName?
     * @throws InvalidOperatingSystemException - custom exception stating that os could not be found or is incompatible.
     */
    protected void setDataDir() throws InvalidOperatingSystemException {
        String dataDir;
        // Get the operating system of the machine.
        String os = System.getProperty("os.name").toLowerCase();
        // Get the username on the user's machine.
        String userName = System.getProperty("user.name");
        // Set dataDir according to os and username.
        // If os does not match mac/osx, windows or linux then throw exception.
        if (os.contains("mac")) {
            dataDir = "/Users/" + userName + "/Library/Application Support/";
        } else if (os.contains("win")) {
            dataDir = "C:\\Users\\" + userName + "\\AppData\\Local\\";
        } else if (os.contains("nux")) {
            dataDir = "/home/" + userName + "/.local/share/";
        } else {
            throw new InvalidOperatingSystemException("Your operating system is either not supported or not found.");
        }
        // TODO change setting of appDir to a separate method??
        appDir = dataDir + "Group77"; //TODO change Group77 to app name?
    }

    protected String getAccountDir(){
        return appDir + "AccountInformation.d";
    }

    protected String getAppDir() {
        return appDir;
    }

    protected String getFolderDir() {
        return appDir + "Folders";
    }

    protected void touch(String path) throws IOException {
        File file = new File(path);
        file.createNewFile();
    }

    protected void mkdir(String path) throws IOException {
        File file = new File(path);
        file.mkdir();
    }

    protected <T> void writeTo(T o, String path) throws IOException {
        FileOutputStream file = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(o);
        out.close();
        file.close();
    }


    protected <T> T readFrom(String path) throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream(path);
        ObjectInputStream in = new ObjectInputStream(file);
        T o = (T) in.readObject();
        in.close();
        file.close();
        return o;
    }

}
