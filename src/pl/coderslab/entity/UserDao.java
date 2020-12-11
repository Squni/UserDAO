package pl.coderslab.entity;

import pl.coderslab.BCrypt;
import pl.coderslab.DBUtil;
import java.sql.*;
import java.util.Arrays;

public class UserDao {
    private User[] users;
    private static final String CREATE_USER = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private static final String EDIT_USER = "UPDATE users SET users.email = ?, users.username = ?, users.password = ? WHERE users.id = ?;";

    public User create(User user) {
        try (Connection conn = DBUtil.connect()) {
            PreparedStatement statement =
                    conn.prepareStatement(CREATE_USER, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt())); // TODO : hash pass
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User read(int userId) {
        User user = null;
        try (Connection conn = DBUtil.connect(); ResultSet rs = DBUtil.getById(conn, "users", userId)) {
            while (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setUserName(rs.getString("username"));
                user.setPassword(rs.getString("password"));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return user;
        }
    }

    private User[] addToArray(User u, User[] users) {

        User[] tmpUsers = Arrays.copyOf(users, users.length + 1);

        tmpUsers[users.length] = u;

        return tmpUsers;

    }

    public User[] findAll() {
        this.users = new User[0];
        try (Connection conn = DBUtil.connect(); ResultSet rs = DBUtil.getAll(conn, "users")) {
            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"));
                users = addToArray(user, users);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return users;
        }
    }

    // Method needs to use Main.getNewInfo() to obtain User user
    public void update(User user) {
        try (Connection conn = DBUtil.connect()) {
            DBUtil.edit(conn,
                    EDIT_USER,
                    user.getId(),
                    user.getEmail(),
                    user.getUserName(),
                    BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void delete(int userId) {
        try (Connection conn = DBUtil.connect()) {
            DBUtil.remove(conn, "users", userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
