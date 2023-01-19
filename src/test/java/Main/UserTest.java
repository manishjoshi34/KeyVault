package Main;

import Models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void set_name() {
        User user = new User("testUser","testEmail","testPassword");
        user.set_name("testName");
        assertEquals(user.getName(),"testName");
    }

    @Test
    void set_emailId() {
    }

    @Test
    void set_passWord() {
    }

    @Test
    void getName() {
    }

    @Test
    void getEmailId() {
    }

    @Test
    void getPassword() {
    }

    @Test
    void getDocument() {
    }

    @Test
    void testGetDocument() {
    }

    @Test
    void getCredential() {
    }
}