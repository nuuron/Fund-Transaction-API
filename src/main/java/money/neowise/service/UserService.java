package money.neowise.service;

import money.neowise.entity.User;
import money.neowise.exception.UserException;
import money.neowise.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Boolean userExistsById(UUID id) {
        return userRepository.existsById(id);
    }

    public User getUserById(UUID id) throws UserException {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserException(
                    HttpStatus.NOT_FOUND,
                    "No such User found for id: '" + id + "'"
                )
            );
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

}
