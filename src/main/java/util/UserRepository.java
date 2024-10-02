package util;

import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {

    private static UserRepository instance;
    private List<User> userList;

    // Private constructor to enforce Singleton pattern
    private UserRepository() {
        userList = new ArrayList<>();
        // Add a default admin user for testing (optional)
        userList.add(new User("admin", "admin123", "Admin", "User"));
    }

    // Get the singleton instance of UserRepository
    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    // Add a new user to the repository
    public void addUser(User user) {
        userList.add(user);
    }

    // Check if a username already exists
    public boolean usernameExists(String username) {
        return userList.stream().anyMatch(user -> user.getUsername().equals(username));
    }

    // Method to validate the username and password combination
    public Optional<User> validateCredentials(String username, String password) {
        return userList.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst();
    }

    // Get the list of all users (if needed)
    public List<User> getUserList() {
        return userList;
    }
}
