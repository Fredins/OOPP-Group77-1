package org.group77.mejl.model;
import javax.mail.Store;
import java.util.*;

public class MicrosoftProvider extends EmailServiceProviderStrategy {

    @Override
    protected List<Folder> parse(Store store) {
        return null;
    }
}
