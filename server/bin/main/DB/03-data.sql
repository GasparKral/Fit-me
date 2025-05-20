-- Requiere extensión pgcrypto para hash SHA-256
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Insertar recurso de imagen para el usuario
INSERT INTO "resources" ("type", "path")
VALUES ('IMAGE', '/images/alex.jpg')
RETURNING id INTO TEMP TABLE temp_resource;

-- Insertar usuario "Alex"
INSERT INTO "user" ("name", "password", "email", "creationTime", "userImage")
VALUES (
    'Alex',
    ENCODE(DIGEST('admin', 'sha256'), 'hex'),  -- Hash de 'admin'
    'alex@example.com',
    NOW(),
    (SELECT id FROM temp_resource)
)
RETURNING id INTO TEMP TABLE temp_user;

-- Insertar certificación
INSERT INTO "certifications" ("name", "issuin_organization", "optainedDate")
VALUES ('Certified Personal Trainer', 'Fitness Org', '2022-06-01')
RETURNING id INTO TEMP TABLE temp_cert;

-- Insertar entrenador asociado al usuario "Alex"
INSERT INTO "trainer" ("user_id", "specialization", "years_of_experience", "bio", "certifications")
VALUES (
    (SELECT id FROM temp_user),
    'Weight Loss & Hypertrophy',
    5,
    'Entrenador con experiencia en fuerza y acondicionamiento físico.',
    ARRAY[(SELECT id FROM temp_cert)]
)
RETURNING id INTO TEMP TABLE temp_trainer;

-- Insertar redes sociales del entrenador
INSERT INTO "socials" ("trainer_id", "media", "links")
VALUES (
    (SELECT id FROM temp_trainer),
    'Instagram',
    'https://instagram.com/alexfitcoach'
);

-- Insertar un usuario deportista
INSERT INTO "user" ("name", "password", "email", "creationTime", "userImage")
VALUES (
    'Carlos',
    ENCODE(DIGEST('carlos123', 'sha256'), 'hex'),
    'carlos@example.com',
    NOW(),
    NULL
)
RETURNING id INTO TEMP TABLE temp_user_sportsman;

-- Insertar deportista asociado al entrenador
INSERT INTO "sportsmans" ("user_id", "trainer_id", "age", "weight", "height", "sex")
VALUES (
    (SELECT id FROM temp_user_sportsman),
    (SELECT id FROM temp_trainer),
    28,
    75.5,
    1.78,
    TRUE -- Hombre
)
RETURNING id INTO TEMP TABLE temp_sportsman;

-- Insertar ejercicio de ejemplo
INSERT INTO "exercises" ("name", "body_part", "reps", "sets", "description", "author", "video")
VALUES (
    'Push-ups',
    'CHEST',
    15,
    3,
    'Flexiones de pecho para fuerza y resistencia.',
    (SELECT id FROM temp_trainer),
    NULL
)
RETURNING id INTO TEMP TABLE temp_exercise;

-- Insertar rutina de entrenamiento
INSERT INTO "workouts" ("name", "description", "sportsman_id", "trainer_id", "requires_assistance", "creation_date", "duration_weeks")
VALUES (
    'Plan Básico de Fuerza',
    'Rutina de 4 semanas enfocada en fuerza básica.',
    (SELECT id FROM temp_sportsman),
    (SELECT id FROM temp_trainer),
    FALSE,
    NOW(),
    4
)
RETURNING id INTO TEMP TABLE temp_workout;

-- Insertar relación entre rutina y ejercicio
INSERT INTO "workout_exercises" ("workout_id", "exercise_id", "week_day", "exercise_order", "reps", "sets", "is_option")
VALUES (
    (SELECT id FROM temp_workout),
    (SELECT id FROM temp_exercise),
    'MONDAY',
    1,
    15,
    3,
    FALSE
);
