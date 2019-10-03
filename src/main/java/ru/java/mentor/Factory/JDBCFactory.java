package ru.java.mentor.Factory;

import ru.java.mentor.model.User;
import ru.java.mentor.util.DbHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCFactory implements DAO {
    private Connection connection = DbHelper.getJDBCConnection();

    JDBCFactory() {
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void addUser(User user) {
        try {
            String sql = "INSERT INTO users(name, login, password, address, birthdate) values (?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getName());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getAddress());
            statement.setInt(5, user.getbirthdate());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void editUser(User user) {
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(User user) {
        try {
            long id = user.getId();
            String sql = "DELETE FROM users WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUserById(Long id) {
        User user = null;
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists users (id bigint auto_increment,  name varchar(256),\n" +
                "         login varchar(20),\n" +
                "         password varchar(256),\n" +
                "         address varchar(20),\n" +
                "         birthdate bigint, \n" +
                "         primary key (id))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS users");
        stmt.close();
    }
}
