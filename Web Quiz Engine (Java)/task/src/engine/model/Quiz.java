package engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String text;
    @ElementCollection(fetch = FetchType.EAGER)
    @NotNull
    @Size(min = 2)
    @CollectionTable(name = "quiz_options", joinColumns = @JoinColumn(name = "quiz_id"))
    @OrderColumn(name = "option_order")
    private List<String> options;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Integer> answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private User user;


    public Quiz() {
    }

    public Quiz(String title, String text, @NotNull @Size(min = 2) List<String> options, Set<Integer> answer) {
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
    }
    public Quiz(String title, String text, @NotNull @Size(min = 2) List<String> options) {
        this.title = title;
        this.text = text;
        this.options = options;
    }

    public String getTitle() {
        return title;
    }

    @JsonProperty("id") // Use a different name in the JSON response if needed
    public Long getId() {
        return id;
    }


    @JsonIgnore // Ignore this field during deserialization (request body)
    public void setId(Long id) {
        // You can leave this setter method empty, or handle it as needed.
        // This will prevent the "id" field from being set when deserializing.
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public @NotNull @Size(min = 2) List<String> getOptions() {
        return options;
    }

    public void setOptions(@NotNull @Size(min = 2) List<String> options) {
        this.options = options;
    }

    @JsonIgnore // This annotation tells Jackson to ignore this field during serialization
    public Set<Integer> getAnswer() {
        return answer;
    }

    @JsonProperty("answer") // Use a different name in the JSON response if needed
    public void setAnswer(Set<Integer> answer) {
        this.answer = answer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
