package data;

import dsa.CommitStack;
import models.Commit;

import java.io.*;
import java.util.HashMap;
import java.util.Stack;

public class CommitStorage {
    private static final String COMMITS_DIR = "commits";
    private HashMap<String, CommitStack> commitMap = new HashMap<>();

    public CommitStorage() {
        new File(COMMITS_DIR).mkdirs();
    }

    // Save a commit
    public void saveCommit(String username, String repoName, String fileName, Commit commit) {
        String key = getKey(username, repoName, fileName);
        commitMap.computeIfAbsent(key, k -> new CommitStack());
        commitMap.get(key).push(commit);
        writeToFile(username, repoName, fileName);
    }

    // Get commit stack for a file
    public CommitStack getCommits(String username, String repoName, String fileName) {
        String key = getKey(username, repoName, fileName);
        if (!commitMap.containsKey(key)) {
            loadFromFile(username, repoName, fileName);
        }
        return commitMap.getOrDefault(key, new CommitStack());
    }

    // Undo last commit
    public Commit undo(String username, String repoName, String fileName) {
        String key = getKey(username, repoName, fileName);
        CommitStack stack = commitMap.get(key);
        if (stack != null && !stack.isEmpty()) {
            Commit undone = stack.undo();
            writeToFile(username, repoName, fileName);
            return undone;
        }
        return null;
    }

    // Write commits to file
    private void writeToFile(String username, String repoName, String fileName) {
        try {
            String key = getKey(username, repoName, fileName);
            File dir = new File(COMMITS_DIR + "/" + username + "/" + repoName);
            dir.mkdirs();
            File file = new File(dir, fileName + ".commits");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            Stack<Commit> commits = commitMap.get(key).getAllCommits();
            for (Commit c : commits) {
                writer.write(c.toFileString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving commits: " + e.getMessage());
        }
    }

    // Load commits from file
    private void loadFromFile(String username, String repoName, String fileName) {
        try {
            String key = getKey(username, repoName, fileName);
            File file = new File(COMMITS_DIR + "/" + username + "/" + repoName + "/" + fileName + ".commits");
            if (!file.exists()) return;
            CommitStack stack = new CommitStack();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                stack.push(Commit.fromFileString(line));
            }
            reader.close();
            commitMap.put(key, stack);
        } catch (IOException e) {
            System.out.println("Error loading commits: " + e.getMessage());
        }
    }

    private String getKey(String username, String repoName, String fileName) {
        return username + "/" + repoName + "/" + fileName;
    }
}