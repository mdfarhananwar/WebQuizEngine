package engine.services;

import engine.model.*;
import engine.repository.QuizCompletionResponseRepository;
import engine.repository.QuizRepository;
import engine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class QuizServices {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuizCompletionResponseRepository quizCompletionResponseRepository;

    @Autowired
    public QuizServices(QuizRepository quizRepository, UserRepository userRepository, QuizCompletionResponseRepository quizCompletionResponseRepository) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.quizCompletionResponseRepository = quizCompletionResponseRepository;
    }

    public ResponseEntity<QuizResponse> quizResult(UserDetails userDetails, Long id, AnswerRequest answer) {
        System.out.println("Enter QUIZ SERVICES POST METHOD SOLVE");
        System.out.println(answer.answer);

        Optional<Quiz> byId = quizRepository.findById(id);
        Quiz checkQuiz = byId.orElse(null);
        if (checkQuiz == null) {
            return ResponseEntity.notFound().build();
        }

        String email = userDetails.getUsername();
        User user = userRepository.findByEmailIgnoreCase(email);


        Set<Integer> quizAnswers = checkQuiz.getAnswer();
        Set<Integer> userAnswers = answer.answer;
        // Check if both the user's answer and the correct answer are empty
        if (isEmpty(quizAnswers) && isEmpty(userAnswers)) {
            QuizCompletionResponse quizCompletionResponse = new QuizCompletionResponse(id, LocalDateTime.now());
            quizCompletionResponse.setUser(user);
            quizCompletionResponseRepository.save(quizCompletionResponse);
            user.getQuizCompletionResponses().add(quizCompletionResponse);
            userRepository.save(user);
            return ResponseEntity.ok(new QuizResponse(true, "Congratulations, you're right!"));
        }
        // Check if the correct answer is empty
        if (isEmpty(quizAnswers)) {

            return ResponseEntity.ok(new QuizResponse(false, "Wrong answer! Please, try again."));
        }
        // Check if the user's answer is empty
        if (isEmpty(userAnswers)) {
            return ResponseEntity.ok(new QuizResponse(false, "Wrong answer! Please, try again."));
        }

        System.out.println(checkQuiz);

        if (quizAnswers.size() == userAnswers.size()) {
            int count = countMatchingAnswers(quizAnswers, userAnswers);
            System.out.println("count :" +count);
            if (count == quizAnswers.size()) {
                QuizCompletionResponse quizCompletionResponse = new QuizCompletionResponse(id, LocalDateTime.now());
                quizCompletionResponse.setUser(user);
                quizCompletionResponseRepository.save(quizCompletionResponse);
                user.getQuizCompletionResponses().add(quizCompletionResponse);
                userRepository.save(user);
                System.out.println(quizCompletionResponse);

                return ResponseEntity.ok(new QuizResponse(true, "Congratulations, you're right!"));
            }
        }
        return ResponseEntity.ok(new QuizResponse(false, "Wrong answer! Please, try again."));
    }

    // Helper method to check if a list is empty or null
    private boolean isEmpty(Set<Integer> list) {
        return list == null || list.isEmpty();
    }

    // Helper method to count matching answers
    private int countMatchingAnswers(Set<Integer> quizAnswers, Set<Integer> userAnswers) {
        int count = 0;
        for (Integer userAnswer : userAnswers) {
            if (quizAnswers.contains(userAnswer)) {
                count++;
            }
        }
        return count;
    }

    public ResponseEntity<Quiz> getQuiz(Long id) {

        if (!quizRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Optional<Quiz> byId = quizRepository.findById(id);
        Quiz quiz = byId.orElse(null);
        System.out.println(quiz);
        return ResponseEntity.ok(quiz);
    }

    public ResponseEntity<Page<Quiz>> getQuizList(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Quiz> all = quizRepository.findAll(pageable);
        System.out.println(all);
        return ResponseEntity.ok(all);
    }

    public ResponseEntity<Quiz> postQuiz(UserDetails userDetails, Quiz quiz) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmailIgnoreCase(email);
        quiz.setUser(user);
        System.out.println(quiz);
        quiz = quizRepository.save(quiz);
        System.out.println(quiz);
        return ResponseEntity.ok(quiz);
    }
    public ResponseEntity<?> deleteQuiz(UserDetails userDetails, Long id) {
        System.out.println("deleteUser");
        System.out.println(userDetails.getUsername());
        System.out.println(id);
        if (!quizRepository.existsById(id)) {
            System.out.println("This user is not present");
            return ResponseEntity.notFound().build();
        }
        String authenticatedEmail = userDetails.getUsername().toLowerCase();
        Optional<Quiz> optionalQuiz = quizRepository.findById(id);
        Quiz quiz = optionalQuiz.get();
        System.out.println("Quiz By ID");
        System.out.println(quiz);
        String userEmail = quiz.getUser().getEmail();
        if (!authenticatedEmail.equalsIgnoreCase(userEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        quizRepository.delete(quiz);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Page<QuizCompletionResponse>> getQuizCompletionList(UserDetails userDetails, int page) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmailIgnoreCase(email);
        Long userId = user.getId();
        Pageable pageable = PageRequest.of(page, 10);
        Page<QuizCompletionResponse> quizCompletionResponses = userRepository.
                findQuizCompletionResponsesByUserId(userId, pageable);
        return ResponseEntity.ok(quizCompletionResponses);
    }


}
