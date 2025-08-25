package com.pluto.learning.assessment;

import com.pluto.learning.auth.User;
import com.pluto.learning.assessments.Quiz;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

/**
 * Resultado de evaluación de quiz
 */
public class QuizAssessmentResult {
    private final User user;
    private final Quiz quiz;
    private final LocalDateTime completedAt;
    private final Map<Long, QuestionResult> questionResults;
    private final Map<String, String> analysisNotes;
    
    private int score;
    private boolean passed;
    private String feedback;
    
    public QuizAssessmentResult(User user, Quiz quiz) {
        this.user = user;
        this.quiz = quiz;
        this.completedAt = LocalDateTime.now();
        this.questionResults = new HashMap<>();
        this.analysisNotes = new HashMap<>();
    }
    
    public void addQuestionResult(Long questionId, String userAnswer, boolean correct) {
        questionResults.put(questionId, new QuestionResult(userAnswer, correct));
    }
    
    public void addAnalysisNote(String category, String note) {
        analysisNotes.put(category, note);
    }
    
    // Getters and Setters
    public User getUser() { return user; }
    public Quiz getQuiz() { return quiz; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public Map<Long, QuestionResult> getQuestionResults() { return questionResults; }
    public Map<String, String> getAnalysisNotes() { return analysisNotes; }
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }
    
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    
    /**
     * Resultado individual de pregunta
     */
    public static class QuestionResult {
        private final String userAnswer;
        private final boolean correct;
        private final LocalDateTime answeredAt;
        
        public QuestionResult(String userAnswer, boolean correct) {
            this.userAnswer = userAnswer;
            this.correct = correct;
            this.answeredAt = LocalDateTime.now();
        }
        
        public String getUserAnswer() { return userAnswer; }
        public boolean isCorrect() { return correct; }
        public LocalDateTime getAnsweredAt() { return answeredAt; }
    }
}
