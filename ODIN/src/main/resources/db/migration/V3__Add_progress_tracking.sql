-- Add user_progress table
CREATE TABLE user_progress (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    current_module_id BIGINT,
    completion_percentage INTEGER NOT NULL DEFAULT 0 CHECK (completion_percentage >= 0 AND completion_percentage <= 100),
    mastery_score INTEGER NOT NULL DEFAULT 0 CHECK (mastery_score >= 0 AND mastery_score <= 100),
    total_points_earned INTEGER NOT NULL DEFAULT 0 CHECK (total_points_earned >= 0),
    total_time_spent_hours INTEGER NOT NULL DEFAULT 0 CHECK (total_time_spent_hours >= 0),
    status VARCHAR(20) NOT NULL DEFAULT 'NOT_STARTED',
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    last_activity_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (skill_id) REFERENCES skills(id),
    FOREIGN KEY (current_module_id) REFERENCES modules(id),
    UNIQUE(user_id, skill_id)
);

-- Add learning_activities table
CREATE TABLE learning_activities (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    activity_type VARCHAR(50) NOT NULL,
    resource_id BIGINT NOT NULL,
    resource_type VARCHAR(50) NOT NULL,
    points_earned INTEGER NOT NULL DEFAULT 0 CHECK (points_earned >= 0),
    time_spent_minutes INTEGER CHECK (time_spent_minutes >= 0),
    score_achieved INTEGER CHECK (score_achieved >= 0),
    metadata TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create indexes for better performance
CREATE INDEX idx_user_progress_user ON user_progress(user_id);
CREATE INDEX idx_user_progress_skill ON user_progress(skill_id);
CREATE INDEX idx_user_progress_status ON user_progress(status);
CREATE INDEX idx_user_progress_last_activity ON user_progress(last_activity_at);
CREATE INDEX idx_user_progress_completion ON user_progress(completion_percentage);

CREATE INDEX idx_learning_activities_user ON learning_activities(user_id);
CREATE INDEX idx_learning_activities_type ON learning_activities(activity_type);
CREATE INDEX idx_learning_activities_resource ON learning_activities(resource_type, resource_id);
CREATE INDEX idx_learning_activities_created_at ON learning_activities(created_at);
CREATE INDEX idx_learning_activities_user_date ON learning_activities(user_id, created_at);

-- Insert sample progress data
INSERT INTO user_progress (user_id, skill_id, completion_percentage, mastery_score, total_points_earned, total_time_spent_hours, status, started_at, last_activity_at) VALUES
(1, 1, 85, 78, 125, 8, 'IN_PROGRESS', '2024-01-15 10:00:00', '2024-01-20 14:30:00'),
(1, 2, 100, 92, 200, 12, 'MASTERED', '2024-01-10 09:00:00', '2024-01-18 16:45:00'),
(2, 1, 45, 65, 75, 5, 'IN_PROGRESS', '2024-01-18 11:00:00', '2024-01-21 13:15:00'),
(2, 3, 100, 88, 180, 10, 'COMPLETED', '2024-01-12 14:00:00', '2024-01-19 17:20:00');

-- Insert sample learning activities
INSERT INTO learning_activities (user_id, activity_type, resource_id, resource_type, points_earned, time_spent_minutes, score_achieved, metadata) VALUES
(1, 'LESSON_COMPLETED', 1, 'LESSON', 10, 30, 85, '{"difficulty": "beginner", "topic": "variables"}'),
(1, 'QUIZ_PASSED', 1, 'QUIZ', 25, 15, 78, '{"questions": 10, "correct": 8}'),
(1, 'LAB_SUBMITTED', 1, 'LAB', 50, 120, 92, '{"github_url": "https://github.com/user/java-lab-1", "tests_passed": 18, "tests_total": 20}'),
(2, 'LESSON_COMPLETED', 2, 'LESSON', 10, 25, 90, '{"difficulty": "intermediate", "topic": "classes"}'),
(2, 'QUIZ_PASSED', 2, 'QUIZ', 30, 20, 85, '{"questions": 12, "correct": 10}'),
(1, 'MODULE_COMPLETED', 1, 'MODULE', 100, 180, 85, '{"total_lessons": 5, "total_quizzes": 2, "total_labs": 1}'),
(2, 'LAB_SUBMITTED', 2, 'LAB', 75, 150, 88, '{"github_url": "https://github.com/user/oop-lab", "tests_passed": 22, "tests_total": 25}');
