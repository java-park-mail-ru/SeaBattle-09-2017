package seabattle.queries;

public class UserQueries {
    public static String addUser() {
        return "INSERT INTO users (email, login, password) VALUES (?, ?, ?);";
    }

    public static String getByLoginOrEmail() {
        return "SELECT DISTINCT email, login, password, score FROM users WHERE email = ? OR login = ?;";
    }

    public static String changeUser() {
        return "UPDATE users SET (email, password) = (?, ?) WHERE login = ?;";
    }

    public static String getLeaderboard() {
        return "SELECT login, score FROM users ORDER BY score DESC";
    }
}
