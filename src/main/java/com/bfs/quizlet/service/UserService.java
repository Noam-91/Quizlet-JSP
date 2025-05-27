package com.bfs.quizlet.service;

import com.bfs.quizlet.dao.UserDao;
import com.bfs.quizlet.domain.User;
import com.bfs.quizlet.dto.UserRegisterDto;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) { this.userDao = userDao; }

    @Transactional
    public void createNewUser(UserRegisterDto user) {
        String encodedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));

        User newUser = User.builder()
                .email(user.getEmail())
                .password(encodedPassword)
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .isActive(true)
                .isAdmin(false)
                .build();
        user.setPassword(encodedPassword);
        User existingUser = userDao.getUserByEmail(user.getEmail());
        if(existingUser != null){
            throw new RuntimeException("Email already exists");
        }
        userDao.createNewUser(newUser);
    }

    public Optional<User> validateLogin(String username, String password) {

        return userDao.getAllUsers().stream()
                .filter(a -> a.getEmail().equals(username)
                        && BCrypt.checkpw(password, a.getPassword()))
                .filter(User::getIsActive)
                .findAny();
    }

    /** Toggle user status by userId
     * record the updated admin userId
     * @param userId
     * @param updatedBy
     */
    @Transactional
    public void toggleUserStatus(Long userId, Long updatedBy) {
        userDao.toggleUserStatus(userId, updatedBy);
    }

    public Optional<User> getUserByUserId(Long userId) {
        return userDao.getUserByUserId(userId);
    }

    public String getFullNameById(Long userId) {
        Optional<User> user = userDao.getUserByUserId(userId);
        if(user.isPresent()){
            return user.get().getFirstname() + " " + user.get().getLastname();
        }
        return "Unknown";
    }
}
