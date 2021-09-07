package org.group77.mejl.model;

import java.io.*;

public class SystemManager {
    protected String getAccountDir(){
        return getDataDir() + "esp.d\\";
    }
    protected String getDataDir(){
        return "C:\\Users\\Martin\\AppData\\Local\\grupp77\\mejl\\";
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
