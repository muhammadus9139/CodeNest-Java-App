


import data.ActivityTracker;
import data.CommitStorage;
import data.RepoStorage;
import data.UserStorage;
import javafx.application.Application;
import javafx.stage.Stage;
import ui.LoginScreen;
import data.IssueStorage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {

        UserStorage userStorage = new UserStorage();
        RepoStorage repoStorage = new RepoStorage();
        CommitStorage commitStorage = new CommitStorage();
        IssueStorage issueStorage = new IssueStorage();
        ActivityTracker activityTracker = new ActivityTracker();
        LoginScreen loginScreen = new LoginScreen(stage, userStorage, repoStorage, commitStorage, issueStorage, activityTracker);
        stage.setTitle("CodeNest");
        stage.setScene(loginScreen.getScene());
        stage.setMaximized(true);
        stage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}