package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    // Method to register a new account
    public Account registerAccount(Account account) {
        // Check if username is not blank
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            return null;
        }

        // Check if password is at least 4 characters long
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return null;
        }

        // Check if the username already exists
        if (accountDAO.doesUsernameExist(account.getUsername())) {
            return null;
        }

        // If all validations pass, insert the account into the database
        return accountDAO.insertAccount(account);
    }

    // Method to handle user login
    public Account login(String username, String password) {
        // Validate that username and password are not null or blank
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }

        // Attempt to retrieve the account from the database
        return accountDAO.getAccountByUsernameAndPassword(username, password);
    }
    
}