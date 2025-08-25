package com.pluto.learning.assessments;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "quiz_questions")
@EntityListeners(AuditingEntityListener.class)
public class QuizQuestion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;
    
    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;
    
    @Min(1)
    @Column(name = "question_order", nullable = false)
    private Integer questionOrder;
    
    @Min(1)
    @Column(name = "points", nullable = false)
    private Integer points = 1;
    
    @Column(columnDefinition = "TEXT")
    private String explanation;
    
    // JSON stored as string for options and correct answers
    @Column(name = "options_json", columnDefinition = "TEXT")
    private String optionsJson;
    
    @Column(name = "correct_answers_json", columnDefinition = "TEXT") 
    private String correctAnswersJson;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public enum QuestionType {
        MULTIPLE_CHOICE,
        SINGLE_CHOICE,
        TRUE_FALSE,
        TEXT_INPUT,
        CODE_SNIPPET
    }
    
    // Constructors
    public QuizQuestion() {}
    
    public QuizQuestion(Quiz quiz, String questionText, QuestionType questionType, Integer questionOrder) {
        this.quiz = quiz;
        this.questionText = questionText;
        this.questionType = questionType;
        this.questionOrder = questionOrder;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Quiz getQuiz() {
        return quiz;
    }
    
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
    
    public String getQuestionText() {
        return questionText;
    }
    
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
    
    public QuestionType getQuestionType() {
        return questionType;
    }
    
    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }
    
    public Integer getQuestionOrder() {
        return questionOrder;
    }
    
    public void setQuestionOrder(Integer questionOrder) {
        this.questionOrder = questionOrder;
    }
    
    public Integer getPoints() {
        return points;
    }
    
    public void setPoints(Integer points) {
        this.points = points;
    }
    
    public String getExplanation() {
        return explanation;
    }
    
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
    
    public String getOptionsJson() {
        return optionsJson;
    }
    
    public void setOptionsJson(String optionsJson) {
        this.optionsJson = optionsJson;
    }
    
    public String getCorrectAnswersJson() {
        return correctAnswersJson;
    }
    
    public void setCorrectAnswersJson(String correctAnswersJson) {
        this.correctAnswersJson = correctAnswersJson;
    }
    
    /**
     * Verifica si la respuesta del usuario es correcta
     */
    public boolean isAnswerCorrect(String userAnswer) {
        if (userAnswer == null || userAnswer.trim().isEmpty()) {
            return false;
        }
        
        if (correctAnswersJson == null || correctAnswersJson.trim().isEmpty()) {
            return false;
        }
        
        // Análisis básico para diferentes tipos de preguntas
        switch (questionType) {
            case TRUE_FALSE:
                return correctAnswersJson.toLowerCase().trim().equals(userAnswer.toLowerCase().trim());
                
            case SINGLE_CHOICE:
                return correctAnswersJson.trim().equals(userAnswer.trim());
                
            case MULTIPLE_CHOICE:
                // Para multiple choice, el JSON puede contener múltiples respuestas
                // Implementación simplificada - en producción usaría JSON parser
                return correctAnswersJson.contains(userAnswer.trim());
                
            case TEXT_INPUT:
                // Comparación flexible para texto
                String correctText = correctAnswersJson.toLowerCase().trim();
                String userText = userAnswer.toLowerCase().trim();
                return correctText.equals(userText) || 
                       correctText.contains(userText) || 
                       userText.contains(correctText);
                
            case CODE_SNIPPET:
                // Para código, comparación más estricta pero eliminando espacios extra
                return normalizeCode(correctAnswersJson).equals(normalizeCode(userAnswer));
                
            default:
                return false;
        }
    }
    
    /**
     * Normaliza código para comparación
     */
    private String normalizeCode(String code) {
        if (code == null) return "";
        return code.trim()
                  .replaceAll("\\s+", " ")  // Multiple spaces to single space
                  .replaceAll("\\s*([{}();,])\\s*", "$1"); // Remove spaces around punctuation
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
