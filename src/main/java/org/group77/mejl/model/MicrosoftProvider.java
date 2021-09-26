package org.group77.mejl.model;
import javax.mail.Store;
import java.util.*;

public class MicrosoftProvider extends EmailServiceProviderStrategy {

    public MicrosoftProvider() {
        super("sluta", "störa", "mig", "tack", 1, 2);
    }

    @Override
    protected List<Folder> parse(Store store) {
        return null;
    }
}
