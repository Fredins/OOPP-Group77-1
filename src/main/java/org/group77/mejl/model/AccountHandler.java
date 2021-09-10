package org.group77.mejl.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class AccountHandler {
    private final SystemManager sys = new SystemManager();

    /**
     * @return a new deserialized ESP object
     */
    protected Account readAccount(String identifier, String protocol) {
        try {
            String path = sys.getAccountDir() + identifier + "-" + protocol;
            return sys.deserialize(path);
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
        try {
            String path = sys.getAccountDir() + info.getIdentifier() + "-" + info.getProtocol();
            sys.touch(path);
            sys.serialize(info, path);
        } catch (IOException e) {
            return e;
        }
        return  null;
    }

    /**
     * sets the active ESP by writing to the file active_esp
     * @param identifier target
     * @implNote not using symlink because Windows require elevated permission
     */
    protected void setAcitiveAccount(String identifier){
        try{
            File file = new File(sys.getActiveAccountPath());
            FileWriter writer = new FileWriter(sys.getActiveAccountPath());
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
    protected Account getActiveAccount(){
        try{
            File file = new File(sys.getActiveAccountPath());
            Scanner scanner = new Scanner(file);
            String active_account = scanner.nextLine();
            return sys.deserialize(sys.getActiveAccountPath());
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }


}
