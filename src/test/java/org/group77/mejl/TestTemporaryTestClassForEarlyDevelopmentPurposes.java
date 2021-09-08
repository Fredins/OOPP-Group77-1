package org.group77.mejl;

import org.group77.mejl.model.AccountInformation;
import org.group77.mejl.model.EmailApp;
import org.group77.mejl.model.TreeNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

public class TestTemporaryTestClassForEarlyDevelopmentPurposes {

    @Test
    void testAddEmail(){
        AccountInformation info = new AccountInformation(
                "gmail",
                "imap.gmail.com",
                993,
                "imaps",
                "77grupp@gmail.com",
                "grupp77group"
        );
        AccountInformation wrongInfo = new AccountInformation(
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
        TreeNode<String> root = new TreeNode<>("im root");
        TreeNode<String> notLeaf = new TreeNode<>("not leaf");
        TreeNode<String> notLeaf1 = new TreeNode<>("not leaf1");
        TreeNode<String> leaf = new TreeNode<>("leaf");
        TreeNode<String> leaf1 = new TreeNode<>("leaf1");
        TreeNode<String> leaf2 = new TreeNode<>("leaf2");

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
