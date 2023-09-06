package engine.services;

import engine.model.User;
import engine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

    private final UserRepository userRepository;

    @Autowired
    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> registerUser(User user) {
                        if (userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
                        user.setPassword(passwordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
