package ru.java.mentor.factory;

import ru.java.mentor.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCDao implements DAO {
    private static JDBCDao instance;
    private Connection connection;

    private JDBCDao(Connection connection) {
        this.connection = connection;
    }

    static JDBCDao getInstance(Connection connection) {
        if (instance == null) {
            instance = new JDBCDao(connection);
        }
        return instance;
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        List<User> list = new ArrayList<>();
        Statement stmt = connection.createStatement();
        stmt.execute("select * from users");
        ResultSet result = stmt.getResultSet();
        while (result.next()) {
            long id = result.getLong("id");
            String name = result.getNString("name");
            String login = result.getNString("login");
            String pass = result.getNString("password");
            String address = result.getNString("address");
            int birthdate = result.getInt("birthdate");
            User user = new User(id, name, login, pass, address, birthdate);
            list.add(user);
        }
        result.close();
        stmt.close();
        return list;
    }

    @Override
    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users(name, login, password, address, birthdate) values (?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getName());
        statement.setString(2, user.getLogin());
        statement.setString(3, user.getPassword());
        statement.setString(4, user.getAddress());
        statement.setInt(5, user.getbirthdate());
        statement.executeUpdate();
        statement.close();
    }

    @Override
    public void editUser(User user) throws SQLException {
        String sql = "UPDATE users set name=?, login=?, password=?, address=?, birthdate=? where id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getName());
        statement.setString(2, user.getLogin());
        statement.setString(3, user.getPassword());
        statement.setString(4, user.getAddress());
        statement.setInt(5, user.getbirthdate());
        statement.setLong(6, user.getId());
        statement.executeUpdate();
        statement.close();
    }

    @Override
    public void deleteUser(User user) throws SQLException {
        long id = user.getId();
        String sql = "DELETE FROM users WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, id);
        statement.executeUpdate();
        statement.close();
    }

    @Override
    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS users");
        stmt.close();
    }


    @Override
    public User getUserById(Long id) throws SQLException {
        User user = null;
        PreparedStatement preparedStatement = connection.prepareStatement("select * from users where id=?");
        preparedStatement.setLong(1, id);
        ResultSet result = preparedStatement.executeQuery();
        result.next();
        String name = result.getNString("name");
        String login = result.getNString("login");
        String pass = result.getNString("password");
        String address = result.getNString("address");
        int birthdate = result.getInt("birthdate");
        user = new User(id, name, login, pass, address, birthdate);
        result.close();
        preparedStatement.close();
        return user;
    }


    public boolean validateUser(String name, String password) {
        try {
            User user = getUserByName(name);
            if (user.getPassword().equals(password)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    private User getUserByName(String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from users where name=?");
        preparedStatement.setString(1, name);
        ResultSet result = preparedStatement.executeQuery();
        result.next();
        long id = result.getLong("id");
        String login = result.getNString("login");
        String pass = result.getNString("password");
        String address = result.getNString("address");
        int birthdate = result.getInt("birthdate");
        User user = new User(id, name, login, pass, address, birthdate);
        result.close();
        preparedStatement.close();
        return user;
    }

    @Override
    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists users (id bigint auto_increment,  " +
                "name varchar(256),\n" +
                "login varchar(20),\n" +
                "password varchar(256),\n" +
                " address varchar(20),\n" +
                "birthdate int, \n" +
                "primary key (id))");
        stmt.close();
    }
}
