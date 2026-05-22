package data;

import models.User;
import java.io.*;
import java.util.HashMap;

public class UserStorage {
    private static final String FILE_PATH = "users.txt";
    private HashMap<String, User> users = new HashMap<>();

    public UserStorage() {
        loadFromFile();
    }

    // Save user
    public boolean saveUser(User user) {
        if (users.containsKey(user.getUsername())) {
            return false; // username already exists
        }
        users.put(user.getUsername(), user);
        writeToFile();
        return true;
    }

    // Login check
    public User loginUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // Load from file
    private void loadFromFile() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                User u = User.fromFileString(line);
                users.put(u.getUsername(), u);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    // Update existing user
    public void updateUser(User user) {
        users.put(user.getUsername(), user);
        writeToFile();
    }

    // Write to file
    private void writeToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));
            for (User u : users.values()) {
                writer.write(u.toFileString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
}