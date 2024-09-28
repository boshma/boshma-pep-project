package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;
    private ObjectMapper objectMapper;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
        this.objectMapper = new ObjectMapper();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::handleRegister);
        app.post("/login", this::handleLogin);
        app.post("/messages", this::handleCreateMessage);
        app.get("/messages", this::handleGetAllMessages);
        app.get("/messages/{message_id}", this::handleGetMessageById);
        app.delete("/messages/{message_id}", this::handleDeleteMessageById);
        app.patch("/messages/{message_id}", this::handleUpdateMessageById);
        app.get("/accounts/{account_id}/messages", this::handleGetMessagesByAccountId);

        return app;
    }

    // Handler for the /register endpoint
    private void handleRegister(Context ctx) throws JsonProcessingException {
        Account accountRequest = objectMapper.readValue(ctx.body(), Account.class);
        Account createdAccount = accountService.registerAccount(accountRequest);

        if (createdAccount != null) {
            ctx.json(objectMapper.writeValueAsString(createdAccount));
        } else {
            ctx.status(400);
        }
    }

    // Handler for the /login endpoint
    private void handleLogin(Context ctx) throws JsonProcessingException {
        Account loginRequest = objectMapper.readValue(ctx.body(), Account.class);
        Account account = accountService.login(loginRequest.getUsername(), loginRequest.getPassword());

        if (account != null) {
            ctx.json(objectMapper.writeValueAsString(account));
        } else {
            ctx.status(401);
        }
    }

    // Handler to create a new message
    private void handleCreateMessage(Context ctx) throws JsonProcessingException {
        Message messageRequest = objectMapper.readValue(ctx.body(), Message.class);
        Message createdMessage = messageService.createMessage(messageRequest);

        if (createdMessage != null) {
            ctx.json(objectMapper.writeValueAsString(createdMessage));
        } else {
            ctx.status(400);
        }
    }

    // Handler to retrieve all messages
    private void handleGetAllMessages(Context ctx) throws JsonProcessingException {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(objectMapper.writeValueAsString(messages));
    }

    // Handler to retrieve a message by ID
    private void handleGetMessageById(Context ctx) throws JsonProcessingException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);

        if (message != null) {
            ctx.json(objectMapper.writeValueAsString(message));
        } else {
            ctx.status(200).json("");
        }
    }

    // Handler to delete a message by ID
    private void handleDeleteMessageById(Context ctx) throws JsonProcessingException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageById(messageId);

        if (deletedMessage != null) {
            ctx.json(objectMapper.writeValueAsString(deletedMessage));
        } else {
            ctx.status(200).json("");
        }
    }

    // Handler to update a message text by ID
    private void handleUpdateMessageById(Context ctx) throws JsonProcessingException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        String newMessageText = objectMapper.readValue(ctx.body(), Message.class).getMessage_text();
        Message updatedMessage = messageService.updateMessageText(messageId, newMessageText);

        if (updatedMessage != null) {
            ctx.json(objectMapper.writeValueAsString(updatedMessage));
        } else {
            ctx.status(400);
        }
    }

    // Handler to retrieve all messages by account_id
    private void handleGetMessagesByAccountId(Context ctx) throws JsonProcessingException {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        ctx.json(objectMapper.writeValueAsString(messages));
    }
}