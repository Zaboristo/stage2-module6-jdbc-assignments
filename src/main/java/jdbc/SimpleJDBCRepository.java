package jdbc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {

    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;

    private static final String createUserSQL = "INSERT INTO public.myusers (firstname, lastname, age) VALUES (?, ?, ?)";
    private static final String updateUserSQL = "UPDATE public.myusers SET " +
            "firstname = ?, lastname = ?, age = ? WHERE id = ?;";
    private static final String deleteUser = "DELETE FROM public.myusers WHERE id = ?;";
    private static final String findUserByIdSQL = "SELECT * FROM public.myusers WHERE id = ?;";
    private static final String findUserByNameSQL = "SELECT * FROM public.myusers WHERE firstname = ? LIMIT 1;";
    private static final String findAllUserSQL = "SELECT * FROM public.myusers;";

    public Long createUser(User user) {
        Long returnedId = null;
        if (user.getFirstName() == null || user.getLastName() == null) {
            throw new IllegalArgumentException("First name and last name cannot be null.");
        }
        try {
            this.connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(createUserSQL, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(3, user.getAge());
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    returnedId = rs.getLong("id"); // Get the generated id
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnedId;
    }

    public User findUserById(Long userId) {
        User user = new User();
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(findUserByIdSQL);
            ps.setLong(1, userId);
            ResultSet result = ps.executeQuery(createUserSQL);
            user.setId(userId);
            user.setFirstName(result.getString("firstname"));
            user.setLastName(result.getString("lastname"));
            user.setAge(result.getInt("age"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User findUserByName(String userName) {
        User user = new User();
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(findUserByNameSQL);
            ps.setString(1, userName);
            ResultSet result = ps.executeQuery(createUserSQL);
            user.setId(result.getLong("id"));
            user.setFirstName(result.getString("firstname"));
            user.setLastName(result.getString("lastname"));
            user.setAge(result.getInt("age"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<User> findAllUser() {
        List<User> queryResult = new ArrayList<>();
        try  {
            connection = CustomDataSource.getInstance().getConnection();
            st = connection.createStatement();
            ResultSet result = st.executeQuery(findAllUserSQL);
            while (result.next()) {
                User user = new User();
                user.setId(result.getLong("id"));
                user.setAge(result.getInt("age"));
                user.setFirstName(result.getString("firstname"));
                user.setLastName(result.getString("lastname"));
                queryResult.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return queryResult;
    }

    public User updateUser(User user) {
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(updateUserSQL);
            ps.setLong(4, user.getId());
            ps.setInt(3, user.getAge());
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void deleteUser(Long userId) {
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(deleteUser);
            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
