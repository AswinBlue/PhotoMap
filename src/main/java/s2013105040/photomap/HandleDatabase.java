package s2013105040.photomap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class HandleDatabase {

    //	private static String url = "jdbc:mysql://155.230.118.94:3306";
    private static String url = "jdbc:mysql://localhost:3306";
    private static String dbName = "photoDB";


    public void createDatabase() {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(this.url, "root", "root");
            statement = connection.createStatement();
            String hrappSQL = "CREATE DATABASE IF NOT EXISTS " + this.dbName;
            statement.executeUpdate(hrappSQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                } // nothing we can do
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                } // nothing we can do
            }
        }
    }

    public void dropDatabase() {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(this.url, "root", "root");

            statement = connection.createStatement();
            String hrappSQL = "DROP DATABASE IF EXISTS " + this.dbName;
            statement.executeUpdate(hrappSQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                } // nothing we can do
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                } // nothing we can do
            }
        }
    }
}
