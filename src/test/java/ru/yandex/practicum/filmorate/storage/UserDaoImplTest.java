package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDaoImplTest {
    private final UserDaoImpl userDao;

    User testUserOne;
    User testUserTwo;
    User testUserThree;

    @BeforeEach
    public void initUsers() {
        testUserOne = User.builder().name("tom").email("email@test.ru").login("anderson")
                .birthday(LocalDate.of(2000, 12, 12)).build();
        testUserTwo = User.builder().name("john").email("email2@test.ru").login("bjornson")
                .birthday(LocalDate.of(2000, 12, 12)).build();
        testUserThree = User.builder().name("John").email("testJohn@email.com")
                .login("JohnMohn").birthday(LocalDate.of(2010, 12, 12)).build();
    }

    @Test
    void createUser() {
        User expectedUser = testUserOne;
        User actualUser;

        userDao.createUser(testUserOne);
        actualUser = userDao.readUser(1);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void readUser() {
        User expectedUser = testUserThree;
        User actualUser;

        userDao.createUser(testUserOne);
        userDao.createUser(testUserTwo);
        userDao.createUser(testUserThree);
        actualUser = userDao.readUser(3);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void updateUser() {
        User expectedUser;
        User actualUser;

        userDao.createUser(testUserOne);
        testUserTwo.setId(1);
        expectedUser = testUserTwo;
        userDao.updateUser(expectedUser);
        actualUser = userDao.readUser(1);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void getAllUsers() {
        List<User> expectedList = List.of(testUserOne, testUserTwo, testUserThree);
        List<User> actualList;

        userDao.createUser(testUserOne);
        userDao.createUser(testUserTwo);
        userDao.createUser(testUserThree);
        actualList = userDao.getAllUsers();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    void addUserFriend() {
        User expecteUser = testUserThree;
        User actualUser;

        userDao.createUser(testUserOne);
        userDao.createUser(testUserTwo);
        userDao.createUser(testUserThree);
        userDao.addUserFriend(1, 3);
        actualUser = userDao.getUserFriends(1).get(0);

        assertEquals(expecteUser, actualUser);
    }

    @Test
    void deleteUserFriend() {
        List<User> expecteFriendList = List.of(testUserTwo);
        List<User> actualFriendList;

        userDao.createUser(testUserOne);
        userDao.createUser(testUserTwo);
        userDao.createUser(testUserThree);
        userDao.addUserFriend(1, 2);
        userDao.addUserFriend(1, 3);
        userDao.deleteUserFriend(1, 3);
        actualFriendList = userDao.getUserFriends(1);

        assertTrue(expecteFriendList.size() == actualFriendList.size());
    }

    @Test
    void getUserFriends() {
        List<User> expecteFriendList = List.of(testUserTwo, testUserThree);
        List<User> actualFriendList;

        userDao.createUser(testUserOne);
        userDao.createUser(testUserTwo);
        userDao.createUser(testUserThree);
        userDao.addUserFriend(1, 2);
        userDao.addUserFriend(1, 3);
        actualFriendList = userDao.getUserFriends(1);

        assertArrayEquals(expecteFriendList.toArray(), actualFriendList.toArray());
    }

    @Test
    void getCommonFriends() {
        List<User> expecteFriendList = List.of(testUserTwo);
        List<User> actualFriendList;

        userDao.createUser(testUserOne);
        userDao.createUser(testUserTwo);
        userDao.createUser(testUserThree);
        userDao.addUserFriend(1, 2);
        userDao.addUserFriend(3, 2);
        actualFriendList = userDao.getCommonFriends(1, 3);

        assertArrayEquals(expecteFriendList.toArray(), actualFriendList.toArray());
    }
}