import models.User;

public class Session {
    private static User loggedInUser;

    // Set the logged-in user
    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    // Get the logged-in user
    public static User getLoggedInUser() {
        return loggedInUser;
    }
}
