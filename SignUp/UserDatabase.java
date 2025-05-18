import java.util.ArrayList;
import java.util.List;

public class UserDatabase {
    private static List<User> users = new ArrayList<>();
    
    public static void addUser(User user) {
        users.add(user);
    }

    public static User findUserByEmail(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public static List<User> getAllUsers() {
        return users;
    }
}
