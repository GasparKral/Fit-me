-- ==================================================
-- 02-tablas.sql
-- Archivo de creación de tablas y estructuras
-- ==================================================

-- Configurar esquema de búsqueda
SET search_path TO public, fit_me;

-- ==================================================
-- TIPOS ENUM PERSONALIZADOS
-- ==================================================

-- Tipo para partes del cuerpo en ejercicios
CREATE TYPE body_part AS ENUM (
    'LEG', 'ARM', 'SHOULDER', 'CORE', 'BACK', 'CHEST','FULL_BODY'
);

-- Tipo para tipos de medios/recursos
CREATE TYPE media_type AS ENUM (
    'IMAGE', 'AUDIO', 'VIDEO'
);

-- Tipo para tipos de mensajes
CREATE TYPE message_type AS ENUM (
    'MESSAGE', 'REPLY', 'MULTIMEDIA'
);

-- Tipo para días de la semana
CREATE TYPE week_day AS ENUM (
    'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'
);

-- ==================================================
-- TABLAS PRINCIPALES
-- ==================================================

-- Tabla de usuarios (base común para trainers y sportsmen)
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    creation_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status BOOLEAN NOT NULL DEFAULT false,
    last_active TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    needs_attention BOOLEAN NOT NULL DEFAULT false,
    password VARCHAR(200) NOT NULL
);

-- Tabla de entrenadores
CREATE TABLE trainers (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    years_of_experience INTEGER NOT NULL,
    bio TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tabla de deportistas
CREATE TABLE sportsmen (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    trainer_id INTEGER,
    age INTEGER NOT NULL,
    sex BOOLEAN NOT NULL,
    training_since TIMESTAMP WITHOUT TIME ZONE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (trainer_id) REFERENCES trainers(id) ON DELETE SET NULL
);

-- Tabla de recursos multimedia
CREATE TABLE resources (
    id SERIAL PRIMARY KEY,
    media_type media_type NOT NULL,
    src VARCHAR(255) NOT NULL,
    user_id INTEGER,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Tabla de notas generales
CREATE TABLE notes (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    message TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ==================================================
-- TABLAS DE EJERCICIOS Y ENTRENAMIENTOS
-- ==================================================

-- Tabla base de ejercicios
CREATE TABLE exercise_bases (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    body_part body_part NOT NULL,
    description TEXT NOT NULL,
    author_id INTEGER,
    video_resource_id INTEGER,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (video_resource_id) REFERENCES resources(id) ON DELETE SET NULL
);

-- Tabla de entrenamientos
CREATE TABLE workouts (
    id SERIAL PRIMARY KEY,
    sportsman_id INTEGER NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    duration_millis BIGINT,
    initial_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (sportsman_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tabla de ejercicios en entrenamientos
CREATE TABLE workout_exercises (
    id SERIAL PRIMARY KEY,
    workout_id INTEGER NOT NULL,
    exercise_base_id INTEGER NOT NULL,
    day week_day NOT NULL,
    reps INTEGER NOT NULL,
    sets INTEGER NOT NULL,
    is_option BOOLEAN NOT NULL DEFAULT false,
    FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE,
    FOREIGN KEY (exercise_base_id) REFERENCES exercise_bases(id) ON DELETE CASCADE
);

-- Tabla de notas de ejercicios en entrenamientos
CREATE TABLE workout_exercise_notes (
    id SERIAL PRIMARY KEY,
    workout_exercise_id INTEGER NOT NULL,
    note_id INTEGER NOT NULL,
    FOREIGN KEY (workout_exercise_id) REFERENCES workout_exercises(id) ON DELETE CASCADE,
    FOREIGN KEY (note_id) REFERENCES notes(id) ON DELETE CASCADE
);

-- Tabla de notas de entrenamientos
CREATE TABLE workouts_notes (
    id SERIAL PRIMARY KEY,
    workout_id INTEGER NOT NULL,
    note_id INTEGER NOT NULL,
    FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE,
    FOREIGN KEY (note_id) REFERENCES notes(id) ON DELETE CASCADE
);

-- Tabla de entrenamientos completados
CREATE TABLE completed_workouts (
    id SERIAL PRIMARY KEY,
    workout_id INTEGER NOT NULL,
    sportsman_id INTEGER NOT NULL,
    completed_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE,
    FOREIGN KEY (sportsman_id) REFERENCES sportsmen(id) ON DELETE CASCADE
);

-- Tabla de notas de entrenamientos completados
CREATE TABLE completed_workout_notes (
    id SERIAL PRIMARY KEY,
    completed_workout_id INTEGER NOT NULL,
    FOREIGN KEY (completed_workout_id) REFERENCES completed_workouts(id) ON DELETE CASCADE
);

-- Tabla de alternativas de ejercicios
CREATE TABLE exercise_alternatives (
    id SERIAL PRIMARY KEY,
    main_exercise_id INTEGER NOT NULL,
    alternative_exercise_id INTEGER NOT NULL,
    FOREIGN KEY (main_exercise_id) REFERENCES workout_exercises(id) ON DELETE CASCADE,
    FOREIGN KEY (alternative_exercise_id) REFERENCES workout_exercises(id) ON DELETE CASCADE
);

-- ==================================================
-- TABLAS DE DIETAS Y ALIMENTACIÓN
-- ==================================================

-- Tabla de platillos/comidas
CREATE TABLE dishes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Tabla de dietas
CREATE TABLE diets (
    id SERIAL PRIMARY KEY,
    sportsman_id INTEGER NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    duration_millis BIGINT NOT NULL,
    initial_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (sportsman_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tabla de comidas en dietas
CREATE TABLE diet_meals (
    id SERIAL PRIMARY KEY,
    diet_id INTEGER NOT NULL,
    day week_day NOT NULL,
    dish_id INTEGER NOT NULL,
    quantity DOUBLE PRECISION NOT NULL,
    FOREIGN KEY (diet_id) REFERENCES diets(id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dishes(id) ON DELETE CASCADE
);

-- Tabla de notas de dietas
CREATE TABLE diet_notes (
    id SERIAL PRIMARY KEY,
    diet_id INTEGER NOT NULL,
    FOREIGN KEY (diet_id) REFERENCES diets(id) ON DELETE CASCADE
);

-- Tabla intermedia de notas de dietas
CREATE TABLE diet_notes_intermediate (
    id SERIAL PRIMARY KEY,
    diet_id INTEGER NOT NULL,
    note_id INTEGER NOT NULL,
    FOREIGN KEY (diet_id) REFERENCES diets(id) ON DELETE CASCADE,
    FOREIGN KEY (note_id) REFERENCES notes(id) ON DELETE CASCADE
);

-- Tabla de dietas completadas
CREATE TABLE completed_diets (
    id SERIAL PRIMARY KEY,
    diet_id INTEGER NOT NULL,
    sportsman_id INTEGER NOT NULL,
    completed_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (diet_id) REFERENCES diets(id) ON DELETE CASCADE,
    FOREIGN KEY (sportsman_id) REFERENCES sportsmen(id) ON DELETE CASCADE
);

-- Tabla de notas de dietas completadas
CREATE TABLE completed_diet_notes (
    id SERIAL PRIMARY KEY,
    completed_diet_id INTEGER NOT NULL,
    FOREIGN KEY (completed_diet_id) REFERENCES completed_diets(id) ON DELETE CASCADE
);

-- Tabla de alternativas de platillos
CREATE TABLE dish_alternatives (
    id SERIAL PRIMARY KEY,
    dish_id INTEGER NOT NULL,
    alternative_dish_id INTEGER NOT NULL,
    FOREIGN KEY (dish_id) REFERENCES dishes(id) ON DELETE CASCADE,
    FOREIGN KEY (alternative_dish_id) REFERENCES dishes(id) ON DELETE CASCADE
);

-- ==================================================
-- TABLAS DE MEDICIONES Y SEGUIMIENTO
-- ==================================================

-- Tabla de mediciones actuales
CREATE TABLE measurements (
    id SERIAL PRIMARY KEY,
    sportsman_id INTEGER NOT NULL,
    weight DOUBLE PRECISION,
    height DOUBLE PRECISION,
    body_fat INTEGER,
    last_updated TIMESTAMP WITHOUT TIME ZONE,
    FOREIGN KEY (sportsman_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tabla de historial de mediciones
CREATE TABLE measurement_history (
    id SERIAL PRIMARY KEY,
    sportsman_id INTEGER NOT NULL,
    weight DOUBLE PRECISION,
    height DOUBLE PRECISION,
    body_fat INTEGER,
    measurement_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (sportsman_id) REFERENCES sportsmen(id) ON DELETE CASCADE
);

-- Tabla de alergias de deportistas
CREATE TABLE sportsman_allergies (
    id SERIAL PRIMARY KEY,
    sportsman_id INTEGER NOT NULL,
    allergy VARCHAR(100) NOT NULL,
    FOREIGN KEY (sportsman_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ==================================================
-- TABLAS DE COMUNICACIÓN
-- ==================================================

-- Tabla de chats
CREATE TABLE chats (
    id SERIAL PRIMARY KEY,
    trainer_id INTEGER NOT NULL,
    sportsman_id INTEGER NOT NULL,
    FOREIGN KEY (trainer_id) REFERENCES trainers(id) ON DELETE CASCADE,
    FOREIGN KEY (sportsman_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tabla de mensajes
CREATE TABLE messages (
    id SERIAL PRIMARY KEY,
    chat_id INTEGER NOT NULL,
    author_id INTEGER NOT NULL,
    message_type message_type,
    message TEXT NOT NULL,
    media_resource_id INTEGER,
    post_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (chat_id) REFERENCES chats(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (media_resource_id) REFERENCES resources(id) ON DELETE SET NULL
);

-- ==================================================
-- TABLAS DE GESTIÓN DE ENTRENADORES
-- ==================================================

-- Tabla de certificaciones de entrenadores
CREATE TABLE certifications (
    id SERIAL PRIMARY KEY,
    trainer_id INTEGER NOT NULL,
    name VARCHAR(100) NOT NULL,
    issuing_organization VARCHAR(100) NOT NULL,
    date_obtained TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (trainer_id) REFERENCES trainers(id) ON DELETE CASCADE
);

-- Tabla de enlaces sociales de entrenadores
CREATE TABLE trainer_social_links (
    id SERIAL PRIMARY KEY,
    trainer_id INTEGER NOT NULL,
    platform VARCHAR(20) NOT NULL,
    url VARCHAR(255) NOT NULL,
    FOREIGN KEY (trainer_id) REFERENCES trainers(id) ON DELETE CASCADE
);

-- Tabla de horarios disponibles
CREATE TABLE time_slots (
    id SERIAL PRIMARY KEY,
    trainer_id INTEGER NOT NULL,
    day week_day NOT NULL,
    start_time VARCHAR(8) NOT NULL,
    end_time VARCHAR(8) NOT NULL,
    FOREIGN KEY (trainer_id) REFERENCES trainers(id) ON DELETE CASCADE
);

-- Tabla de sesiones
CREATE TABLE sessions (
    id SERIAL PRIMARY KEY,
    trainer_id INTEGER NOT NULL,
    sportsman_id INTEGER NOT NULL,
    date_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    duration_millis BIGINT NOT NULL,
    FOREIGN KEY (trainer_id) REFERENCES trainers(id) ON DELETE CASCADE,
    FOREIGN KEY (sportsman_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ==================================================
-- TABLAS AUXILIARES
-- ==================================================

-- Tabla de imágenes de usuario
CREATE TABLE user_images (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    resource_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (resource_id) REFERENCES resources(id) ON DELETE CASCADE
);

-- Tabla de generación de claves
CREATE TABLE key_gen (
    id SERIAL PRIMARY KEY,
    key VARCHAR(250) NOT NULL,
    trainer_id INTEGER NOT NULL,
    FOREIGN KEY (trainer_id) REFERENCES users(id) ON DELETE CASCADE
);


-- ==================================================
-- ÍNDICES PARA OPTIMIZACIÓN
-- ==================================================

-- Índices para usuarios
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_status ON users(status);
CREATE INDEX idx_users_last_active ON users(last_active);

-- Índices para entrenadores y deportistas
CREATE INDEX idx_trainers_user_id ON trainers(user_id);
CREATE INDEX idx_sportsmen_user_id ON sportsmen(user_id);
CREATE INDEX idx_sportsmen_trainer_id ON sportsmen(trainer_id);

-- Índices para entrenamientos
CREATE INDEX idx_workouts_sportsman_id ON workouts(sportsman_id);
CREATE INDEX idx_workout_exercises_workout_id ON workout_exercises(workout_id);
CREATE INDEX idx_workout_exercises_day ON workout_exercises(day);

-- Índices para dietas
CREATE INDEX idx_diets_sportsman_id ON diets(sportsman_id);
CREATE INDEX idx_diet_meals_diet_id ON diet_meals(diet_id);
CREATE INDEX idx_diet_meals_day ON diet_meals(day);

-- Índices para comunicación
CREATE INDEX idx_chats_trainer_id ON chats(trainer_id);
CREATE INDEX idx_chats_sportsman_id ON chats(sportsman_id);
CREATE INDEX idx_messages_chat_id ON messages(chat_id);
CREATE INDEX idx_messages_post_time ON messages(post_time);

-- Índices para mediciones
CREATE INDEX idx_measurements_sportsman_id ON measurements(sportsman_id);
CREATE INDEX idx_measurement_history_sportsman_id ON measurement_history(sportsman_id);
CREATE INDEX idx_measurement_history_timestamp ON measurement_history(measurement_date);

-- ==================================================
-- COMENTARIOS DE DOCUMENTACIÓN
-- ==================================================

COMMENT ON TABLE users IS 'Tabla principal de usuarios del sistema';
COMMENT ON TABLE trainers IS 'Información específica de entrenadores';
COMMENT ON TABLE sportsmen IS 'Información específica de deportistas';
COMMENT ON TABLE workouts IS 'Rutinas de entrenamiento asignadas';
COMMENT ON TABLE diets IS 'Planes de alimentación asignados';
COMMENT ON TABLE exercise_bases IS 'Catálogo de ejercicios base';
COMMENT ON TABLE dishes IS 'Catálogo de platillos/comidas';

-- Mensaje de confirmación
DO $$
BEGIN
    RAISE NOTICE 'Todas las tablas, índices y restricciones han sido creados exitosamente';
END $$;