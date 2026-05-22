package data;

import dsa.Trie;
import models.Repo;

import java.util.ArrayList;
import java.util.List;

public class SearchEngine {
    private Trie repoTrie = new Trie();
    private ArrayList<Repo> allRepos = new ArrayList<>();

    // Load all repos into Trie
    public void loadRepos(ArrayList<Repo> repos) {
        repoTrie.clear();
        allRepos.clear();
        allRepos.addAll(repos);
        for (Repo repo : repos) {
            repoTrie.insert(repo.getRepoName());
            repoTrie.insert(repo.getLanguage());
        }
    }

    // Autocomplete by prefix
    public List<String> autocomplete(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) return new ArrayList<>();
        return repoTrie.autocomplete(prefix.trim());
    }

    // Search repos by name
    public ArrayList<Repo> searchByName(String query) {
        ArrayList<Repo> results = new ArrayList<>();
        String q = query.toLowerCase().trim();
        for (Repo repo : allRepos) {
            if (repo.getRepoName().toLowerCase().contains(q)) {
                results.add(repo);
            }
        }
        return results;
    }

    // Search repos by language
    public ArrayList<Repo> searchByLanguage(String language) {
        ArrayList<Repo> results = new ArrayList<>();
        for (Repo repo : allRepos) {
            if (repo.getLanguage().equalsIgnoreCase(language.trim())) {
                results.add(repo);
            }
        }
        return results;
    }

    // Combined search — name or language
    public ArrayList<Repo> search(String query) {
        ArrayList<Repo> results = new ArrayList<>();
        String q = query.toLowerCase().trim();
        for (Repo repo : allRepos) {
            if (repo.getRepoName().toLowerCase().contains(q) ||
                    repo.getLanguage().toLowerCase().contains(q) ||
                    repo.getDescription().toLowerCase().contains(q)) {
                results.add(repo);
            }
        }
        return results;
    }
}