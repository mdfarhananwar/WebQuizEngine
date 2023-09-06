package engine.controller;

import engine.model.*;
import engine.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import engine.services.QuizServices;

import java.util.List;

@RestController
public class QuizController {

    private final QuizServices quizServices;

    @Autowired
    public QuizController(QuizServices quizServices) {
        this.quizServices = quizServices;
    }

    @PostMapping("/api/quizzes")
    public ResponseEntity<Quiz> postQuiz(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Validated Quiz quiz) {
        return quizServices.postQuiz(userDetails, quiz);
    }

    @PostMapping("/api/quizzes/{id}/solve")
    public ResponseEntity<QuizResponse> quizResult(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody AnswerRequest answer) {
        System.out.println("Enter POST METHOD WITH ID SOLVE");
        return quizServices.quizResult(userDetails, id, answer);
    }

    @GetMapping("/api/quizzes/{id}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable Long id) {
        System.out.println("Enter in GetMapping of specific id");
        return quizServices.getQuiz(id);
    }

    @GetMapping("/api/quizzes")
    public ResponseEntity<Page<Quiz>> getQuizList(@RequestParam int page) {
        System.out.println("Enter in GetMapping List");
        return quizServices.getQuizList(page);
    }

    @GetMapping("/api/quizzes/completed")
    public ResponseEntity<Page<QuizCompletionResponse>> getQuizCompletionList(@AuthenticationPrincipal UserDetails userDetails, @RequestParam int page) {
        System.out.println("Enter in Quiz CompletionList");
        return quizServices.getQuizCompletionList(userDetails, page);
    }
    @DeleteMapping("/api/quizzes/{id}")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        return quizServices.deleteQuiz(userDetails,id);
    }



}
