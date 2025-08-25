-- Add enhanced progress tracking tables
CREATE TABLE user_skill_progress (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'NOT_STARTED',
    completion_percentage INTEGER NOT NULL DEFAULT 0 CHECK (completion_percentage >= 0 AND completion_percentage <= 100),
    total_modules INTEGER NOT NULL DEFAULT 0,
    completed_modules INTEGER NOT NULL DEFAULT 0,
    total_lessons INTEGER NOT NULL DEFAULT 0,
    completed_lessons INTEGER NOT NULL DEFAULT 0,
    total_labs INTEGER NOT NULL DEFAULT 0,
    passed_labs INTEGER NOT NULL DEFAULT 0,
    total_quizzes INTEGER NOT NULL DEFAULT 0,
    passed_quizzes INTEGER NOT NULL DEFAULT 0,
    total_points_earned INTEGER NOT NULL DEFAULT 0,
    total_points_possible INTEGER NOT NULL DEFAULT 0,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    last_activity_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (skill_id) REFERENCES skills(id),
    UNIQUE(user_id, skill_id)
);

-- Add learning activities table
CREATE TABLE learning_activities (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    activity_type VARCHAR(30) NOT NULL,
    entity_id BIGINT NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    activity_name VARCHAR(255) NOT NULL,
    description TEXT,
    points_earned INTEGER,
    time_spent_minutes INTEGER,
    metadata TEXT, -- JSON for additional data
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create indexes for progress tracking
CREATE INDEX idx_user_skill_progress_user ON user_skill_progress(user_id);
CREATE INDEX idx_user_skill_progress_skill ON user_skill_progress(skill_id);
CREATE INDEX idx_user_skill_progress_status ON user_skill_progress(status);
CREATE INDEX idx_user_skill_progress_last_activity ON user_skill_progress(last_activity_at);

CREATE INDEX idx_learning_activities_user ON learning_activities(user_id);
CREATE INDEX idx_learning_activities_type ON learning_activities(activity_type);
CREATE INDEX idx_learning_activities_entity ON learning_activities(entity_id, entity_type);
CREATE INDEX idx_learning_activities_created ON learning_activities(created_at);
