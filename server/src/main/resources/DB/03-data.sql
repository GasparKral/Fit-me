-- ==================================================
-- Dataset de Prueba Completo - Sistema de Fitness
-- ==================================================

-- ===============================
-- 1. USUARIOS BASE
-- ===============================
INSERT INTO users (name, email, phone, creation_time, status, last_active, needs_attention, password)
VALUES
    -- Entrenador principal Alex (requisito específico)
    ('Alex', 'alex.trainer@fitnesshub.com', '+1-555-0101', NOW() - INTERVAL '6 months', true, NOW() - INTERVAL '2 hours', false, '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918'),

    -- Otros entrenadores
    ('María Rodríguez', 'maria.rodriguez@fitnesshub.com', '+1-555-0102', NOW() - INTERVAL '4 months', true, NOW() - INTERVAL '1 hour', false, 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3'),
    ('Carlos Mendoza', 'carlos.mendoza@fitnesshub.com', '+1-555-0103', NOW() - INTERVAL '3 months', true, NOW() - INTERVAL '3 hours', false, 'b3a8e0e1f9ab1bfe3a36f231f676f78bb30a519d2b21e6c530c0eee8ebb4a5d0'),

    -- Deportistas
    ('Laura Gómez', 'laura.gomez@email.com', '+1-555-0201', NOW() - INTERVAL '2 months', true, NOW() - INTERVAL '30 minutes', false, 'e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855'),
    ('Carlos Pérez', 'carlos.perez@email.com', '+1-555-0202', NOW() - INTERVAL '45 days', true, NOW() - INTERVAL '1 day', true, 'e258d248fda94c63753607f7c4494ee0fcbe92f1a76bfdac795c9d84101eb317'),
    ('Ana Martínez', 'ana.martinez@email.com', '+1-555-0203', NOW() - INTERVAL '30 days', true, NOW() - INTERVAL '4 hours', false, 'cd2eb0837c9b4c962c22d2ff8b5441b7b45805887f051d39bf133b583baf6860'),
    ('Roberto Silva', 'roberto.silva@email.com', '+1-555-0204', NOW() - INTERVAL '60 days', true, NOW() - INTERVAL '12 hours', false, 'a4e624d686e03ed2767c0abd85c14426b0b1157d2ce81d27bb4fe4f6f01d688a'),
    ('Elena Vásquez', 'elena.vasquez@email.com', '+1-555-0205', NOW() - INTERVAL '20 days', true, NOW() - INTERVAL '2 days', true, 'b6a9c8c230722b7c748331a8b450f05566dc7d0f64bd7c092e5e7a6df6b5a5b'),
    ('Diego Herrera', 'diego.herrera@email.com', '+1-555-0206', NOW() - INTERVAL '15 days', true, NOW() - INTERVAL '6 hours', false, 'c7ad44cbad762a5da0a452f9e854fdc1e0e7a52a38015f23f3eab1d80b931dd4'),
    ('Sofía Torres', 'sofia.torres@email.com', '+1-555-0207', NOW() - INTERVAL '25 days', true, NOW() - INTERVAL '8 hours', false, 'd7613212e02bbc2fe7113b28a67db2e0b6a7fc23c4c5b7a2c5c0e7f1f5a5b5c');

-- ===============================
-- 2. ENTRENADORES
-- ===============================
INSERT INTO trainers (user_id, specialization, years_of_experience, bio)
VALUES
    ((SELECT id FROM users WHERE name = 'Alex'), 'Fuerza y Movilidad', 8, 'Entrenador personal certificado con enfoque en desarrollo de fuerza funcional y mejora de movilidad. Especialista en rehabilitación deportiva y entrenamiento de alto rendimiento.'),
    ((SELECT id FROM users WHERE name = 'María Rodríguez'), 'Fitness Femenino', 5, 'Especializada en entrenamiento para mujeres, pre y post natal. Certificada en pilates y yoga. Enfoque en bienestar integral y autoestima.'),
    ((SELECT id FROM users WHERE name = 'Carlos Mendoza'), 'CrossFit y Funcional', 12, 'Entrenador de CrossFit Level 3. Especialista en entrenamiento funcional de alta intensidad y preparación para competencias deportivas.');

-- ===============================
-- 3. DEPORTISTAS
-- ===============================
INSERT INTO sportsmen (user_id, trainer_id, age, sex, training_since)
VALUES
    ((SELECT id FROM users WHERE name = 'Laura Gómez'),1, 28, true, NOW() - INTERVAL '2 months'),
    ((SELECT id FROM users WHERE name = 'Carlos Pérez'),1, 35, false, NOW() - INTERVAL '45 days'),
    ((SELECT id FROM users WHERE name = 'Ana Martínez'),1, 32, true, NOW() - INTERVAL '30 days'),
    ((SELECT id FROM users WHERE name = 'Roberto Silva'),1, 29, false, NOW() - INTERVAL '60 days'),
    ((SELECT id FROM users WHERE name = 'Elena Vásquez'),1, 26, true, NOW() - INTERVAL '20 days'),
    ((SELECT id FROM users WHERE name = 'Diego Herrera'),1, 31, false, NOW() - INTERVAL '15 days'),
    ((SELECT id FROM users WHERE name = 'Sofía Torres'),1, 24, true, NOW() - INTERVAL '25 days');

-- ===============================
-- 4. RECURSOS MULTIMEDIA
-- ===============================
INSERT INTO resources (media_type, src, user_id)
VALUES
    -- Videos de Alex
    ('VIDEO', 'https://storage.fitnesshub.com/videos/sentadillas_basicas.mp4', (SELECT id FROM users WHERE name = 'Alex')),
    ('VIDEO', 'https://storage.fitnesshub.com/videos/deadlift_form.mp4', (SELECT id FROM users WHERE name = 'Alex')),
    ('VIDEO', 'https://storage.fitnesshub.com/videos/push_up_variations.mp4', (SELECT id FROM users WHERE name = 'Alex')),
    ('IMAGE', 'https://storage.fitnesshub.com/images/proper_squat_form.jpg', (SELECT id FROM users WHERE name = 'Alex')),

    -- Videos de María
    ('VIDEO', 'https://storage.fitnesshub.com/videos/prenatal_yoga.mp4', (SELECT id FROM users WHERE name = 'María Rodríguez')),
    ('VIDEO', 'https://storage.fitnesshub.com/videos/pilates_core.mp4', (SELECT id FROM users WHERE name = 'María Rodríguez')),

    -- Videos de Carlos
    ('VIDEO', 'https://storage.fitnesshub.com/videos/crossfit_wod.mp4', (SELECT id FROM users WHERE name = 'Carlos Mendoza')),
    ('VIDEO', 'https://storage.fitnesshub.com/videos/functional_movements.mp4', (SELECT id FROM users WHERE name = 'Carlos Mendoza'));

-- ===============================
-- 5. EJERCICIOS BASE
-- ===============================
INSERT INTO exercise_bases (name, body_part, description, author_id, video_resource_id)
VALUES
    -- Ejercicios de Alex
    ('Sentadillas', 'LEG', 'Ejercicio fundamental para el fortalecimiento del tren inferior. Trabaja glúteos, cuádriceps y core.',
     (SELECT id FROM users WHERE name = 'Alex'),
     (SELECT id FROM resources WHERE src LIKE '%sentadillas_basicas%')),

    ('Peso Muerto', 'BACK', 'Ejercicio compuesto que fortalece la cadena posterior. Fundamental para el desarrollo de fuerza general.',
     (SELECT id FROM users WHERE name = 'Alex'),
     (SELECT id FROM resources WHERE src LIKE '%deadlift_form%')),

    ('Flexiones', 'CHEST', 'Ejercicio básico de empuje que fortalece pecho, hombros y tríceps.',
     (SELECT id FROM users WHERE name = 'Alex'),
     (SELECT id FROM resources WHERE src LIKE '%push_up_variations%')),

    ('Plancha', 'CORE', 'Ejercicio isométrico para el fortalecimiento del core y estabilidad.',
     (SELECT id FROM users WHERE name = 'Alex'), NULL),

    -- Ejercicios de María
    ('Yoga Prenatal', 'FULL_BODY', 'Secuencia de yoga adaptada para mujeres embarazadas.',
     (SELECT id FROM users WHERE name = 'María Rodríguez'),
     (SELECT id FROM resources WHERE src LIKE '%prenatal_yoga%')),

    ('Pilates Core', 'CORE', 'Ejercicios de pilates enfocados en el fortalecimiento del core.',
     (SELECT id FROM users WHERE name = 'María Rodríguez'),
     (SELECT id FROM resources WHERE src LIKE '%pilates_core%')),

    -- Ejercicios de Carlos
    ('Burpees', 'FULL_BODY', 'Ejercicio de alta intensidad que combina fuerza y cardio.',
     (SELECT id FROM users WHERE name = 'Carlos Mendoza'), NULL),

    ('Thrusters', 'FULL_BODY', 'Ejercicio funcional que combina sentadilla frontal con press militar.',
     (SELECT id FROM users WHERE name = 'Carlos Mendoza'), NULL);

-- ===============================
-- 6. NOTAS
-- ===============================
INSERT INTO notes (user_id, message)
VALUES
    ((SELECT id FROM users WHERE name = 'Laura Gómez'), 'Excelente progresión en sentadillas esta semana. Aumentar peso la próxima sesión.'),
    ((SELECT id FROM users WHERE name = 'Laura Gómez'), 'Recordar mantener la espalda recta durante el peso muerto.'),
    ((SELECT id FROM users WHERE name = 'Carlos Pérez'), 'Necesita trabajar más en flexibilidad de cadera.'),
    ((SELECT id FROM users WHERE name = 'Carlos Pérez'), 'Buena técnica en flexiones, listo para progresión.'),
    ((SELECT id FROM users WHERE name = 'Ana Martínez'), 'Adaptar ejercicios para segundo trimestre de embarazo.'),
    ((SELECT id FROM users WHERE name = 'Roberto Silva'), 'Excelente resistencia cardiovascular, aumentar intensidad.'),
    ((SELECT id FROM users WHERE name = 'Elena Vásquez'), 'Trabajar en coordinación y equilibrio.'),
    ((SELECT id FROM users WHERE name = 'Diego Herrera'), 'Revisar forma en levantamientos olímpicos.'),
    ((SELECT id FROM users WHERE name = 'Sofía Torres'), 'Mostrar gran motivación, mantener consistencia.');

-- 2. INSERTAR USUARIOS BÁSICOS
INSERT INTO users (name, email, phone, creation_time, status, last_active, needs_attention, password) VALUES
('Ana García', 'ana.garcia@email.com', '+34612345678', '2024-01-15 10:30:00', true, '2024-05-20 14:22:00', false, '$2b$10$hashedpassword1'),
('Carlos Rodríguez', 'carlos.rodriguez@email.com', '+34623456789', '2024-01-20 09:15:00', true, '2024-05-21 16:45:00', false, '$2b$10$hashedpassword2'),
('María López', 'maria.lopez@email.com', '+34634567890', '2024-02-01 11:20:00', true, '2024-05-22 08:30:00', true, '$2b$10$hashedpassword3'),
('David Martín', 'david.martin@email.com', '+34645678901', '2024-02-05 16:45:00', true, '2024-05-21 19:15:00', false, '$2b$10$hashedpassword4'),
('Laura Sánchez', 'laura.sanchez@email.com', '+34656789012', '2024-02-10 14:30:00', true, '2024-05-22 12:10:00', false, '$2b$10$hashedpassword5'),
('Miguel Torres', 'miguel.torres@email.com', '+34667890123', '2024-02-15 13:20:00', true, '2024-05-20 20:30:00', false, '$2b$10$hashedpassword6'),
('Sofia Ruiz', 'sofia.ruiz@email.com', '+34678901234', '2024-02-20 10:45:00', true, '2024-05-22 07:45:00', false, '$2b$10$hashedpassword7'),
('Javier Herrera', 'javier.herrera@email.com', '+34689012345', '2024-03-01 15:30:00', true, '2024-05-21 21:20:00', true, '$2b$10$hashedpassword8'),
('Carmen Jiménez', 'carmen.jimenez@email.com', '+34690123456', '2024-03-05 12:15:00', true, '2024-05-22 09:30:00', false, '$2b$10$hashedpassword9'),
('Roberto Vega', 'roberto.vega@email.com', '+34601234567', '2024-03-10 17:00:00', true, '2024-05-21 18:45:00', false, '$2b$10$hashedpassword10');

-- 4. INSERTAR ENTRENADORES
INSERT INTO trainers (user_id, specialization, years_of_experience, bio) VALUES
(1, 'Entrenamiento Funcional', 5, 'Especialista en entrenamiento funcional y rehabilitación. Certificada en CrossFit y yoga terapéutico.'),
(2, 'Musculación y Fuerza', 8, 'Entrenador personal especializado en desarrollo de fuerza y masa muscular. Competidor de powerlifting.'),
(4, 'Fitness Femenino', 6, 'Experto en entrenamiento específico para mujeres, pre y post natal. Nutrición deportiva.'),
(6, 'Alto Rendimiento', 10, 'Entrenador de atletas de alto rendimiento. Especialista en periodización y planificación deportiva.');

-- 5. INSERTAR DEPORTISTAS
INSERT INTO sportsmen (user_id, trainer_id, age, sex, training_since) VALUES
(3, 1, 28, false, '2024-02-15 00:00:00'),  -- María con Ana
(5, 2, 25, false, '2024-03-01 00:00:00'),  -- Laura con Carlos
(7, 1, 22, false, '2024-03-05 00:00:00'),  -- Sofia con Ana
(8, 4, 35, true, '2024-03-10 00:00:00'),   -- Javier con Miguel
(9, 4, 30, false, '2024-03-15 00:00:00'),  -- Carmen con Miguel
(10, 2, 29, true, '2024-03-20 00:00:00');  -- Roberto con Carlos

-- 6. INSERTAR CERTIFICACIONES
INSERT INTO certifications (trainer_id, name, issuing_organization, date_obtained) VALUES
(1, 'CrossFit Level 1 Trainer', 'CrossFit Inc.','2025-06-15'),
(1, 'Yoga Alliance RYT-200', 'Yoga Alliance', '2026-08-20'),
(2, 'NSCA-CPT', 'National Strength and Conditioning Association','2025-03-10'),
(2, 'Powerlifting Coach Level 2', 'IPF',  '2024-11-05'),
(3, 'Pre/Postnatal Exercise Specialist', 'ACSM', '2025-01-15'),
(4, 'Performance Enhancement Specialist', 'NASM', '2024-09-20');

-- 8. INSERTAR EJERCICIOS BASE
INSERT INTO exercise_bases (name, body_part, description, author_id, video_resource_id) VALUES
('Sentadilla con peso corporal', 'LEG', 'Ejercicio fundamental para desarrollar fuerza en piernas y glúteos. Mantener la espalda recta y bajar hasta que los muslos estén paralelos al suelo.', 1, 1),
('Flexiones de pecho', 'CHEST', 'Ejercicio clásico para desarrollar pecho, hombros y tríceps. Mantener el cuerpo recto como una tabla.', 2, 2),
('Peso muerto rumano', 'BACK', 'Excelente ejercicio para fortalecer la cadena posterior. Enfoque en la bisagra de cadera y mantener la barra cerca del cuerpo.', 2, 3),
('Plancha frontal', 'CORE', 'Ejercicio isométrico para fortalecer el core. Mantener el cuerpo recto y contraer abdominales.', 1, 4),
('Press militar con mancuernas', 'SHOULDER', 'Desarrollo de hombros y estabilidad del core. Mantener el core contraído durante todo el movimiento.', 4, NULL),
('Remo con banda elástica', 'BACK', 'Fortalecimiento de la musculatura de la espalda media. Apretar omóplatos al tirar.', 1, NULL),
('Sentadilla búlgara', 'LEG', 'Ejercicio unilateral para piernas. Excelente para corregir desequilibrios musculares.', 4, NULL),
('Burpees', 'FULL_BODY', 'Ejercicio cardiovascular de alta intensidad que trabaja todo el cuerpo.', 2, 6);

-- 10. INSERTAR ENTRENAMIENTOS
INSERT INTO workouts (sportsman_id, name, description, duration_millis, initial_date) VALUES
(1, 'Entrenamiento de Piernas', 'Rutina enfocada en el desarrollo de la musculatura de las piernas', 3600000, '2024-05-20 17:00:00'),
(1, 'Upper Body Strength', 'Entrenamiento de fuerza para tren superior', 2700000, '2024-05-22 18:00:00'),
(2, 'Full Body Beginner', 'Rutina de cuerpo completo para principiantes', 2400000, '2024-05-21 19:00:00'),
(3, 'HIIT Cardio', 'Entrenamiento interválico de alta intensidad', 1800000, '2024-05-22 07:00:00'),
(4, 'Strength & Conditioning', 'Combinación de fuerza y acondicionamiento', 4200000, '2024-05-21 16:30:00'),
(5, 'Flexibility & Mobility', 'Sesión de flexibilidad y movilidad', 3000000, '2024-05-22 10:00:00');

-- 11. INSERTAR EJERCICIOS EN ENTRENAMIENTOS
INSERT INTO workout_exercises (workout_id, exercise_base_id, sets, reps,day,is_option) VALUES
-- Entrenamiento de Piernas (workout_id = 1)
(1, 1, 4, 15,'MONDAY',TRUE),
(1, 7, 3, 12,'MONDAY',FALSE),
(1, 1, 3, 20,'MONDAY',TRUE),
-- Upper Body Strength (workout_id = 2)
(2, 2, 4, 10,'MONDAY',FALSE),
(2, 5, 3, 8,'MONDAY',TRUE),
(2, 6, 3, 12,'MONDAY',FALSE),
-- Full Body Beginner (workout_id = 3)
(3, 1, 3, 10,'MONDAY',FALSE),
(3, 2, 2, 8,'MONDAY',TRUE),
(3, 4, 3,10,'MONDAY',FALSE),
-- HIIT Cardio (workout_id = 4)
(4, 8, 5, 10,'MONDAY',FALSE),
(4, 1, 4, 15,'MONDAY',TRUE),
(4, 2, 3, 10,'MONDAY',FALSE),
-- Strength & Conditioning (workout_id = 5)
(5, 3, 5, 8,'MONDAY',FALSE),
(5, 5, 4, 2,'MONDAY',TRUE),
(5, 4, 3,15,'MONDAY',FALSE);

-- 12. INSERTAR MEDICIONES
INSERT INTO measurements (sportsman_id, weight, height, body_fat, last_updated) VALUES
(1, 65.5, 165.0, 22.5, '2024-02-15'),
(1, 64.8, 165.0, 21.8, '2024-03-15'),
(1, 64.2, 165.0, 21.2, '2024-04-15'),
(2, 58.0, 160.0, 18.5, '2024-03-01'),
(2, 59.2, 160.0, 18.8, '2024-04-01'),
(3, 52.5, 155.0, 20.0, '2024-03-05'),
(4, 75.0, 175.0, 15.5, '2024-03-10'),
(5, 62.0, 168.0, 24.0, '2024-03-15'),
(6, 78.5, 180.0, 12.8, '2024-03-20');

-- 13. INSERTAR PLATOS/COMIDAS
INSERT INTO dishes (name) VALUES
('Pechuga de pollo a la plancha'),
('Arroz integral cocido'),
('Ensalada mixta'),
('Salmón al horno'),
('Batata asada'),
('Yogur griego natural'),
('Avena con frutas'),
('Tortilla de claras');

-- 14. INSERTAR ALTERNATIVAS DE PLATOS
INSERT INTO dish_alternatives (dish_id, alternative_dish_id) VALUES
(1, 4),
(2, 5),
(6, 8),
(7, 6);

-- 15. INSERTAR DIETAS
INSERT INTO diets (sportsman_id, name, description, duration_millis, initial_date) VALUES
(1, 'Dieta de Definición Muscular', 'Plan nutricional enfocado en reducir grasa corporal manteniendo masa muscular. Rica en proteínas y baja en carbohidratos simples.', 5184000000, '2024-01-15 08:00:00'),
(2, 'Dieta de Volumen Limpio', 'Régimen alimentario para aumentar masa muscular de forma controlada. Alto contenido calórico con alimentos de calidad.', 7776000000, '2024-02-01 09:30:00'),
(3, 'Dieta Mediterránea Deportiva', 'Adaptación de la dieta mediterránea para atletas. Enfoque en grasas saludables, pescado, vegetales y cereales integrales.', 10368000000, '2024-03-10 07:15:00'),
(4, 'Plan Nutricional Pre-Competencia', 'Dieta específica para preparación de competencia. Control estricto de macronutrientes y timing de comidas.', 2592000000, '2024-04-05 06:45:00'),
(5, 'Dieta de Recuperación Post-Lesión', 'Plan alimentario diseñado para acelerar la recuperación tras lesión deportiva. Rica en antioxidantes y antiinflamatorios.', 6480000000, '2024-05-01 10:00:00');
-- 16. INSERTAR COMIDAS EN DIETAS
INSERT INTO diet_meals (diet_id, dish_id, quantity, day) VALUES
-- Dieta 1 - Pérdida de Grasa
(1, 7, 1.0, 'MONDAY'),
(1, 1, 1.2, 'MONDAY'),
(1, 3, 1.0, 'MONDAY'),
(1, 4, 1.0, 'MONDAY'),
(1, 5, 0.8, 'MONDAY'),
-- Dieta 2 - Ganancia Muscular
(2, 7, 1.5, 'MONDAY'),
(2, 1, 1.5, 'MONDAY'),
(2, 2, 1.2, 'MONDAY'),
(2, 4, 1.3, 'MONDAY'),
(2, 6, 1.0, 'MONDAY'),
-- Dieta 3 - Mantenimiento
(3, 6, 1.0, 'MONDAY'),
(3, 1, 1.0, 'MONDAY'),
(3, 2, 0.8, 'MONDAY'),
(3, 3, 1.0, 'MONDAY'),
(3, 4, 0.9, 'MONDAY');

-- 17. INSERTAR ALERGIAS
INSERT INTO sportsman_allergies (sportsman_id, allergy) VALUES
(1, 'Frutos secos'),
(3, 'Lactosa'),
(4, 'Gluten'),
(5, 'Mariscos');

-- 18. INSERTAR CHATS
INSERT INTO chats (trainer_id, sportsman_id) VALUES
(1, 1),
(2, 2),
(1, 3),
(4, 4),
(4, 5);

-- 19. INSERTAR MENSAJES
INSERT INTO messages (chat_id, author_id, message, post_time, message_type) VALUES
(1, 1, '¡Hola María! ¿Cómo te fue con el entrenamiento de ayer?', '2024-05-22 14:00:00', 'MESSAGE'),
(1, 3, 'Hola Ana, muy bien! Aunque siento que las sentadillas búlgaras me costaron más de lo esperado', '2024-05-22 14:15:00', 'MESSAGE'),
(1, 1, 'Es normal, son más exigentes. Recuerda mantener el peso en la pierna delantera', '2024-05-22 14:30:00', 'MESSAGE'),
(2, 2, 'Laura, recuerda que mañana tenemos sesión a las 19:00', '2024-05-22 11:00:00', 'MESSAGE'),
(2, 5, 'Perfecto Carlos, ahí estaré. ¿Seguimos con el press de banca?', '2024-05-22 11:15:00', 'MESSAGE'),
(3, 1, 'Sofia, excelente progreso esta semana. ¿Cómo te sientes?', '2024-05-21 20:30:00', 'MESSAGE'),
(3, 7, 'Genial! Me siento mucho más fuerte', '2024-05-21 20:45:00', 'MESSAGE');

-- 20. INSERTAR ENTRENAMIENTOS COMPLETADOS
INSERT INTO completed_workouts (workout_id,sportsman_id, completed_at) VALUES
(1, 4,'2024-05-20 18:00:00'),
(2, 5,'2024-05-22 19:15:00'),
(3, 5,'2024-05-21 20:30:00'),
(4, 6,'2024-05-22 08:00:00');

-- 21. INSERTAR ENLACES SOCIALES DE ENTRENADORES
INSERT INTO trainer_social_links (trainer_id, platform, url) VALUES
(1, 'Instagram', 'https://instagram.com/ana_fitness_coach'),
(1, 'YouTube', 'https://youtube.com/c/AnaFunctionalFitness'),
(2, 'Instagram', 'https://instagram.com/carlos_strength'),
(4, 'LinkedIn', 'https://linkedin.com/in/miguel-torres-trainer');

/* -- 22. INSERTAR SLOTS DE TIEMPO DISPONIBLES
INSERT INTO time_slots (trainer_id, day_of_week, start_time, end_time, is_available) VALUES
(1, 'MONDAY', '08:00:00', '10:00:00', true),
(1, 'MONDAY', '17:00:00', '20:00:00', true),
(1, 'WEDNESDAY', '08:00:00', '10:00:00', true),
(1, 'FRIDAY', '17:00:00', '20:00:00', true),
(2, 'TUESDAY', '16:00:00', '21:00:00', true),
(2, 'THURSDAY', '16:00:00', '21:00:00', true),
(2, 'SATURDAY', '09:00:00', '14:00:00', true),
(4, 'MONDAY', '06:00:00', '12:00:00', true),
(4, 'WEDNESDAY', '06:00:00', '12:00:00', true),
(4, 'FRIDAY', '06:00:00', '12:00:00', true); */

-- 23. INSERTAR SESIONES PROGRAMADAS
INSERT INTO sessions (trainer_id, sportsman_id, date_time, duration_millis) VALUES
(1, 1, '2024-05-23 17:30:00', 3600000),
(1, 3, '2024-05-24 18:00:00', 3600000),
(2, 2, '2024-05-23 19:00:00', 3600000),
(4, 4, '2024-05-24 07:00:00', 4200000),
(4, 5, '2024-05-23 10:00:00', 3000000);
