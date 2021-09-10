package org.group77.mejl.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class AccountHandler {
    private final SystemManager systemManager = new SystemManager();

    /**
     * sets the active ESP by writing to the file active_esp
     * @param identifier target
     * @implNote not using symlink because Windows require elevated permission
     */
    protected void setAcitiveAccount(String identifier){
        String path = getSystemManager().getAppDir() + "active_account"; //TODO change SystemManager to have a getActiveAccountDir() instead? We use this multiple times.
        createAccountHandlerPaths();
        try{
            File file = new File(path);
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

    private void createAccountHandlerPaths() {
        SystemManager sys = getSystemManager();
        String active_account = sys.getAppDir() + "active_account";
        String accountInformation_d = sys.getAppDir() + "accountinformation.d";
        try{
            if(!Files.exists(Path.of(active_account))){
                sys.touch(active_account);
            }
            if(!Files.exists(Path.of(accountInformation_d))){
                sys.mkdir(accountInformation_d);
            }
        }catch(IOException e){
           e.printStackTrace();
        }
    }

    /**
     * reads the active ESP which is indicated in the file active_esp
     * @return the in use ESP
     */
    protected Account getActiveAccount(){
        SystemManager sys = getSystemManager();
        createAccountHandlerPaths();
        try{
            String s = getSystemManager().getAppDir() + "active_account";
            File file = new File(s);
            Scanner scanner = new Scanner(file);
            String active_account = scanner.nextLine();
            return sys.readFrom(sys.getAccountDir() + active_account);
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return a new deserialized ESP object
     */
    protected Account readAccount(String identifier, String protocol) {
        SystemManager sys = getSystemManager();
        createAccountHandlerPaths();
        try {
            String path = sys.getAccountDir() + identifier + "-" + protocol;
            return sys.readFrom(path);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Stores esp information serialized in data directory
     *
     * @param info an object with required data for connecting to remote ESP
     */
    protected IOException writeAccount(Account info) {
        SystemManager sys = getSystemManager();
        createAccountHandlerPaths();
        try {
            String path = sys.getAccountDir() + info.getIdentifier() + "-" + info.getProtocol();
            getSystemManager().touch(path);
            getSystemManager().writeTo(info, path);
        } catch (IOException e) {
            return e;
        }
        return  null;
    }

    private SystemManager getSystemManager() {
        return systemManager;
    }
}
