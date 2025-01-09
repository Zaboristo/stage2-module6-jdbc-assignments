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

    {
        try {
            this.connection = CustomDataSource.getInstance().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String createUserSQL = "INSERT INTO public.myusers (\n" +
            "id, firstname, lastname, age) VALUES (\n" +
            "'?'::bigint, '?'::character varying, '?'::character varying, '?'::integer)\n" +
            " returning id;";
    private static final String updateUserSQL = "UPDATE public.myusers SET\n" +
            "id = '?'::bigint, firstname = '?'::character varying, lastname = '?'::character varying, age = '?'::integer WHERE\n" +
            "id = '?';";
    private static final String deleteUser = "DELETE FROM public.myusers\n" +
            "    WHERE id IN\n" +
            "        (?);";
    private static final String findUserByIdSQL = "SELECT * FROM public.myusers WHERE id = ?;";
    private static final String findUserByNameSQL = "SELECT * FROM public.myusers WHERE name = ? LIMIT 1;";
    private static final String findAllUserSQL = "SELECT * FROM public.myusers;";

    public Long createUser(User user) {
        Long queryResp = 0L;
        try (PreparedStatement st = connection.prepareStatement(createUserSQL)) {
            st.setLong(1, user.getId());
            st.setInt(4, user.getAge());
            st.setString(2, user.getFirstName());
            st.setString(3, user.getLastName());
            queryResp = (long) st.executeUpdate(createUserSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryResp;
    }

    public User findUserById(Long userId) {
        User user = new User();
        try (PreparedStatement st = connection.prepareStatement(findUserByIdSQL)) {
            st.setLong(1, userId);
            ResultSet result = st.executeQuery(createUserSQL);
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
        try (PreparedStatement st = connection.prepareStatement(findUserByNameSQL)) {
            st.setString(1, userName);
            ResultSet result = st.executeQuery(createUserSQL);
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

    public User updateUser(User user) {
        try (PreparedStatement st = connection.prepareStatement(updateUserSQL)) {
            st.setLong(1, user.getId());
            st.setLong(5, user.getId());
            st.setInt(4, user.getAge());
            st.setString(2, user.getFirstName());
            st.setString(3, user.getLastName());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void deleteUser(Long userId) {
        try (PreparedStatement st = connection.prepareStatement(deleteUser)) {
            st.setLong(1, userId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
