-- Add missing columns to learning_activities table
ALTER TABLE learning_activities 
ADD COLUMN score_achieved INTEGER,
ADD COLUMN resource_id BIGINT,
ADD COLUMN resource_type VARCHAR(50);

-- Create indexes for new columns
CREATE INDEX idx_learning_activities_resource ON learning_activities(resource_id, resource_type);
CREATE INDEX idx_learning_activities_score ON learning_activities(score_achieved);
