package batteux.FineArt.Basic.service;

import batteux.FineArt.Basic.model.User;
import batteux.FineArt.Basic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void createUser(User user) {
        userRepository.createUser(user);
    }

    public User login(String username, String password) {
        return userRepository.loginUser(username, password);
    }

    public User getUserById(int userId) {
        return userRepository.findUserById(userId);
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    public User findUserByEmailOrLoginName(String emailOrLoginName) {
        return userRepository.findUserByEmailOrLoginName(emailOrLoginName);
    }
    
    public void updatePassword(int userId, String newPassword) {
        userRepository.updatePassword(userId, newPassword);
    }
    
}
