package engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@IdClass(QuizCompletionResponse.CompletionResponseKey.class)
public class QuizCompletionResponse {

    @Id
    private Long id;

    @Id
    private LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private User user;

    public QuizCompletionResponse() {
    }

    public QuizCompletionResponse(Long id, LocalDateTime completedAt) {
        this.id = id;
        this.completedAt = completedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Define a composite key class
    public static class CompletionResponseKey implements Serializable {
        private Long id;
        private LocalDateTime completedAt;

        public CompletionResponseKey() {
        }

        public CompletionResponseKey(Long id, LocalDateTime completedAt) {
            this.id = id;
            this.completedAt = completedAt;
        }
    }


}
