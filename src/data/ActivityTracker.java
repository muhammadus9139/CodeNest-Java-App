package data;

import dsa.CircularQueue;

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;

public class ActivityTracker {
    private static final String ACTIVITY_DIR = "activity";
    private HashMap<String, CircularQueue> userActivity = new HashMap<>();
    private static final int DAYS = 7;

    public ActivityTracker() {
        new File(ACTIVITY_DIR).mkdirs();
    }

    // Record a commit for today
    public void recordCommit(String username) {
        String key = username;
        CircularQueue queue = getUserQueue(username);
        int todayIndex = LocalDate.now().getDayOfWeek().getValue() - 1;
        int[] data = queue.getAll();

        // Refill queue with updated today count
        CircularQueue newQueue = new CircularQueue(DAYS);
        for (int i = 0; i < DAYS; i++) {
            if (i < data.length) {
                if (i == todayIndex) {
                    newQueue.enqueue(data[i] + 1);
                } else {
                    newQueue.enqueue(data[i]);
                }
            } else {
                if (i == todayIndex) {
                    newQueue.enqueue(1);
                } else {
                    newQueue.enqueue(0);
                }
            }
        }
        userActivity.put(key, newQueue);
        saveToFile(username);
    }

    // Get activity data for last 7 days
    public int[] getWeeklyActivity(String username) {
        CircularQueue queue = getUserQueue(username);
        int[] data = queue.getAll();
        int[] result = new int[DAYS];
        for (int i = 0; i < DAYS; i++) {
            result[i] = i < data.length ? data[i] : 0;
        }
        return result;
    }

    // Get day labels
    public String[] getDayLabels() {
        String[] labels = new String[DAYS];
        LocalDate today = LocalDate.now();
        for (int i = 0; i < DAYS; i++) {
            LocalDate day = today.minusDays(DAYS - 1 - i);
            labels[i] = day.getDayOfWeek()
                    .getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        }
        return labels;
    }

    // Get or create queue
    private CircularQueue getUserQueue(String username) {
        if (!userActivity.containsKey(username)) {
            loadFromFile(username);
        }
        return userActivity.getOrDefault(username, new CircularQueue(DAYS));
    }

    // Save to file
    private void saveToFile(String username) {
        try {
            File file = new File(ACTIVITY_DIR + "/" + username + ".activity");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            int[] data = userActivity.get(username).getAll();
            for (int val : data) {
                writer.write(val + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving activity: " + e.getMessage());
        }
    }

    // Load from file
    private void loadFromFile(String username) {
        try {
            File file = new File(ACTIVITY_DIR + "/" + username + ".activity");
            if (!file.exists()) {
                userActivity.put(username, new CircularQueue(DAYS));
                return;
            }
            CircularQueue queue = new CircularQueue(DAYS);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                queue.enqueue(Integer.parseInt(line.trim()));
            }
            reader.close();
            userActivity.put(username, queue);
        } catch (IOException e) {
            userActivity.put(username, new CircularQueue(DAYS));
        }
    }
}