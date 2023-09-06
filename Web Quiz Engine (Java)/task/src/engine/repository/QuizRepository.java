package engine.repository;

import engine.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long>, PagingAndSortingRepository<Quiz, Long> {
}
