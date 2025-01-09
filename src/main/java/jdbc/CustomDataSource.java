package jdbc;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import java.util.Properties;
import jdbc.CustomConnector;

@Getter
@Setter
public class CustomDataSource implements DataSource {
    private static volatile CustomDataSource instance = null;
    private final String driver;
    private final String url;
    private final String name;
    private final String password;
    private final Instance instanceProps = new Instance();

    private CustomDataSource(String driver, String url, String password, String name) {
        this.driver = driver;
        this.url = url;
        this.name = name;
        this.password = password;
    }

    public static CustomDataSource getInstance() {
        if (instance == null) {
            Instance props = new Instance();
            instance = new CustomDataSource(props.driver, props.url, props.password, props.name);
        }
        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return CustomConnector.getConnection(url, name, password);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return CustomConnector.getConnection(url, username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }


    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    private static class Instance {
        private final String driver;
        private final String url;
        private final String name;
        private final String password;

        private Instance() {
            Properties props = new Properties();
            try {
                props.load(CustomDataSource.class.getClassLoader().getResourceAsStream("app.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.driver = props.getProperty("postgres.driver");
            this.name = props.getProperty("postgres.name");
            this.url = props.getProperty("postgres.url");
            this.password = props.getProperty("postgres.password");
            try {
                Class.forName(this.driver);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
