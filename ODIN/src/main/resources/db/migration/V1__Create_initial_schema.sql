-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    keycloak_id VARCHAR(255) UNIQUE,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create skill_categories table
CREATE TABLE skill_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    color_code VARCHAR(7),
    display_order INTEGER,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create skills table
CREATE TABLE skills (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    skill_category_id BIGINT NOT NULL,
    level VARCHAR(20) NOT NULL,
    difficulty_score INTEGER CHECK (difficulty_score >= 1 AND difficulty_score <= 10),
    estimated_hours INTEGER,
    icon_url VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (skill_category_id) REFERENCES skill_categories(id)
);

-- Create modules table
CREATE TABLE modules (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    skill_id BIGINT NOT NULL,
    module_order INTEGER NOT NULL,
    estimated_hours INTEGER,
    difficulty_level VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (skill_id) REFERENCES skills(id)
);

-- Create lessons table (metadata only, content stored in MongoDB)
CREATE TABLE lessons (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    module_id BIGINT NOT NULL,
    lesson_order INTEGER NOT NULL,
    lesson_type VARCHAR(20) NOT NULL,
    estimated_minutes INTEGER,
    mongo_content_id VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (module_id) REFERENCES modules(id)
);

-- Create module_prerequisites table
CREATE TABLE module_prerequisites (
    module_id BIGINT NOT NULL,
    prerequisite_module_id BIGINT NOT NULL,
    PRIMARY KEY (module_id, prerequisite_module_id),
    FOREIGN KEY (module_id) REFERENCES modules(id),
    FOREIGN KEY (prerequisite_module_id) REFERENCES modules(id)
);

-- Create indexes for better performance
CREATE INDEX idx_users_keycloak_id ON users(keycloak_id);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_skills_category ON skills(skill_category_id);
CREATE INDEX idx_skills_level ON skills(level);
CREATE INDEX idx_modules_skill ON modules(skill_id);
CREATE INDEX idx_modules_order ON modules(skill_id, module_order);
CREATE INDEX idx_lessons_module ON lessons(module_id);
CREATE INDEX idx_lessons_order ON lessons(module_id, lesson_order);

-- Insert initial data
INSERT INTO skill_categories (name, description, color_code, display_order) VALUES
('Java Fundamentals', 'Core Java programming concepts and syntax', '#FF6B35', 1),
('Object-Oriented Programming', 'OOP principles and design patterns', '#F7931E', 2),
('Data Structures', 'Fundamental data structures and algorithms', '#FFD23F', 3),
('Spring Framework', 'Spring Boot, Spring Security, Spring Data', '#06D6A0', 4),
('Database Development', 'SQL, JPA, and database design', '#118AB2', 5),
('Testing', 'Unit testing, integration testing, TDD', '#073B4C', 6),
('Web Development', 'REST APIs, microservices, web technologies', '#EF476F', 7),
('DevOps', 'CI/CD, Docker, cloud deployment', '#7209B7', 8);

INSERT INTO skills (name, description, skill_category_id, level, difficulty_score, estimated_hours) VALUES
('Java Syntax', 'Basic Java syntax, variables, operators, control structures', 1, 'BEGINNER', 2, 15),
('Classes and Objects', 'Understanding classes, objects, methods, and constructors', 2, 'BEGINNER', 3, 20),
('Inheritance', 'Inheritance, polymorphism, and method overriding', 2, 'INTERMEDIATE', 5, 25),
('Collections Framework', 'Lists, Sets, Maps, and iterators', 3, 'INTERMEDIATE', 6, 30),
('Spring Boot Basics', 'Creating Spring Boot applications and understanding auto-configuration', 4, 'INTERMEDIATE', 4, 35),
('JPA and Hibernate', 'Object-relational mapping and database operations', 5, 'INTERMEDIATE', 7, 40),
('Unit Testing with JUnit', 'Writing effective unit tests', 6, 'INTERMEDIATE', 5, 25),
('REST API Development', 'Building RESTful web services', 7, 'ADVANCED', 8, 45);
