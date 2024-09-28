package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {

    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    // Method to create a new message
    public Message createMessage(Message message) {
        // Validate that the message text is not blank
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()) {
            return null;
        }

        // Validate that the message text is not over 255 characters
        if (message.getMessage_text().length() > 255) {
            return null;
        }

        // Validate that the posted_by refers to a real, existing user
        if (!accountDAO.doesUsernameExistById(message.getPosted_by())) {
            return null;
        }

        // If all validations pass, insert the message into the database
        return messageDAO.insertMessage(message);
    }

    // Method to retrieve all messages
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    // Method to retrieve a message by its ID
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    // Method to delete a message by its ID
    public Message deleteMessageById(int messageId) {
        return messageDAO.deleteMessageById(messageId);
    }

    // Method to update a message text by its ID
    public Message updateMessageText(int messageId, String newMessageText) {
        // Validate that the new message text is not blank or null
        if (newMessageText == null || newMessageText.trim().isEmpty()) {
            return null;
        }

        // Validate that the new message text is not over 255 characters
        if (newMessageText.length() > 255) {
            return null;
        }

        // Call the DAO method to update the message
        return messageDAO.updateMessageText(messageId, newMessageText);
    }

    // Method to retrieve all messages by account_id
    public List<Message> getMessagesByAccountId(int accountId) {
        return messageDAO.getMessagesByAccountId(accountId);
    }
}