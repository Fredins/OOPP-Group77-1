package org.group77.mailMe.model;

import java.io.Serializable;
import java.util.*;

/**
 * @author Hampus Jernkrook
 */
public class Email implements Serializable {
    private final String from;
    private final List<String> to;
    private final String subject;
    private final String content;
    
    public Email(String from, List<String> to, String subject, String content) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }
    
    public String getFrom() {
        return from;
    }

    public List<String> getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }
}
