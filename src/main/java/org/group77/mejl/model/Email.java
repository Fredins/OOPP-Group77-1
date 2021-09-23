package org.group77.mejl.model;

import java.util.*;

/**
 * @author Hampus Jernkrook
 */
public class Email {
    private String from;
    private List<String> to;
    private String subject;
    private String content;
    
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
