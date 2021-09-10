package org.group77.mejl.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class SystemManager {
    // path to the root directory of this application's files.
    // Will depend on OS.
    private String appDir;
    String separator;

    /** //TODO make getDataDir() into separate method to make it testable?
     *      // Then have setAppDir as a separate method?
     * Method to determine both the data directory of the user's os and the root directory of this
     * application's files.
     * App root will be <data directory of user's OS>/Group77. //TODO change from Group77 to appName?
     * @throws InvalidOperatingSystemException - custom exception stating that os could not be found or is incompatible.
     */
    public SystemManager(){
        createAppPath();
        createAccountHandlerPaths();
    }

    protected void setAppDir() throws InvalidOperatingSystemException {
        String dataDir;
        // Get the operating system of the machine.
        String os = System.getProperty("os.name").toLowerCase();
        // Get the username on the user's machine.
        String userName = System.getProperty("user.name");
        // Set dataDir according to os and username.
        // If os does not match mac/osx, windows or linux then throw exception.
        if (os.contains("mac")) {
            dataDir = "/Users/" + userName + "/Library/Application Support/";
            separator = "/";
        } else if (os.contains("win")) {
            dataDir = "C:\\Users\\" + userName + "\\AppData\\Local\\";
            separator = "\\";
        } else if (os.contains("nux")) {
            dataDir = "/home/" + userName + "/.local/share/";
            separator = "/";
        } else {
            throw new InvalidOperatingSystemException("Your operating system is either not supported or not found.");
        }
        // TODO change setting of appDir to a separate method??
        appDir = dataDir + getAppName() + separator; //TODO change Group77 to app name?
        System.out.println("appdir: " + appDir);
    }

    protected String getAccountDir(){
        return appDir + "AccountInformation.d" + separator;
    }
    protected String getActiveAccountPath(){
        return appDir + "active_account";
    }

    protected String getAppDir() { // är detta okej hampus?
        if(appDir == null){
            try{
                setAppDir();
            }catch(InvalidOperatingSystemException e){
                e.printStackTrace();
            }
        }
        return appDir;
    }

    private void createAccountHandlerPaths() {
        try{
            if(!Files.exists(Path.of(getActiveAccountPath()))){
                touch(getActiveAccountPath());
            }
            if(!Files.exists(Path.of(getAccountDir()))){
                mkdir(getAccountDir());
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void createAppPath(){
        if(appDir == null){
            try{
                setAppDir();
            }catch(InvalidOperatingSystemException e){
                e.printStackTrace();
            }
        }
        try{
            if(!Files.exists(Path.of(getAppDir()))){
               mkdir(getAppDir());
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    protected String getAppName(){
        return "Group77"; // TODO decide on app name
    }

    protected void touch(String path) throws IOException {
        File file = new File(path);
        file.createNewFile();
    }

    protected void mkdir(String path) throws IOException {
        File file = new File(path);
        file.mkdir();
    }

    protected <T> void serialize(T o, String path) throws IOException {
        FileOutputStream file = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(o);
        out.close();
        file.close();
    }


    protected <T> T deserialize(String path) throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream(path);
        ObjectInputStream in = new ObjectInputStream(file);
        T o = (T) in.readObject();
        in.close();
        file.close();
        return o;
    }

}
