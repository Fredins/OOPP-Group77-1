package org.group77.mejl.model;

import java.io.*;

public class SystemManager {
    // path to the root directory of this application's files.
    // Will depend on OS.
    private String appDir;
    // user's os' data directory
    private String dataDir;
    // application name
    private String appName = "Group77";     //TODO change Group77 to app name?
    // root directories of this application's files depending on OS.
    private final String linuxRoot = "/home/<user>/.local/share/";
    private final String macRoot = "/Users/<user>/Library/Application Support/";
    private final String windowsRoot = "C:\\Users\\<user>\\AppData\\Local\\"; //TODO ok to remove <groupname> and only have <appname>? (MARTIN)

    //path to directory with account information
    private String accountDir;

    /**
     * Method to determine both the data directory of the user's os and the root directory of this
     * application's files.
     * App root will be <data directory of user's OS>/Group77. //TODO change from Group77 to appName?
     * @throws InvalidOperatingSystemException - custom exception stating that os could not be found or is incompatible.
     */
    protected void setDataDir() throws InvalidOperatingSystemException {
        // Get the operating system of the machine.
        // If os does not match OSX, windows or linux then throw exception.
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            dataDir = macRoot;
        } else if (os.contains("win")) {
            dataDir = windowsRoot;
        } else if (os.contains("nux")) {
            dataDir = linuxRoot;
        } else {
            throw new InvalidOperatingSystemException("Your operating system is either not supported or not found.");
        }
        appDir = dataDir + appName;
    }

    private void setAccountDir() {
        accountDir = appDir + "AccountInformation.d";
    }

    protected String getAccountDir(){
        return accountDir;
    }

    protected String getDatadir() {
        return dataDir;
    }

    protected void createFile(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
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
