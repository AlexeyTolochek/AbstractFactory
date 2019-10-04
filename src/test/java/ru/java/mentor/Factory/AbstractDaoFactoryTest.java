package ru.java.mentor.Factory;

import org.junit.Test;
import ru.java.mentor.model.User;
import ru.java.mentor.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AbstractDaoFactoryTest {
    private User user1 = new User("test", "test", "test", "test", 12);
    private User user2 = new User("test1", "test1", "test1", "test1", 12);


    @Test
    public void userCR() {
        List<User> list;
        UserService service = UserService.getInstance();
        service.dropTable();
        service.createTable();
        service.addUser(user1);
        service.addUser(user2);
        list = service.getAllUsers();
        List<User> referenceUserList = new ArrayList<>();
        user1.setId(1L);
        user2.setId(2L);
        referenceUserList.add(user1);
        referenceUserList.add(user2);
        assertEquals(true, list.equals(referenceUserList));
    }

    @Test
    public void userCRU() {
        UserService service = UserService.getInstance();
        service.dropTable();
        service.createTable();
        User userDB;
        service.addUser(user1);
        List<User> users = service.getAllUsers();
        userDB = users.get(0);
        user1.setId(userDB.getId());
        assertEquals(userDB, user1);
    }

    @Test
    public void userCRD() {
        UserService service = UserService.getInstance();
        service.dropTable();
        service.createTable();
        User userDB0;
        User userDB1;
        service.addUser(user1);
        List<User> users = service.getAllUsers();
        userDB0 = users.get(0);
        service.deleteUser(userDB0);
        userDB1 = service.getUserById(1L);
        user1.setId(userDB0.getId());
        assertTrue(userDB0.equals(user1) && userDB1 == null);
    }

}