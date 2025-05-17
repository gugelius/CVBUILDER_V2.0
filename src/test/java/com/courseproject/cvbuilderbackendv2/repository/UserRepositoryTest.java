package com.courseproject.cvbuilderbackendv2.repository;

import com.courseproject.cvbuilderbackendv2.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void givenUser_whenFindByUserName_thenReturnUser() {
        User user = new User("testUser", "hashedPassword");
        userRepository.save(user);

        User foundUser = userRepository.findByUserName("testUser");

        assertNotNull(foundUser);
        assertEquals("testUser", foundUser.getUserName());
    }

    @Test
    void givenExistingUser_whenSaveUser_thenUpdateUserPassword() {
        User user = new User("testUser", "hashedPassword");
        userRepository.save(user);

        user.setUserPassword("newHashedPassword");
        userRepository.save(user);
        User updatedUser = userRepository.findByUserName("testUser");

        assertEquals("newHashedPassword", updatedUser.getUserPassword());
    }
}
