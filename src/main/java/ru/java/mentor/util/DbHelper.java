package ru.java.mentor.util;

import com.mysql.cj.jdbc.Driver;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.java.mentor.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static ru.java.mentor.util.ReaderProperty.readPropery;

public class DbHelper {

    private static SessionFactory sessionFactory;

    private DbHelper() {
    }


    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                sessionFactory = createSessionFactory();
            } catch (ExceptionFromReadMethod e) {
                e.printStackTrace();
                System.out.println(e);
            }
        }
        return sessionFactory;
    }


    private static Configuration getConfiguration() throws ExceptionFromReadMethod {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.setProperty("hibernate.dialect", readPropery("hibernate.dialect"));
        configuration.setProperty("hibernate.connection.driver_class", readPropery("hibernate.connection.driver_class"));
        configuration.setProperty("hibernate.connection.url", readPropery("hibernate.connection.url"));
        configuration.setProperty("hibernate.connection.username", readPropery("hibernate.connection.username"));
        configuration.setProperty("hibernate.connection.password", readPropery("hibernate.connection.password"));
        configuration.setProperty("hibernate.show_sql", readPropery("hibernate.show_sql"));
        configuration.setProperty("hibernate.hbm2ddl.auto", readPropery("hibernate.hbm2ddl.auto"));
        return configuration;
    }

    private static SessionFactory createSessionFactory() throws ExceptionFromReadMethod {
        Configuration configuration = getConfiguration();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public static Connection getJDBCConnection() {
        Connection connection = null;
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.cj.jdbc.Driver").newInstance());
            connection = DriverManager.getConnection(readPropery("jdbc.connection.url"),
                    readPropery("jdbc.connection.user"),
                    readPropery("jdbc.connection.password"));
        } catch (SQLException | IllegalAccessException | ClassNotFoundException | ExceptionFromReadMethod | InstantiationException e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return connection;
    }
}
