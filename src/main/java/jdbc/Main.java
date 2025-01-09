package jdbc;

public class Main {
    public static void main(String[] args) {
        SimpleJDBCRepository db = new SimpleJDBCRepository();
        db.deleteUser(1L);
        db.createUser(new User(1L, "abc", "leo", 3));
    }
}
