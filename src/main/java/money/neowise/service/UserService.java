package money.neowise.service;

import money.neowise.entity.User;
import money.neowise.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Boolean userExistsById(UUID id) {
        return userRepository.existsById(id);
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

}
