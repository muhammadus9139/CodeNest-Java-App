package ui;


import data.CommitStorage;
import data.RepoStorage;
import data.UserStorage;
import models.Repo;
import models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class RepoScreen {

    private Stage stage;
    private User currentUser;
    private UserStorage userStorage;
    private RepoStorage repoStorage;
    private CommitStorage commitStorage;

    public RepoScreen(Stage stage, User currentUser, UserStorage userStorage, RepoStorage repoStorage, CommitStorage commitStorage) {
        this.stage = stage;
        this.currentUser = currentUser;
        this.userStorage = userStorage;
        this.repoStorage = repoStorage;
        this.commitStorage = commitStorage;
    }

    public Scene getScene() {

        // ── Navbar ──
        HBox navbar = new HBox();
        navbar.setPadding(new Insets(14, 24, 14, 24));
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setSpacing(20);
        navbar.setStyle("-fx-background-color: #2d6a4f;");

        Text logo = new Text("CodeNest");
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        logo.setFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backBtn = new Button("← Dashboard");
        backBtn.setStyle("""
                -fx-background-color: transparent;
                -fx-text-fill: white;
                -fx-font-size: 14;
                -fx-cursor: hand;
                """);

        navbar.getChildren().addAll(logo, spacer, backBtn);

        // ── Header ──
        HBox headerBox = new HBox();
        headerBox.setPadding(new Insets(24, 30, 10, 30));
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Text heading = new Text("My Repositories");
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        heading.setFill(Color.web("#222222"));

        Region hSpacer = new Region();
        HBox.setHgrow(hSpacer, Priority.ALWAYS);

        Button newRepoBtn = new Button("➕ New Repository");
        newRepoBtn.setStyle("""
                -fx-background-color: #2d6a4f;
                -fx-text-fill: white;
                -fx-font-size: 14;
                -fx-padding: 10 20;
                -fx-background-radius: 8;
                -fx-cursor: hand;
                """);

        headerBox.getChildren().addAll(heading, hSpacer, newRepoBtn);

        // ── Repo List ──
        VBox repoList = new VBox(12);
        repoList.setPadding(new Insets(10, 30, 30, 30));

        refreshRepoList(repoList);

        // ── New Repo Form (hidden) ──
        VBox newRepoForm = createNewRepoForm(repoList);
        newRepoForm.setVisible(false);
        newRepoForm.setManaged(false);

        newRepoBtn.setOnAction(e -> {
            newRepoForm.setVisible(true);
            newRepoForm.setManaged(true);
        });

        backBtn.setOnAction(e -> {
            DashboardScreen dashboard = new DashboardScreen(stage, currentUser, userStorage, repoStorage, commitStorage);
            stage.setScene(dashboard.getScene());
        });

        // ── Main Layout ──
        VBox mainLayout = new VBox();
        mainLayout.getChildren().addAll(navbar, headerBox, newRepoForm, repoList);
        mainLayout.setStyle("-fx-background-color: #f0f4f0;");

        ScrollPane scrollPane = new ScrollPane(mainLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f0f4f0;");

        return new Scene(scrollPane, 900, 600);
    }

    private void refreshRepoList(VBox repoList) {
        repoList.getChildren().clear();
        ArrayList<Repo> repos = repoStorage.getUserRepos(currentUser.getUsername());

        if (repos.isEmpty()) {
            Label noRepo = new Label("No repositories yet. Create your first one!");
            noRepo.setTextFill(Color.web("#999999"));
            noRepo.setFont(Font.font("Arial", 14));
            repoList.getChildren().add(noRepo);
            return;
        }

        for (Repo repo : repos) {
            repoList.getChildren().add(repoCard(repo, repoList));
        }
    }

    private HBox repoCard(Repo repo, VBox repoList) {
        HBox card = new HBox(16);
        card.setPadding(new Insets(16));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 10;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 2);
                """);

        // Repo info
        VBox infoBox = new VBox(6);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        Text repoName = new Text("📁 " + repo.getRepoName());
        repoName.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        repoName.setFill(Color.web("#2d6a4f"));
        repoName.setStyle("-fx-cursor: hand;");

        Text desc = new Text(repo.getDescription().isEmpty() ? "No description" : repo.getDescription());
        desc.setFont(Font.font("Arial", 13));
        desc.setFill(Color.web("#666666"));

        HBox metaBox = new HBox(16);
        Text lang = new Text("🔵 " + repo.getLanguage());
        lang.setFont(Font.font("Arial", 12));
        lang.setFill(Color.web("#888888"));

        Text visibility = new Text(repo.isPublic() ? "🌐 Public" : "🔒 Private");
        visibility.setFont(Font.font("Arial", 12));
        visibility.setFill(Color.web("#888888"));

        Text stars = new Text("⭐ " + repo.getStars());
        stars.setFont(Font.font("Arial", 12));
        stars.setFill(Color.web("#888888"));

        Text commits = new Text("📝 " + repo.getCommits() + " commits");
        commits.setFont(Font.font("Arial", 12));
        commits.setFill(Color.web("#888888"));

        metaBox.getChildren().addAll(lang, visibility, stars, commits);
        infoBox.getChildren().addAll(repoName, desc, metaBox);

        // Buttons
        Button openBtn = new Button("Open");
        openBtn.setStyle("""
                -fx-background-color: #2d6a4f;
                -fx-text-fill: white;
                -fx-font-size: 12;
                -fx-padding: 6 14;
                -fx-background-radius: 6;
                -fx-cursor: hand;
                """);

        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle("""
                -fx-background-color: #ff4444;
                -fx-text-fill: white;
                -fx-font-size: 12;
                -fx-padding: 6 14;
                -fx-background-radius: 6;
                -fx-cursor: hand;
                """);

        // Open repo
        openBtn.setOnAction(e -> {
            RepoDetailScreen detail = new RepoDetailScreen(stage, currentUser, repo, userStorage, repoStorage, commitStorage);
            stage.setScene(detail.getScene());
        });

        // Delete repo
        deleteBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Repository");
            alert.setHeaderText("Delete " + repo.getRepoName() + "?");
            alert.setContentText("This action cannot be undone.");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    repoStorage.deleteRepo(repo.getRepoName(), currentUser.getUsername());
                    refreshRepoList(repoList);
                }
            });
        });

        card.getChildren().addAll(infoBox, openBtn, deleteBtn);
        return card;
    }

    private VBox createNewRepoForm(VBox repoList) {
        VBox form = new VBox(12);
        form.setPadding(new Insets(20, 30, 10, 30));
        form.setMaxWidth(600);

        Text formTitle = new Text("Create New Repository");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        formTitle.setFill(Color.web("#2d6a4f"));

        TextField nameField = new TextField();
        nameField.setPromptText("Repository Name");
        nameField.setStyle("-fx-padding: 10; -fx-font-size: 14;");

        TextField descField = new TextField();
        descField.setPromptText("Description (optional)");
        descField.setStyle("-fx-padding: 10; -fx-font-size: 14;");

        ToggleGroup visibilityGroup = new ToggleGroup();
        RadioButton publicBtn = new RadioButton("🌐 Public");
        RadioButton privateBtn = new RadioButton("🔒 Private");
        publicBtn.setToggleGroup(visibilityGroup);
        privateBtn.setToggleGroup(visibilityGroup);
        publicBtn.setSelected(true);

        HBox visibilityBox = new HBox(20, publicBtn, privateBtn);
        visibilityBox.setAlignment(Pos.CENTER_LEFT);

        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);

        Button createBtn = new Button("Create Repository");
        createBtn.setStyle("""
                -fx-background-color: #2d6a4f;
                -fx-text-fill: white;
                -fx-font-size: 14;
                -fx-padding: 10 24;
                -fx-background-radius: 6;
                -fx-cursor: hand;
                """);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("""
                -fx-background-color: #cccccc;
                -fx-text-fill: #333333;
                -fx-font-size: 14;
                -fx-padding: 10 24;
                -fx-background-radius: 6;
                -fx-cursor: hand;
                """);

        HBox btnBox = new HBox(12, createBtn, cancelBtn);

        createBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                errorLabel.setText("Repository name is required!");
                return;
            }
            boolean isPublic = publicBtn.isSelected();
            Repo newRepo = new Repo(name, currentUser.getUsername(), descField.getText().trim(), isPublic);
            boolean saved = repoStorage.createRepo(newRepo);
            if (saved) {
                form.setVisible(false);
                form.setManaged(false);
                refreshRepoList(repoList);
            } else {
                errorLabel.setText("Repository name already exists!");
            }
        });

        cancelBtn.setOnAction(e -> {
            form.setVisible(false);
            form.setManaged(false);
        });

        form.getChildren().addAll(formTitle, nameField, descField, visibilityBox, errorLabel, btnBox);
        form.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 10;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 2);
                -fx-padding: 20;
                """);
        return form;
    }
}