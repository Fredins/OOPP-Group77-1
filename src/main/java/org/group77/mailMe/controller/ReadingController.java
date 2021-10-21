package org.group77.mailMe.controller;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.web.*;
import javafx.stage.*;
import org.group77.mailMe.*;
import org.group77.mailMe.controller.utils.*;
import org.group77.mailMe.model.Attachment;
import org.group77.mailMe.Control;
import org.group77.mailMe.model.Email;
import org.group77.mailMe.model.Folder;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import static java.util.regex.Pattern.compile;


public class ReadingController {
    @FXML
    private Label fromLabel;
    @FXML
    private Label subjectLabel;
    @FXML
    private Label toLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Button replyBtn;
    @FXML
    private Button trashBtn;
    @FXML
    private Button archiveBtn;
    @FXML
    private WebView webView;
    @FXML
    private ImageView archiveImg;
    @FXML
    private ImageView replyImg;
    @FXML
    private HBox attachmentsHBox;

    /**
     * 1. set initial values for nodes
     * 2. add event handlers to nodes
     *
     * @param control the control layer
     * @param email   the corresponding email
     * @author Martin Fredin, David Zamanian
     */
    void init(Control control, Email email) {
        fromLabel.setText(email.from());
        subjectLabel.setText(email.subject());
        toLabel.setText(control.removeBrackets(Arrays.toString(email.to())));
        dateLabel.setText(email.date().toString().replace("T", "  "));
        archiveImg.setImage(getImage(control.getActiveFolder().get(), archiveImg));
        replyImg.setImage(getImage(control.getActiveFolder().get(), replyImg));
        attachmentsController(email);

        // webview
        String content = email.content();
        WebEngine webEngine = webView.getEngine();
        if(email.content().contains("contenteditable=\"true\"")){
            content =email.content().replace("contenteditable=\"true\"", "contenteditable=\"false\"");
        }
        webEngine.loadContent(content, "text/html");

        // attach event handlers
        trashBtn.setOnAction(i -> handleDelete(control));
        replyBtn.setOnAction(getButtonHandler(control, email, replyBtn));
        archiveBtn.setOnAction(getButtonHandler(control, email, archiveBtn));
    }

    /**
     * open email writing window and fill in to recipient field if possible
     * @param control the control layer
     * @author Martin Fredin
     */
    private void reply(Control control){
        try{
            WindowOpener.openReply(control, extractEmailAddress(fromLabel.getText()));
        }catch (Exception ignore){
            WindowOpener.openWriting(control);
        }
    }

    /**
     * extract email address from string
     * @param str a string that possibly contains an email address
     * @return the first valid email address if exists
     * @throws Exception if there are no valid email address
     * @author Martin Fredin
     */
    private String extractEmailAddress(String str) throws Exception{
        // official email validator standard RFC 5322
        String rfc5322 = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        Pattern pattern = compile(rfc5322);
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()){
            return matcher.group(0);
        }else{
            throw new Exception("reply: couldn't extract email address");
        }
    }


    /**
     *  decides on what image depending on folder and image view
     *
     * @param folder active folder
     * @param imageView corresponding image view
     * @author Martin Fredin
     *
     */
    private Image getImage(Folder folder, ImageView imageView){
       if(imageView.equals(archiveImg)){
          return new Image(
              String.valueOf((Main.class.getResource(
                (folder.name().equals("Archive") | folder.name().equals("Trash")) ? "images_and_icons/restore.png" : "images_and_icons/archive.png"
              ))));
       }else {
           return new Image(
             String.valueOf((Main.class.getResource(
               folder.name().equals("Drafts") ? "images_and_icons/continue_draft.png" : "images_and_icons/reply.png"
             ))));
       }
    }

    /**
     * finds folder based on folderName and then calls control.moveEmail()
     *
     * @param control    the control layer
     * @param folderName the move-email-to-folder-name
     * @author Martin
     */
    private void moveEmailTo(Control control, String folderName) {
        Optional<Folder> maybeFolder = control
                .getFolders()
                .stream()
                .filter(folder -> folder.name().equals(folderName))
                .findFirst();
        maybeFolder.ifPresent(folder -> {
            try {
                control.moveEmail(folder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * remove all existing even handlers, set a new event handler depending on current folder and button
     *
     * @param button         the corresponding button
     * @param control        the control layer
     * @param email          the corresponding email to controller
     * @author Martin
     */
    private EventHandler<ActionEvent> getButtonHandler(Control control, Email email, Button button) {
        EventHandler<ActionEvent> archiveHandler = actionEvent -> moveEmailTo(control, "Archive");
        EventHandler<ActionEvent> restoreHandler = actionEvent -> moveEmailTo(control, "Inbox");
        EventHandler<ActionEvent> replyHandler = actionEvent -> reply(control);
        EventHandler<ActionEvent> continueDraftHandler = actionEvent ->  WindowOpener.openDraft(control, email);
        Folder folder = control.getActiveFolder().get();

        if (button.equals(archiveBtn)){
            return (folder.name().equals("Archive") | folder.name().equals("Trash")) ? restoreHandler : archiveHandler;
        }else {
            return (folder.name().equals("Drafts")  ? continueDraftHandler : replyHandler);
        }
    }


    /**
     * Moves email to trash if not already in trash. If in trash --> Deletes permanently (but with confirmation).
     *
     * @param control the control layer
     * @author David
     */
    private void handleDelete(Control control) {
        if ((control.getActiveFolder().get().name().equals("Trash"))) {
            try {
                if (customAlert("Are you sure you want to permanently delete this email?", Alert.AlertType.CONFIRMATION).get().equals(ButtonType.OK)) {
                    control.permDeleteEmail();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                control.deleteEmail();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Displays a confirmation alert when you try to permanently delete an email.
     *
     * @param message   Type in your message you want user to see in an alert.
     * @param alertType What types of alert you want to display (CONFIRMATION in this case)
     * @author David Zamanian
     */
    private Optional<ButtonType> customAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Delete Email");
        alert.setContentText(message);
        alert.getDialogPane().setPrefSize(300, 200);
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }

    /**
     * @param email - Received email.
     * @author Alexey Ryabov
     */
    private void attachmentsController(Email email) {
        if (!email.attachments().isEmpty()) {
            //For every attachment in the list.
            for (Attachment attachment : email.attachments()) {
                try {
                    //Creating a button with attachment name.
                    Button button = hBoxButtonSetup(attachment);
                    attachmentButtonAction(attachment, button);
                    //Adding a button to HBox.
                    attachmentsHBox.setSpacing(5);
                    attachmentsHBox.getChildren().add(button);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param attachment - list of attachments
     * @return button for the HBox.
     * @author Alexey Ryabov, Martin Fredin
     */
    private Button hBoxButtonSetup(Attachment attachment) {
        //Creating image for attachments button
        ImageView icon = new ImageView(new Image(String.valueOf(Main.class.getResource("images_and_icons/attachment.png"))));
        icon.rotateProperty().setValue(-90);
        icon.setFitHeight(24);
        icon.setFitWidth(24);
        //Creating button for the HBox
        Button button = new Button();
        Tooltip tooltip = new Tooltip(); // Tooltip for the button with attachment's name.
        tooltip.setText(attachment.name());
        button.setTooltip(tooltip);
        button.setGraphic(icon);
        button.setText(attachment.name());
        button.setCursor(Cursor.HAND);
        button.setFont(Font.font("System", FontWeight.BOLD, 13));
        button.setStyle("-fx-background-color: white");
        return button;
    }

    /**
     * @param attachment - attachment.
     * @param button     - button, with info about its attachment.
     * @author - Alexey Ryabov
     */
    public void attachmentButtonAction(Attachment attachment, Button button) {
        button.setOnAction(e -> {
            //On button action a file chooser going to open.
            openFileChooser((Stage) button.getScene().getWindow(), attachment.content(), attachment.name());
        });
    }

    /**
     * @param stage      - stage where save window is going to be displayed.
     * @param fileToSave - array of the attachment.
     * @param fileName   - name of the attachment.
     * @author - Alexey Ryabov
     */
    public void openFileChooser(Stage stage, byte[] fileToSave, String fileName) {
        //Creating file chooser.
        FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All Files", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(fileName);
        //Show save file dialog
        File filePath = fileChooser.showSaveDialog(stage);


        if (filePath == null) {
            //Do nothing if file chooser was cancelled.
            //This is for NullPointerException.
            System.out.println("FileChooser was cancelled.");
        } else {
            if (fileToSave != null) {
                saveFile(fileToSave, filePath);
            }
        }



    }

    /**
     * @param content - byte array to be saved as a file.
     * @param file    - path where file is going to be saved.
     * @author Alexey Ryabov
     */
    private void saveFile(byte[] content, File file) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(content);
            outputStream.close();
            outputStream.flush();
        } catch (IOException | NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}
