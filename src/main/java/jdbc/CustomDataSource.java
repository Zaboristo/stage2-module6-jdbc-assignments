package jdbc;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import jdbc.CustomConnector;

@Getter
@Setter
public class CustomDataSource implements DataSource {
    private static volatile CustomDataSource instance;
    private final String driver;
    private final String url;
    private final String name;
    private final String password;
    private final CustomConnector connector;

    private CustomDataSource(String driver, String url, String password, String name) {
        this.driver = driver;
        this.url = url;
        this.name = name;
        this.password = password;
        this.connector = new CustomConnector();
    }

    public static CustomDataSource getInstance() {
        if (instance == null) {
            Properties props = new Properties();
            try {
                Class.forName(props.driver);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            instance = new CustomDataSource(props.driver, props.url, props.password, props.name);
        }
        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connector.getConnection(url);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return connector.getConnection(url, username, password);
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

    private static class Properties {
        private final String driver;
        private final String url;
        private final String name;
        private final String password;

        private Properties() {
            this.driver = System.getProperty("driver");
            this.name = System.getProperty("name");
            this.url = System.getProperty("url");
            this.password = System.getProperty("password");
        }
    }
}
