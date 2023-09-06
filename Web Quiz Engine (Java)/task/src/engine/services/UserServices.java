package engine.services;

import engine.model.User;
import engine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServices {

    private final UserRepository userRepository;

    @Autowired
    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> registerUser(User user) {
        System.out.println("registerUser");
        System.out.println(user);
        if (userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        System.out.println("Inside User register");
        System.out.println(user);
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> deleteQuiz(UserDetails userDetails, Long id) {
        System.out.println("deleteUser");
        System.out.println(userDetails.getUsername());
        System.out.println(id);
        if (!userRepository.existsById(id)) {
            System.out.println("This user is not present");
            return ResponseEntity.notFound().build();
        }
        String authenticatedEmail = userDetails.getUsername().toLowerCase();
        Optional<User> byId = userRepository.findById(id);
        User user = byId.get();
        System.out.println("User By ID");
        System.out.println(user);
        String userEmail = user.getEmail().toLowerCase();
        if (!authenticatedEmail.equals(userEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }
    public ResponseEntity<?> userList() {
        return ResponseEntity.ok(userRepository.findAll());
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
