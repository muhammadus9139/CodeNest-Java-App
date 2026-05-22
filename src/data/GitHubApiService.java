package data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;

public class GitHubApiService {

    // Search GitHub repos
    public ArrayList<JsonObject> searchRepos(String query) {
        String url = ApiConfig.GITHUB_API_URL + "/search/repositories?q=" + query.replace(" ", "+") + "&sort=stars&per_page=10";
        String response = callGitHub(url);
        ArrayList<JsonObject> repos = new ArrayList<>();

        if (response == null) return repos;

        try {
            JsonObject json = JsonParser.parseString(response).getAsJsonObject();
            JsonArray items = json.getAsJsonArray("items");
            for (int i = 0; i < items.size(); i++) {
                repos.add(items.get(i).getAsJsonObject());
            }
        } catch (Exception e) {
            System.out.println("Error parsing repos: " + e.getMessage());
        }
        return repos;
    }

    // Search GitHub users
    public ArrayList<JsonObject> searchUsers(String query) {
        String url = ApiConfig.GITHUB_API_URL + "/search/users?q=" + query.replace(" ", "+") + "&per_page=10";
        String response = callGitHub(url);
        ArrayList<JsonObject> users = new ArrayList<>();

        if (response == null) return users;

        try {
            JsonObject json = JsonParser.parseString(response).getAsJsonObject();
            JsonArray items = json.getAsJsonArray("items");
            for (int i = 0; i < items.size(); i++) {
                users.add(items.get(i).getAsJsonObject());
            }
        } catch (Exception e) {
            System.out.println("Error parsing users: " + e.getMessage());
        }
        return users;
    }

    // Get trending repos (most starred this week)
    public ArrayList<JsonObject> getTrendingRepos() {
        String url = ApiConfig.GITHUB_API_URL + "/search/repositories?q=stars:%3E1000&sort=stars&order=desc&per_page=10";
        String response = callGitHub(url);
        ArrayList<JsonObject> repos = new ArrayList<>();

        if (response == null) return repos;

        try {
            JsonObject json = JsonParser.parseString(response).getAsJsonObject();
            JsonArray items = json.getAsJsonArray("items");
            for (int i = 0; i < items.size(); i++) {
                repos.add(items.get(i).getAsJsonObject());
            }
        } catch (Exception e) {
            System.out.println("Error parsing trending: " + e.getMessage());
        }
        return repos;
    }

    // Call GitHub API
    private String callGitHub(String urlString) {
        try {
            URI uri = new URI(urlString);
            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
            conn.setRequestProperty("User-Agent", "CodeNest-App");

            // Add token if available
            if (!ApiConfig.GITHUB_TOKEN.isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + ApiConfig.GITHUB_TOKEN);
            }

            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } else {
                System.out.println("GitHub API error: " + responseCode);
                return null;
            }

        } catch (Exception e) {
            System.out.println("GitHub connection error: " + e.getMessage());
            return null;
        }
    }
}