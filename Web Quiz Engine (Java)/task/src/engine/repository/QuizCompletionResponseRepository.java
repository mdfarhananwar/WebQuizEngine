package engine.repository;

import engine.model.QuizCompletionResponse;
import engine.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuizCompletionResponseRepository extends JpaRepository<QuizCompletionResponse, Long>,
        PagingAndSortingRepository<QuizCompletionResponse, Long> {

    Page<QuizCompletionResponse> findAllByUser(User user, Pageable pageable);

    Page<QuizCompletionResponse> findByUserOrderByCompletedAtDesc(User user, Pageable pageable);



}
