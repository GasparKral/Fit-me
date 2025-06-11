-- ============================================================================
-- FITNESS APPLICATION DATABASE INITIALIZATION SCRIPT
-- ============================================================================

-- Create database (uncomment if needed)
-- CREATE DATABASE fitness_app;
-- \c fitness_app;

-- ============================================================================
-- ENUMS CREATION
-- ============================================================================

-- Status State Enum
CREATE TYPE status_state_enum AS ENUM (
    'ACTIVE',
    'INACTIVE',
    'SUSPENDED',
    'PENDING'
);

-- Sex Enum
CREATE TYPE sex AS ENUM (
    'MALE',
    'FEMALE'
);

-- Week Day Enum
CREATE TYPE week_day AS ENUM (
    'MONDAY',
    'TUESDAY',
    'WEDNESDAY',
    'THURSDAY',
    'FRIDAY',
    'SATURDAY',
    'SUNDAY'
);

-- Body Part Enum
CREATE TYPE body_part AS ENUM (
    'CHEST',
    'BACK',
    'SHOULDER',
    'ARM',
    'LEG',
    'CORE',
    'FULL_BODY'
);

-- Difficulty Enum
CREATE TYPE difficulty AS ENUM (
    'EASY',
    'ADVANCE',
    'HARD'
);

-- Workout Type Enum
CREATE TYPE workout_type AS ENUM (
    'STRENGTH',
    'CARDIO',
    'FULL_BODY',
    'UPPER_BODY',
    'LOWER_BODY',
    'CORE',
    'FLEXIBILITY'
);

-- Diet Type Enum
CREATE TYPE diet_type AS ENUM (
    'WEIGHT_LOSS',
    'MUSCLE_GAIN',
    'BALANCED',
    'VEGETARIAN',
    'VEGAN',
    'LOW_CARB',
    'PERFORMANCE'
);

-- Meal Type Enum
CREATE TYPE meal_type AS ENUM (
    'BREAKFAST',
    'LUNCH',
    'DINNER',
    'SNACK',
    'PRE_WORKOUT',
    'POST_WORKOUT'
);

-- Message Type Enum
CREATE TYPE message_type_enum AS ENUM (
    'TEXT',
    'IMAGE',
    'VIDEO',
    'AUDIO',
    'FILE',
    'SYSTEM'
);

-- Message Status Enum
CREATE TYPE message_status_enum AS ENUM (
    'SENT',
    'DELIVERED',
    'READ',
    'FAILED'
);

-- Session Type Enum
CREATE TYPE sessionType AS ENUM (
    'TRAINING',
    'CONSULTATION',
    'ASSESSMENT',
    'FOLLOW_UP'
);

-- ============================================================================
-- USER TABLES
-- ============================================================================

-- Users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    fullname VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(50) NOT NULL,
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_image_url VARCHAR(500)
);

-- Create unique index on email
CREATE UNIQUE INDEX idx_users_email ON users(email);

-- User Status table
CREATE TABLE user_status (
    id SERIAL PRIMARY KEY,
    state status_state_enum NOT NULL,
    last_time_active TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    is_online BOOLEAN DEFAULT FALSE,
    last_seen_at TIMESTAMP
);

-- Trainers table
CREATE TABLE trainers (
    id SERIAL PRIMARY KEY,
    specialization VARCHAR(255) NOT NULL,
    years_of_experience INTEGER NOT NULL,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

-- Athletes table
CREATE TABLE athletes (
    id SERIAL PRIMARY KEY,
    age INTEGER NOT NULL,
    sex sex NOT NULL,
    training_since TIMESTAMP NOT NULL,
    workout_id INTEGER,
    diet_id INTEGER,
    trainer_id INTEGER REFERENCES trainers(id),
    need_assistant BOOLEAN NOT NULL,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

-- ============================================================================
-- USER INFORMATION TABLES
-- ============================================================================

-- Allergies table
CREATE TABLE allergies (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Athlete Allergies junction table
CREATE TABLE athlete_allergies (
    athlete_id INTEGER NOT NULL REFERENCES athletes(id) ON DELETE CASCADE,
    allergy_id INTEGER NOT NULL REFERENCES allergies(id) ON DELETE CASCADE,
    PRIMARY KEY (athlete_id, allergy_id)
);

-- Measurements table
CREATE TABLE measurements (
    id SERIAL PRIMARY KEY,
    athlete_id INTEGER NOT NULL REFERENCES athletes(id) ON DELETE CASCADE,
    weight DOUBLE PRECISION DEFAULT 0.0,
    height DOUBLE PRECISION DEFAULT 0.0,
    body_fat DOUBLE PRECISION DEFAULT 0.0,
    arm_size DOUBLE PRECISION DEFAULT 0.0,
    chest_back_size DOUBLE PRECISION DEFAULT 0.0,
    hip_size DOUBLE PRECISION DEFAULT 0.0,
    leg_size DOUBLE PRECISION DEFAULT 0.0,
    calves_size DOUBLE PRECISION DEFAULT 0.0,
    measure_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_current BOOLEAN DEFAULT TRUE
);

-- Certifications table
CREATE TABLE certifications (
    id SERIAL PRIMARY KEY,
    trainer_id INTEGER NOT NULL REFERENCES trainers(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    issuing_organization VARCHAR(255) NOT NULL,
    complete_at TIMESTAMP NOT NULL
);

-- Trainer Socials table
CREATE TABLE trainer_socials (
    id SERIAL PRIMARY KEY,
    trainer_id INTEGER NOT NULL REFERENCES trainers(id) ON DELETE CASCADE,
    platform VARCHAR(50) NOT NULL,
    url VARCHAR(500) NOT NULL
);

-- Create unique index for trainer socials
CREATE UNIQUE INDEX idx_trainer_socials_unique ON trainer_socials(trainer_id, platform);

-- Trainer Availability table
CREATE TABLE trainer_availability (
    id SERIAL PRIMARY KEY,
    trainer_id INTEGER NOT NULL REFERENCES trainers(id) ON DELETE CASCADE,
    week_day week_day NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL
);

-- ============================================================================
-- EXERCISES AND WORKOUTS TABLES
-- ============================================================================

-- Exercises table
CREATE TABLE exercises (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    body_part body_part NOT NULL
);

-- Workouts table
CREATE TABLE workouts (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    difficulty difficulty DEFAULT 'EASY',
    duration_minutes BIGINT NOT NULL,
    workout_type workout_type NOT NULL,
    created_by INTEGER NOT NULL REFERENCES trainers(id),
    start_up TIMESTAMP NOT NULL
);

-- Workout Exercises table
CREATE TABLE workout_exercises (
    id SERIAL PRIMARY KEY,
    workout_id INTEGER NOT NULL REFERENCES workouts(id) ON DELETE CASCADE,
    exercise_id INTEGER NOT NULL REFERENCES exercises(id) ON DELETE CASCADE,
    week_day week_day NOT NULL,
    reps INTEGER NOT NULL,
    sets INTEGER NOT NULL,
    is_optional BOOLEAN DEFAULT FALSE,
    parent_exercise_id INTEGER REFERENCES workout_exercises(id)
);

-- Workout Templates table
CREATE TABLE workout_templates (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    difficulty difficulty DEFAULT 'EASY',
    workout_type workout_type NOT NULL,
    created_by INTEGER NOT NULL REFERENCES trainers(id)
);

-- Workout Template Exercises table
CREATE TABLE workout_template_exercises (
    id SERIAL PRIMARY KEY,
    template_id INTEGER NOT NULL REFERENCES workout_templates(id) ON DELETE CASCADE,
    exercise_id INTEGER NOT NULL REFERENCES exercises(id) ON DELETE CASCADE,
    week_day week_day NOT NULL,
    reps INTEGER NOT NULL,
    sets INTEGER NOT NULL,
    is_optional BOOLEAN DEFAULT FALSE,
    parent_exercise_id INTEGER REFERENCES workout_exercises(id)
);

-- Completion Workout Statistics table
CREATE TABLE completion_workout_statistics (
    id SERIAL PRIMARY KEY,
    workout_id INTEGER NOT NULL REFERENCES workouts(id) ON DELETE CASCADE,
    athlete_id INTEGER NOT NULL REFERENCES athletes(id) ON DELETE CASCADE,
    complete_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- DIETS AND MEALS TABLES
-- ============================================================================

-- Dishes table
CREATE TABLE dishes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Diets table
CREATE TABLE diets (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    diet_type diet_type NOT NULL,
    duration_days BIGINT NOT NULL,
    created_by INTEGER NOT NULL REFERENCES trainers(id),
    start_at TIMESTAMP NOT NULL
);

-- Diet Dishes table
CREATE TABLE diet_dishes (
    id SERIAL PRIMARY KEY,
    diet_id INTEGER NOT NULL REFERENCES diets(id) ON DELETE CASCADE,
    dish_id INTEGER NOT NULL REFERENCES dishes(id) ON DELETE CASCADE,
    week_day week_day NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    meal_type meal_type NOT NULL
);

-- Diet Templates table
CREATE TABLE diet_templates (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    diet_type diet_type NOT NULL,
    created_by INTEGER NOT NULL REFERENCES trainers(id)
);

-- Diet Template Dishes table
CREATE TABLE diet_template_dishes (
    id SERIAL PRIMARY KEY,
    template_id INTEGER NOT NULL REFERENCES diet_templates(id) ON DELETE CASCADE,
    dish_id INTEGER NOT NULL REFERENCES dishes(id) ON DELETE CASCADE,
    week_day week_day NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    meal_type meal_type NOT NULL
);

-- Completion Diet Statistics table
CREATE TABLE completion_diet_statistics (
    id SERIAL PRIMARY KEY,
    diet_id INTEGER NOT NULL REFERENCES diets(id) ON DELETE CASCADE,
    athlete_id INTEGER NOT NULL REFERENCES athletes(id) ON DELETE CASCADE,
    complete_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- COMMUNICATION TABLES
-- ============================================================================

-- Conversations table
CREATE TABLE conversations (
    id SERIAL PRIMARY KEY,
    trainer_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    athlete_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    last_activity_at TIMESTAMP
);

-- Create unique index for conversations
CREATE UNIQUE INDEX idx_conversations_unique ON conversations(trainer_id, athlete_id);

-- Conversation Users table
CREATE TABLE conversation_users (
    id SERIAL PRIMARY KEY,
    conversation_id INTEGER NOT NULL REFERENCES conversations(id) ON DELETE CASCADE,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    last_read_message_id VARCHAR(36),
    unread_count INTEGER DEFAULT 0,
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    left_at TIMESTAMP,
    is_muted BOOLEAN DEFAULT FALSE,
    is_archived BOOLEAN DEFAULT FALSE
);

-- Create unique index for conversation users
CREATE UNIQUE INDEX idx_conversation_users_unique ON conversation_users(conversation_id, user_id);

-- Messages table
CREATE TABLE messages (
    id SERIAL PRIMARY KEY,
    message_id VARCHAR(36) UNIQUE NOT NULL,
    conversation_id INTEGER NOT NULL REFERENCES conversations(id) ON DELETE CASCADE,
    sender_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    receiver_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    sender_name VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    message_type message_type_enum DEFAULT 'TEXT',
    sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    delivered_at TIMESTAMP,
    read_at TIMESTAMP,
    status message_status_enum DEFAULT 'SENT',
    edited_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    metadata TEXT
);

-- Create unique index for message_id
CREATE UNIQUE INDEX idx_messages_message_id ON messages(message_id);

-- Active Sessions table
CREATE TABLE active_sessions (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    conversation_id INTEGER NOT NULL REFERENCES conversations(id) ON DELETE CASCADE,
    session_id VARCHAR(36) NOT NULL,
    connected_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_ping_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_agent VARCHAR(500),
    ip_address VARCHAR(45)
);

-- Create unique index for active sessions
CREATE UNIQUE INDEX idx_active_sessions_unique ON active_sessions(user_id, conversation_id, session_id);

-- Sessions table
CREATE TABLE sessions (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    date_time TIMESTAMP NOT NULL,
    session_type sessionType NOT NULL,
    trainer_id INTEGER NOT NULL REFERENCES trainers(id) ON DELETE CASCADE,
    athlete_id INTEGER NOT NULL REFERENCES athletes(id) ON DELETE CASCADE,
    expected_duration_minutes BIGINT NOT NULL,
    actual_duration_minutes BIGINT,
    completed BOOLEAN DEFAULT FALSE
);

CREATE TABLE strength_statistics (
    id SERIAL PRIMARY KEY,
    athlete_id INTEGER NOT NULL,
    recorded_at TIMESTAMP NOT NULL,
    bench_press_max DOUBLE PRECISION, -- Peso máximo en press de banca (kg)
    squat_max DOUBLE PRECISION, -- Peso máximo en sentadilla (kg)
    deadlift_max DOUBLE PRECISION, -- Peso máximo en peso muerto (kg)
    pull_ups_max INTEGER, -- Máximo de dominadas
    push_ups_max INTEGER, -- Máximo de flexiones
    strength_index DOUBLE PRECISION NOT NULL DEFAULT 0.0, -- Índice de fuerza calculado
    muscular_endurance DOUBLE PRECISION NOT NULL DEFAULT 0.0, -- Resistencia muscular
    power_output DOUBLE PRECISION, -- Potencia de salida (watts)

    FOREIGN KEY (athlete_id) REFERENCES athletes(id)
);

-- Tabla de estadísticas de resistencia
CREATE TABLE endurance_statistics (
    id SERIAL PRIMARY KEY,
    athlete_id INTEGER NOT NULL,
    recorded_at TIMESTAMP NOT NULL,
    vo2_max DOUBLE PRECISION, -- VO2 máximo (ml/kg/min)
    resting_heart_rate INTEGER, -- Frecuencia cardíaca en reposo
    max_heart_rate INTEGER, -- Frecuencia cardíaca máxima
    running_pace_min_km DOUBLE PRECISION, -- Ritmo de carrera (min/km)
    cardio_endurance DOUBLE PRECISION NOT NULL DEFAULT 0.0, -- Resistencia cardiovascular (0-100)
    aerobic_capacity DOUBLE PRECISION NOT NULL DEFAULT 0.0, -- Capacidad aeróbica
    recovery_time_minutes DOUBLE PRECISION, -- Tiempo de recuperación
    distance_covered_km DOUBLE PRECISION NOT NULL DEFAULT 0.0, -- Distancia cubierta en km

    FOREIGN KEY (athlete_id) REFERENCES athletes(id)
);

-- Tabla de historial de mediciones corporales
CREATE TABLE body_measurement_history (
    id SERIAL PRIMARY KEY,
    athlete_id INTEGER NOT NULL,
    measurement_id INTEGER NOT NULL,
    recorded_at TIMESTAMP NOT NULL,
    weight_change DOUBLE PRECISION NOT NULL DEFAULT 0.0, -- Cambio de peso respecto a medición anterior
    body_fat_change DOUBLE PRECISION NOT NULL DEFAULT 0.0, -- Cambio de grasa corporal
    muscle_mass_gain DOUBLE PRECISION NOT NULL DEFAULT 0.0, -- Ganancia de masa muscular
    bmi DOUBLE PRECISION NOT NULL DEFAULT 0.0, -- Índice de masa corporal
    body_composition DOUBLE PRECISION NOT NULL DEFAULT 0.0, -- Composición corporal general

    FOREIGN KEY (athlete_id) REFERENCES athletes(id),
    FOREIGN KEY (measurement_id) REFERENCES measurements(id)
);

-- ============================================================================
-- ACCESS KEYS TABLE
-- ============================================================================

-- Registration Key Table
CREATE TABLE key_gen (
    id SERIAL PRIMARY KEY,
    key VARCHAR(250) NOT NULL,
    trainer_id INTEGER NOT NULL REFERENCES trainers(id) ON DELETE CASCADE
);

-- ============================================================================
-- ADD FOREIGN KEY CONSTRAINTS THAT WERE DEFERRED
-- ============================================================================

-- Add foreign key constraints for athletes table that reference tables created later
ALTER TABLE athletes
ADD CONSTRAINT fk_athletes_workout
FOREIGN KEY (workout_id) REFERENCES workouts(id);

ALTER TABLE athletes
ADD CONSTRAINT fk_athletes_diet
FOREIGN KEY (diet_id) REFERENCES diets(id);

-- ============================================================================
-- USEFUL INDEXES FOR PERFORMANCE
-- ============================================================================

-- User related indexes
CREATE INDEX idx_user_status_user_id ON user_status(user_id);
CREATE INDEX idx_trainers_user_id ON trainers(user_id);
CREATE INDEX idx_athletes_user_id ON athletes(user_id);
CREATE INDEX idx_athletes_trainer_id ON athletes(trainer_id);

-- Workout related indexes
CREATE INDEX idx_workout_exercises_workout_id ON workout_exercises(workout_id);
CREATE INDEX idx_workout_exercises_exercise_id ON workout_exercises(exercise_id);
CREATE INDEX idx_workouts_created_by ON workouts(created_by);

-- Diet related indexes
CREATE INDEX idx_diet_dishes_diet_id ON diet_dishes(diet_id);
CREATE INDEX idx_diet_dishes_dish_id ON diet_dishes(dish_id);
CREATE INDEX idx_diets_created_by ON diets(created_by);

-- Communication related indexes
CREATE INDEX idx_messages_conversation_id ON messages(conversation_id);
CREATE INDEX idx_messages_sender_id ON messages(sender_id);
CREATE INDEX idx_messages_sent_at ON messages(sent_at);
CREATE INDEX idx_conversations_trainer_id ON conversations(trainer_id);
CREATE INDEX idx_conversations_athlete_id ON conversations(athlete_id);

-- Session related indexes
CREATE INDEX idx_sessions_trainer_id ON sessions(trainer_id);
CREATE INDEX idx_sessions_athlete_id ON sessions(athlete_id);
CREATE INDEX idx_sessions_date_time ON sessions(date_time);

-- Statistics indexes
CREATE INDEX idx_completion_workout_stats_workout_id ON completion_workout_statistics(workout_id);
CREATE INDEX idx_completion_workout_stats_athlete_id ON completion_workout_statistics(athlete_id);
CREATE INDEX idx_completion_diet_stats_diet_id ON completion_diet_statistics(diet_id);
CREATE INDEX idx_completion_diet_stats_athlete_id ON completion_diet_statistics(athlete_id);

CREATE INDEX idx_strength_statistics_athlete_id ON strength_statistics(athlete_id);
CREATE INDEX idx_strength_statistics_recorded_at ON strength_statistics(recorded_at);

CREATE INDEX idx_endurance_statistics_athlete_id ON endurance_statistics(athlete_id);
CREATE INDEX idx_endurance_statistics_recorded_at ON endurance_statistics(recorded_at);

CREATE INDEX idx_body_measurement_history_athlete_id ON body_measurement_history(athlete_id);
CREATE INDEX idx_body_measurement_history_measurement_id ON body_measurement_history(measurement_id);
CREATE INDEX idx_body_measurement_history_recorded_at ON body_measurement_history(recorded_at);