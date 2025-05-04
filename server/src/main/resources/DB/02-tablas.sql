-- Crear tipos enumerados primero
CREATE TYPE "MediaType" AS ENUM (
    'IMAGE',
    'VIDEO',
    'AUDIO',
    'DOCUMENT'
);

CREATE TYPE "BodyPart" AS ENUM (
    'CHEST',
    'BACK',
    'ARMS',
    'SHOULDERS',
    'LEGS',
    'ABS',
    'FULL_BODY',
    'OTHER'
);

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS "user" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    password VARCHAR(250) NOT NULL,
    email VARCHAR(250) NOT NULL,
    "creationTime" TIMESTAMP NOT NULL,
    image_dir VARCHAR(100) NULL
);

-- Tabla de entrenadores
CREATE TABLE IF NOT EXISTS "trainer" (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE ON UPDATE CASCADE,
    specialization VARCHAR(50),
    experience INTEGER,
    bio TEXT,
    rating DOUBLE PRECISION,
    certifications INTEGER[]
);

-- Tabla de enlaces sociales
CREATE TABLE IF NOT EXISTS "socials" (
    id SERIAL PRIMARY KEY,
    trainer_id INTEGER REFERENCES "trainer"(id) ON DELETE CASCADE ON UPDATE CASCADE,
    media VARCHAR(25),
    links VARCHAR(150)
);

-- Tabla de certificaciones
CREATE TABLE IF NOT EXISTS "certifications" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    issuin_organization VARCHAR(75),
    "dateObtained" TIMESTAMP
);

-- Tabla de deportistas
CREATE TABLE IF NOT EXISTS "sportsmans" (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE ON UPDATE CASCADE,
    trainer_id INTEGER REFERENCES "trainer"(id) ON UPDATE CASCADE ON DELETE SET NULL,
    age INTEGER,
    weight DOUBLE PRECISION,
    height DOUBLE PRECISION,
    sex BOOLEAN
);

-- Tabla de recursos
CREATE TABLE IF NOT EXISTS "resources" (
    id SERIAL PRIMARY KEY,
    type "MediaType" NOT NULL,
    path VARCHAR(255) NOT NULL
);

-- Tabla de notas
CREATE TABLE IF NOT EXISTS "notes" (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE ON UPDATE CASCADE,
    message TEXT
);

-- Tabla de ejercicios
CREATE TABLE IF NOT EXISTS "exercises" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    body_part "BodyPart" NOT NULL,
    reps INTEGER NOT NULL,
    sets INTEGER NOT NULL,
    description TEXT NOT NULL,
    author INTEGER REFERENCES "trainer"(id) ON UPDATE CASCADE ON DELETE SET NULL,
    video INTEGER REFERENCES "resources"(id) ON UPDATE CASCADE ON DELETE SET NULL
);

-- Tabla de relaciones entre ejercicios (opcionales)
CREATE TABLE IF NOT EXISTS "exercise_optional_links" (
    exercise_id INTEGER NOT NULL REFERENCES "exercises"(id) ON DELETE CASCADE,
    optional_id INTEGER NOT NULL REFERENCES "exercises"(id) ON DELETE CASCADE,
    PRIMARY KEY (exercise_id, optional_id)
);

-- Tabla de chats
CREATE TABLE IF NOT EXISTS "chat" (
    id SERIAL PRIMARY KEY,
    trainer_id INTEGER REFERENCES "trainer"(id) ON UPDATE CASCADE ON DELETE SET NULL,
    sportsman_id INTEGER REFERENCES "sportsmans"(id) ON UPDATE CASCADE ON DELETE SET NULL
);

-- Tabla de mensajes
CREATE TABLE IF NOT EXISTS "messages" (
    id SERIAL PRIMARY KEY,
    chat_id INTEGER NOT NULL REFERENCES "chat"(id) ON DELETE CASCADE ON UPDATE CASCADE,
    user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE ON UPDATE CASCADE,
    message_text VARCHAR(250) NOT NULL,
    message_type "MediaType" NOT NULL
);

-- Tabla de workouts (vacía como en tu definición)
CREATE TABLE IF NOT EXISTS "workouts" (
    id SERIAL PRIMARY KEY
);