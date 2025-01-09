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

    private static final String createUserSQL = "INSERT INTO public.myusers (\n" +
            "id, firstname, lastname, age) VALUES (\n" +
            "'?'::integer, '?'::character varying, '?'::character varying, '?'::integer)\n" +
            " returning id;";
    private static final String updateUserSQL = "UPDATE public.myusers SET\n" +
            "firstname = '?'::character varying WHERE\n" +
            "id = 1;";
    private static final String deleteUser = "DELETE FROM public.myusers\n" +
            "    WHERE id IN\n" +
            "        (?);";
    private static final String findUserByIdSQL = "SELECT * FROM public.myusers WHERE id = ?;";
    private static final String findUserByNameSQL = "SELECT * FROM public.myusers WHERE name = ? LIMIT 1;";
    private static final String findAllUserSQL = "SELECT * FROM public.myusers;";

    public Long createUser() {
        return null;
    }

    public User findUserById(Long userId) {
        return null;
    }

    public User findUserByName(String userName) {
        return null;
    }

    public List<User> findAllUser() {
        List<User> queryResult = new ArrayList<>();
        try (Statement st = connection.createStatement()) {
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

    public User updateUser() {
        return null;
    }

    private void deleteUser(Long userId) {
    }
}
