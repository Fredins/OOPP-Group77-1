package org.group77.mejl.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class AccountHandler {
    private final SystemManager systemManager = new SystemManager();

    /**
     * sets the active ESP by writing to the file active_esp
     * @param identifier target
     * @implNote not using symlink because Windows require elevated permission
     */
    protected void setAcitiveAccount(String identifier){
        String path = getSystemManager().getDataDir() + "active_account";
        File file = new File(path);
        try{
            if(!file.exists()){
                getSystemManager().createFile(path);
            }
            FileWriter writer = new FileWriter(path);
            if(!file.canWrite()){
                file.setWritable(true);
            }
            writer.write(identifier);
            writer.flush();
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * reads the active ESP which is indicated in the file active_esp
     * @return the in use ESP
     */
    protected AccountInformation getActiveAccount(){
        String s = getSystemManager().getDataDir() + "active_account";
        File file = new File(s);
        try{
            if(!file.exists()){
                getSystemManager().createFile(s);
            }
            Scanner scanner = new Scanner(file);
            String identifier = scanner.nextLine();
            return getSystemManager().readFrom(getSystemManager().getAccountDir() + identifier);
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }

        return null;
    }
    /**
     * @param path to the file which contains ESP data
     * @return a new deserialized ESP object
     */
    protected AccountInformation readAccount(String path) {
        try {
            AccountInformation accountInformation = getSystemManager().readFrom(path);
            return accountInformation;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Stores esp information serialized in data directory
     *
     * @param accountInformation an object with required data for connecting to remote ESP
     */
    protected void writeAccount(AccountInformation accountInformation) {
        try {
            String path = getSystemManager().getAccountDir() + accountInformation.getIdentifier();
            getSystemManager().createFile(path);
            getSystemManager().writeTo(accountInformation, path);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private SystemManager getSystemManager() {
        return systemManager;
    }
}
