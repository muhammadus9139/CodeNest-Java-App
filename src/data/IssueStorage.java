package data;

import dsa.IssueQueue;
import models.Issue;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class IssueStorage {
    private static final String ISSUES_DIR = "issues";
    private HashMap<String, IssueQueue> issueMap = new HashMap<>();

    public IssueStorage() {
        new File(ISSUES_DIR).mkdirs();
    }

    // Add issue
    public void addIssue(String username, String repoName, Issue issue) {
        String key = getKey(username, repoName);
        issueMap.computeIfAbsent(key, k -> new IssueQueue());
        issueMap.get(key).enqueue(issue);
        writeToFile(username, repoName);
    }

    // Get all issues for a repo
    public ArrayList<Issue> getIssues(String username, String repoName) {
        String key = getKey(username, repoName);
        if (!issueMap.containsKey(key)) {
            loadFromFile(username, repoName);
        }
        ArrayList<Issue> issues = new ArrayList<>();
        IssueQueue queue = issueMap.getOrDefault(key, new IssueQueue());
        for (Issue i : queue.getAll()) {
            issues.add(i);
        }
        return issues;
    }

    // Update issue (open/close)
    public void updateIssues(String username, String repoName, ArrayList<Issue> updatedIssues) {
        String key = getKey(username, repoName);
        IssueQueue newQueue = new IssueQueue();
        for (Issue i : updatedIssues) {
            newQueue.enqueue(i);
        }
        issueMap.put(key, newQueue);
        writeToFile(username, repoName);
    }

    // Write to file
    private void writeToFile(String username, String repoName) {
        try {
            File dir = new File(ISSUES_DIR + "/" + username);
            dir.mkdirs();
            File file = new File(dir, repoName + ".issues");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            String key = getKey(username, repoName);
            IssueQueue queue = issueMap.getOrDefault(key, new IssueQueue());
            for (Issue i : queue.getAll()) {
                writer.write(i.toFileString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving issues: " + e.getMessage());
        }
    }

    // Load from file
    private void loadFromFile(String username, String repoName) {
        try {
            String key = getKey(username, repoName);
            File file = new File(ISSUES_DIR + "/" + username + "/" + repoName + ".issues");
            if (!file.exists()) return;
            IssueQueue queue = new IssueQueue();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                queue.enqueue(Issue.fromFileString(line));
            }
            reader.close();
            issueMap.put(key, queue);
        } catch (IOException e) {
            System.out.println("Error loading issues: " + e.getMessage());
        }
    }

    private String getKey(String username, String repoName) {
        return username + "/" + repoName;
    }
}