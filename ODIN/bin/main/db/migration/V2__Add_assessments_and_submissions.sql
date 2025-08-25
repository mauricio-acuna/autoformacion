-- Add labs table
CREATE TABLE labs (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    module_id BIGINT NOT NULL,
    github_template_url VARCHAR(500) NOT NULL,
    starter_code TEXT,
    instructions TEXT NOT NULL,
    max_attempts INTEGER NOT NULL DEFAULT 5,
    time_limit_hours INTEGER,
    points INTEGER NOT NULL DEFAULT 100,
    active BOOLEAN NOT NULL DEFAULT true,
    evaluation_type VARCHAR(20) NOT NULL DEFAULT 'AUTOMATED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (module_id) REFERENCES modules(id)
);

-- Add quizzes table
CREATE TABLE quizzes (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    module_id BIGINT NOT NULL,
    time_limit_minutes INTEGER,
    max_attempts INTEGER NOT NULL DEFAULT 3,
    passing_score INTEGER NOT NULL DEFAULT 70,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (module_id) REFERENCES modules(id)
);

-- Add quiz_questions table
CREATE TABLE quiz_questions (
    id BIGSERIAL PRIMARY KEY,
    quiz_id BIGINT NOT NULL,
    question_text TEXT NOT NULL,
    question_type VARCHAR(20) NOT NULL,
    question_order INTEGER NOT NULL,
    points INTEGER NOT NULL DEFAULT 1,
    explanation TEXT,
    options_json TEXT,
    correct_answers_json TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);

-- Add lab_submissions table
CREATE TABLE lab_submissions (
    id BIGSERIAL PRIMARY KEY,
    lab_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    github_repo_url VARCHAR(500) NOT NULL,
    commit_hash VARCHAR(40),
    branch_name VARCHAR(100) DEFAULT 'main',
    status VARCHAR(20) NOT NULL DEFAULT 'SUBMITTED',
    automated_score INTEGER CHECK (automated_score >= 0 AND automated_score <= 100),
    manual_score INTEGER CHECK (manual_score >= 0 AND manual_score <= 100),
    final_score INTEGER CHECK (final_score >= 0 AND final_score <= 100),
    test_results TEXT,
    instructor_feedback TEXT,
    execution_logs TEXT,
    attempt_number INTEGER NOT NULL DEFAULT 1,
    submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    evaluated_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (lab_id) REFERENCES labs(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create indexes for better performance
CREATE INDEX idx_labs_module ON labs(module_id);
CREATE INDEX idx_labs_evaluation_type ON labs(evaluation_type);
CREATE INDEX idx_quizzes_module ON quizzes(module_id);
CREATE INDEX idx_quiz_questions_quiz ON quiz_questions(quiz_id);
CREATE INDEX idx_quiz_questions_order ON quiz_questions(quiz_id, question_order);
CREATE INDEX idx_lab_submissions_lab_user ON lab_submissions(lab_id, user_id);
CREATE INDEX idx_lab_submissions_status ON lab_submissions(status);
CREATE INDEX idx_lab_submissions_submitted_at ON lab_submissions(submitted_at);

-- Insert sample labs data
INSERT INTO labs (title, description, module_id, github_template_url, instructions, evaluation_type, points) VALUES
('Java Variables Lab', 'Práctica con variables y tipos de datos en Java', 1, 'https://github.com/pluto-learning/java-variables-template', 'Completa los ejercicios sobre variables, tipos primitivos y operadores básicos en Java.', 'AUTOMATED', 50),
('OOP Classes Lab', 'Implementación de clases y objetos', 2, 'https://github.com/pluto-learning/java-oop-template', 'Crea una jerarquía de clases que demuestre conceptos de herencia y polimorfismo.', 'HYBRID', 100),
('Collections Framework Lab', 'Trabajo con listas, sets y maps', 3, 'https://github.com/pluto-learning/java-collections-template', 'Implementa algoritmos utilizando diferentes estructuras de datos de Java Collections.', 'AUTOMATED', 75),
('Spring Boot REST API Lab', 'Desarrollo de API REST con Spring Boot', 4, 'https://github.com/pluto-learning/spring-rest-template', 'Construye una API REST completa con operaciones CRUD y validación.', 'MANUAL', 150);

-- Insert sample quizzes data
INSERT INTO quizzes (title, description, module_id, time_limit_minutes, passing_score) VALUES
('Java Syntax Quiz', 'Evaluación de conocimientos básicos de sintaxis Java', 1, 30, 70),
('OOP Fundamentals Quiz', 'Quiz sobre conceptos de programación orientada a objetos', 2, 45, 75),
('Collections API Quiz', 'Evaluación del manejo de Collections Framework', 3, 40, 70),
('Spring Boot Concepts Quiz', 'Quiz sobre fundamentos de Spring Boot', 4, 60, 80);

-- Insert sample quiz questions
INSERT INTO quiz_questions (quiz_id, question_text, question_type, question_order, points, options_json, correct_answers_json) VALUES
(1, '¿Cuál es el tipo de dato primitivo para números enteros en Java?', 'SINGLE_CHOICE', 1, 5, '["int", "Integer", "string", "float"]', '["int"]'),
(1, '¿Qué palabra clave se usa para declarar una constante en Java?', 'SINGLE_CHOICE', 2, 5, '["const", "final", "static", "immutable"]', '["final"]'),
(2, '¿Cuáles son los pilares de la programación orientada a objetos?', 'MULTIPLE_CHOICE', 1, 10, '["Encapsulación", "Herencia", "Polimorfismo", "Abstracción", "Recursión"]', '["Encapsulación", "Herencia", "Polimorfismo", "Abstracción"]'),
(3, '¿Qué interfaz implementa ArrayList?', 'SINGLE_CHOICE', 1, 8, '["List", "Set", "Map", "Collection"]', '["List"]'),
(4, '¿Spring Boot usa configuración por convención?', 'TRUE_FALSE', 1, 5, '["Verdadero", "Falso"]', '["Verdadero"]');
