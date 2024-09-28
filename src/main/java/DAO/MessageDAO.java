package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    // Method to insert a new message into the database
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);

            // Set the parameters for the SQL query
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    return new Message(generatedId, message.getPosted_by(), message.getMessage_text(),
                            message.getTime_posted_epoch());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Insert failed
    }

    // Method to retrieve all messages from the database
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int messageId = rs.getInt("message_id");
                int postedBy = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                long timePosted = rs.getLong("time_posted_epoch");

                Message message = new Message(messageId, postedBy, messageText, timePosted);
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    // Method to retrieve a message by its ID
    public Message getMessageById(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int postedBy = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                long timePosted = rs.getLong("time_posted_epoch");

                return new Message(messageId, postedBy, messageText, timePosted);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Message not found
    }

    // Method to delete a message by its ID and return the deleted message
    public Message deleteMessageById(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // First, retrieve the message to return it after deletion
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, messageId);

            ResultSet rs = selectStatement.executeQuery();
            if (rs.next()) {
                int postedBy = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                long timePosted = rs.getLong("time_posted_epoch");

                Message message = new Message(messageId, postedBy, messageText, timePosted);

                // Now proceed to delete the message
                String deleteSql = "DELETE FROM message WHERE message_id = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
                deleteStatement.setInt(1, messageId);
                deleteStatement.executeUpdate();

                // Return the deleted message
                return message;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Message not found or deletion failed
    }

    // Method to update a message text by its ID
    public Message updateMessageText(int messageId, String newMessageText) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // First, check if the message exists
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, messageId);

            ResultSet rs = selectStatement.executeQuery();
            if (rs.next()) {
                int postedBy = rs.getInt("posted_by");
                long timePosted = rs.getLong("time_posted_epoch");

                // Now proceed to update the message text
                String updateSql = "UPDATE message SET message_text = ? WHERE message_id = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setString(1, newMessageText);
                updateStatement.setInt(2, messageId);
                updateStatement.executeUpdate();

                // Return the updated message
                return new Message(messageId, postedBy, newMessageText, timePosted);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Message not found or update failed
    }

    // Method to retrieve all messages by account_id
    public List<Message> getMessagesByAccountId(int accountId) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int messageId = rs.getInt("message_id");
                String messageText = rs.getString("message_text");
                long timePosted = rs.getLong("time_posted_epoch");

                Message message = new Message(messageId, accountId, messageText, timePosted);
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
}