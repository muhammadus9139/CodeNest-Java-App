package data;

import models.Repo;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class RepoStorage {
    private static final String FILE_PATH = "repos.txt";
    private LinkedList<Repo> allRepos = new LinkedList<>();

    public RepoStorage() {
        loadFromFile();
    }

    // Create repo
    public boolean createRepo(Repo repo) {
        for (Repo r : allRepos) {
            if (r.getRepoName().equals(repo.getRepoName()) &&
                    r.getOwnerUsername().equals(repo.getOwnerUsername())) {
                return false; // already exists
            }
        }
        allRepos.add(repo);
        writeToFile();
        return true;
    }

    // Get all repos of a user
    public ArrayList<Repo> getUserRepos(String username) {
        ArrayList<Repo> userRepos = new ArrayList<>();
        for (Repo r : allRepos) {
            if (r.getOwnerUsername().equals(username)) {
                userRepos.add(r);
            }
        }
        return userRepos;
    }

    // Delete repo
    public void deleteRepo(String repoName, String username) {
        allRepos.removeIf(r -> r.getRepoName().equals(repoName) &&
                r.getOwnerUsername().equals(username));
        writeToFile();
    }

    // Update repo
    public void updateRepo(Repo repo) {
        for (int i = 0; i < allRepos.size(); i++) {
            Repo r = allRepos.get(i);
            if (r.getRepoName().equals(repo.getRepoName()) &&
                    r.getOwnerUsername().equals(repo.getOwnerUsername())) {
                allRepos.set(i, repo);
                break;
            }
        }
        writeToFile();
    }

    // Get all public repos
    public ArrayList<Repo> getPublicRepos() {
        ArrayList<Repo> publicRepos = new ArrayList<>();
        for (Repo r : allRepos) {
            if (r.isPublic()) {
                publicRepos.add(r);
            }
        }
        return publicRepos;
    }

    // Load from file
    private void loadFromFile() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                allRepos.add(Repo.fromFileString(line));
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading repos: " + e.getMessage());
        }
    }

    // Write to file
    private void writeToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));
            for (Repo r : allRepos) {
                writer.write(r.toFileString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving repos: " + e.getMessage());
        }
    }
}