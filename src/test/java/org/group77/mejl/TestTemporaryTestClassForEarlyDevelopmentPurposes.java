package org.group77.mejl;

import org.group77.mejl.model.Account;
import org.group77.mejl.model.EmailApp;
import org.group77.mejl.model.Tree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTemporaryTestClassForEarlyDevelopmentPurposes {

    @Test
    void testAddEmail(){
        Account info = new Account(
                "gmail",
                "imap.gmail.com",
                993,
                "imaps",
                "77grupp@gmail.com",
                "grupp77group"
        );
        Account wrongInfo = new Account(
                "gmail",
                "imap.gmail.com",
                993,
                "imaps",
                "77grupp@gmail.com",
                "nopassword"
        );
        EmailApp emailApp = new EmailApp();
        String res = emailApp.addEmail(info.getIdentifier(),
                info.getHost(),
                info.getPort(),
                info.getProtocol(),
                info.getUser(),
                info.getPassword()
        );
        String res1 = emailApp.addEmail(
                wrongInfo.getIdentifier(),
                wrongInfo.getHost(),
                wrongInfo.getPort(),
                wrongInfo.getProtocol(),
                wrongInfo.getUser(),
                wrongInfo.getPassword()
        );
        Assertions.assertTrue(res.equals("account successfully added"));
        Assertions.assertFalse(res.equals(res1));
    }

    @Test
    void testTree(){
        Tree<String> root = new Tree<>("im root");
        Tree<String> notLeaf = new Tree<>("not leaf");
        Tree<String> notLeaf1 = new Tree<>("not leaf1");
        Tree<String> leaf = new Tree<>("leaf");
        Tree<String> leaf1 = new Tree<>("leaf1");
        Tree<String> leaf2 = new Tree<>("leaf2");

        notLeaf.add(leaf);
        notLeaf.add(leaf1);
        notLeaf1.add(leaf2);
        root.add(notLeaf);
        root.add(notLeaf1);
        /*
         * im root
         *      not leaf
         *              leaf
         *              leaf1
         *      not leaf1
         *              leaf2
         */

        Assertions.assertEquals(root.getChildren().get(0), notLeaf);
        Assertions.assertEquals(root.getChildren().get(1), notLeaf1);
        Assertions.assertEquals(notLeaf1.getChildren().get(0), leaf2);
        Assertions.assertEquals(root, leaf1.getRoot());
    }

}
