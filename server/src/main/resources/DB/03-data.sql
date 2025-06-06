-- ============================================================================
-- INSERTING TEST DATA
-- ============================================================================

-- Insert users
INSERT INTO users (fullname, password, email, phone, user_image_url) VALUES
('Alex', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'alex@fitnesstrainer.com', '+34 666 123 456', 'https://example.com/images/carlos.jpg'),
('Sarah Athlete', '$2a$10$yK9L6w4zM2n5v6b7c8d9e0', 'sarah.athlete@example.com', '2345678901', 'https://example.com/images/sarah.jpg'),
('Mike Coach', '$2a$10$zL0M7x5yN4o3p2q1r9s8t7', 'mike.coach@example.com', '3456789012', 'https://example.com/images/mike.jpg'),
('Emma Runner', '$2a$10$aB1C2d3E4f5G6h7I8j9K0l', 'emma.runner@example.com', '4567890123', 'https://example.com/images/emma.jpg'),
('David Lifter', '$2a$10$bM9N8O7P6q5R4s3T2U1V0w', 'david.lifter@example.com', '5678901234', 'https://example.com/images/david.jpg'),
('Lisa Swimmer', '$2a$10$cX9Y8Z7W6v5U4t3S2R1Q0p', 'lisa.swimmer@example.com', '6789012345', 'https://example.com/images/lisa.jpg'),
('Admin User', '$2a$10$dA1d2m3i4n5a6d7m8i9n0a', 'admin@example.com', '7890123456', 'https://example.com/images/admin.jpg');

-- Insert user status
INSERT INTO user_status (state, user_id, is_online, last_seen_at) VALUES
('ACTIVE', 1, TRUE, NOW()),
('ACTIVE', 2, FALSE, NOW() - INTERVAL '30 minutes'),
('ACTIVE', 3, TRUE, NOW()),
('ACTIVE', 4, FALSE, NOW() - INTERVAL '2 hours'),
('ACTIVE', 5, TRUE, NOW()),
('ACTIVE', 6, FALSE, NOW() - INTERVAL '1 day'),
('ACTIVE', 7, TRUE, NOW());

-- Insert trainers
INSERT INTO trainers (specialization, years_of_experience, user_id) VALUES
('Strength Training', 5, 1),
('Cardio and Endurance', 3, 3),
('Swimming and Aquatic Sports', 7, 7);

-- Insert allergies
INSERT INTO allergies (name) VALUES
('Peanuts'),
('Gluten'),
('Lactose'),
('Shellfish'),
('Eggs'),
('Soy');

-- Insert athletes
INSERT INTO athletes (age, sex, training_since, need_assistant, user_id, trainer_id) VALUES
(28, 'FEMALE', NOW() - INTERVAL '1 year', FALSE, 2, 1),
(32, 'MALE', NOW() - INTERVAL '6 months', TRUE, 4, 1),
(25, 'MALE', NOW() - INTERVAL '2 years', FALSE, 5, 3),
(30, 'FEMALE', NOW() - INTERVAL '3 months', TRUE, 6, 3);

-- Insert athlete allergies
INSERT INTO athlete_allergies (athlete_id, allergy_id) VALUES
(1, 2), -- Sarah is allergic to gluten
(1, 3), -- Sarah is allergic to lactose
(3, 1), -- David is allergic to peanuts
(4, 4); -- Lisa is allergic to shellfish

-- Insert measurements
INSERT INTO measurements (athlete_id, weight, height, body_fat, arm_size, chest_back_size, hip_size, leg_size, calves_size) VALUES
(1, 65.5, 170.0, 22.0, 28.5, 95.0, 92.0, 55.0, 36.0),
(2, 80.0, 180.0, 18.0, 35.0, 110.0, 100.0, 60.0, 40.0),
(3, 75.0, 175.0, 15.0, 32.0, 105.0, 95.0, 58.0, 38.0),
(4, 60.0, 165.0, 20.0, 26.0, 90.0, 88.0, 52.0, 34.0);

-- Insert certifications
INSERT INTO certifications (trainer_id, name, issuing_organization, complete_at) VALUES
(1, 'Certified Strength Trainer', 'National Strength Association', NOW() - INTERVAL '3 years'),
(1, 'Advanced Nutrition Specialist', 'Fitness Professionals Academy', NOW() - INTERVAL '2 years'),
(2, 'Cardio Conditioning Expert', 'International Sports Science Association', NOW() - INTERVAL '1 year'),
(3, 'Aquatic Fitness Instructor', 'World Swimming Federation', NOW() - INTERVAL '5 years');

-- Insert trainer socials
INSERT INTO trainer_socials (trainer_id, platform, url) VALUES
(1, 'Instagram', 'https://instagram.com/johntrainer'),
(1, 'Twitter', 'https://twitter.com/johntrainer'),
(2, 'Instagram', 'https://instagram.com/mikecoach'),
(3, 'YouTube', 'https://youtube.com/lisaswimmer');

-- Insert trainer availability
INSERT INTO trainer_availability (trainer_id, week_day, start_time, end_time) VALUES
(1, 'MONDAY', '2023-01-01 09:00:00', '2023-01-01 17:00:00'),
(1, 'WEDNESDAY', '2023-01-01 09:00:00', '2023-01-01 17:00:00'),
(1, 'FRIDAY', '2023-01-01 09:00:00', '2023-01-01 17:00:00'),
(2, 'TUESDAY', '2023-01-01 08:00:00', '2023-01-01 16:00:00'),
(2, 'THURSDAY', '2023-01-01 08:00:00', '2023-01-01 16:00:00'),
(3, 'MONDAY', '2023-01-01 07:00:00', '2023-01-01 15:00:00'),
(3, 'WEDNESDAY', '2023-01-01 07:00:00', '2023-01-01 15:00:00'),
(3, 'FRIDAY', '2023-01-01 07:00:00', '2023-01-01 15:00:00');

-- Insert exercises
INSERT INTO exercises (name, description, body_part) VALUES
('Bench Press', 'Standard barbell bench press', 'CHEST'),
('Squat', 'Barbell back squat', 'LEG'),
('Deadlift', 'Conventional deadlift', 'BACK'),
('Pull-up', 'Bodyweight pull-up', 'BACK'),
('Push-up', 'Standard push-up', 'CHEST'),
('Running', 'Outdoor or treadmill running', 'FULL_BODY'),
('Swimming', 'Freestyle swimming', 'FULL_BODY'),
('Plank', 'Core stability exercise', 'CORE'),
('Bicep Curl', 'Dumbbell bicep curl', 'ARM'),
('Shoulder Press', 'Overhead dumbbell press', 'SHOULDER');

-- Insert workout templates
INSERT INTO workout_templates (name, description, difficulty, workout_type, created_by) VALUES
('Beginner Full Body', 'Full body workout for beginners', 'EASY', 'STRENGTH', 1),
('Intermediate Strength', 'Strength focused intermediate workout', 'ADVANCE', 'STRENGTH', 1),
('Advanced Powerlifting', 'Advanced powerlifting program', 'HARD', 'STRENGTH', 1),
('Cardio Blast', 'High intensity cardio workout', 'ADVANCE', 'CARDIO', 2),
('Swimmer Routine', 'Swimming endurance program', 'HARD', 'CARDIO', 3);

-- Insert workout template exercises
INSERT INTO workout_template_exercises (template_id, exercise_id, week_day, reps, sets, is_optional) VALUES
(1, 1, 'MONDAY', 10, 3, FALSE),
(1, 2, 'MONDAY', 8, 3, FALSE),
(1, 3, 'MONDAY', 6, 3, FALSE),
(1, 8, 'MONDAY', 30, 3, TRUE),
(2, 1, 'MONDAY', 8, 4, FALSE),
(2, 4, 'WEDNESDAY', 6, 4, FALSE),
(2, 2, 'FRIDAY', 5, 5, FALSE),
(3, 1, 'MONDAY', 5, 5, FALSE),
(3, 2, 'WEDNESDAY', 3, 5, FALSE),
(3, 3, 'FRIDAY', 1, 5, FALSE),
(4, 6, 'TUESDAY', 20, 1, FALSE),
(4, 6, 'THURSDAY', 30, 1, FALSE),
(5, 7, 'MONDAY', 40, 1, FALSE),
(5, 7, 'WEDNESDAY', 50, 1, FALSE),
(5, 7, 'FRIDAY', 60, 1, FALSE);

-- Insert workouts
INSERT INTO workouts (name, description, difficulty, duration_minutes, workout_type, created_by, start_up) VALUES
('Sarah Strength Program', 'Custom strength program for Sarah', 'ADVANCE', 60, 'STRENGTH', 1, NOW() + INTERVAL '1 day'),
('Mike Cardio Plan', 'Cardio plan for Mike', 'EASY', 45, 'CARDIO', 2, NOW() + INTERVAL '2 days'),
('Emma Swim Training', 'Swim training for Emma', 'HARD', 90, 'CARDIO', 3, NOW() + INTERVAL '3 days'),
('David Powerlifting', 'Powerlifting routine for David', 'HARD', 120, 'STRENGTH', 1, NOW() + INTERVAL '1 week');

-- Update athletes with their workout IDs
UPDATE athletes SET workout_id = 1 WHERE id = 1;
UPDATE athletes SET workout_id = 2 WHERE id = 2;
UPDATE athletes SET workout_id = 3 WHERE id = 3;
UPDATE athletes SET workout_id = 4 WHERE id = 4;

-- Insert workout exercises
INSERT INTO workout_exercises (workout_id, exercise_id, week_day, reps, sets, is_optional) VALUES
(1, 1, 'MONDAY', 8, 4, FALSE),
(1, 2, 'MONDAY', 6, 4, FALSE),
(1, 9, 'MONDAY', 10, 3, TRUE),
(1, 4, 'WEDNESDAY', 6, 4, FALSE),
(1, 5, 'WEDNESDAY', 15, 3, FALSE),
(1, 8, 'WEDNESDAY', 45, 3, TRUE),
(1, 3, 'FRIDAY', 5, 5, FALSE),
(1, 10, 'FRIDAY', 8, 4, FALSE),
(2, 6, 'TUESDAY', 20, 1, FALSE),
(2, 6, 'THURSDAY', 30, 1, FALSE),
(3, 7, 'MONDAY', 40, 1, FALSE),
(3, 7, 'WEDNESDAY', 50, 1, FALSE),
(3, 7, 'FRIDAY', 60, 1, FALSE),
(4, 1, 'MONDAY', 5, 5, FALSE),
(4, 2, 'WEDNESDAY', 3, 5, FALSE),
(4, 3, 'FRIDAY', 1, 5, FALSE);

-- Insert completion workout statistics
INSERT INTO completion_workout_statistics (workout_id, athlete_id, complete_at) VALUES
(1, 1, NOW() - INTERVAL '3 days'),
(1, 1, NOW() - INTERVAL '1 week'),
(2, 2, NOW() - INTERVAL '2 days'),
(3, 3, NOW() - INTERVAL '5 days'),
(4, 4, NOW() - INTERVAL '1 week');

-- Insert dishes
INSERT INTO dishes (name) VALUES
('Grilled Chicken'),
('Brown Rice'),
('Steamed Vegetables'),
('Salmon Fillet'),
('Quinoa Salad'),
('Greek Yogurt'),
('Protein Shake'),
('Oatmeal'),
('Egg Whites'),
('Avocado Toast'),
('Tuna Salad'),
('Sweet Potato'),
('Grilled Vegetables'),
('Whole Wheat Pasta'),
('Lean Beef');

-- Insert diet templates
INSERT INTO diet_templates (name, description, diet_type, created_by) VALUES
('Weight Loss Standard', 'Balanced diet for weight loss', 'WEIGHT_LOSS', 1),
('Muscle Gain High Protein', 'High protein diet for muscle gain', 'MUSCLE_GAIN', 1),
('Athlete Maintenance', 'Diet for maintaining athletic performance', 'MAINTENANCE', 2),
('Swimmer Nutrition', 'Nutrition plan for swimmers', 'MAINTENANCE', 3);

-- Insert diet template dishes
INSERT INTO diet_template_dishes (template_id, dish_id, week_day, amount, meal_type) VALUES
(1, 1, 'MONDAY', 150.0, 'LUNCH'),
(1, 2, 'MONDAY', 100.0, 'LUNCH'),
(1, 3, 'MONDAY', 200.0, 'LUNCH'),
(1, 4, 'MONDAY', 150.0, 'DINNER'),
(1, 5, 'MONDAY', 150.0, 'DINNER'),
(1, 6, 'MONDAY', 200.0, 'SNACK'),
(2, 1, 'MONDAY', 200.0, 'BREAKFAST'),
(2, 7, 'MONDAY', 300.0, 'POST_WORKOUT'),
(2, 8, 'MONDAY', 150.0, 'BREAKFAST'),
(2, 9, 'MONDAY', 200.0, 'LUNCH'),
(3, 10, 'MONDAY', 150.0, 'BREAKFAST'),
(3, 11, 'MONDAY', 200.0, 'LUNCH'),
(3, 12, 'MONDAY', 150.0, 'DINNER'),
(4, 4, 'MONDAY', 200.0, 'LUNCH'),
(4, 13, 'MONDAY', 250.0, 'DINNER'),
(4, 14, 'MONDAY', 150.0, 'POST_WORKOUT');

-- Insert diets
INSERT INTO diets (name, description, diet_type, duration_days, created_by, start_at) VALUES
('Sarah Weight Loss', 'Custom weight loss plan for Sarah', 'WEIGHT_LOSS', 30, 1, NOW()),
('Mike Muscle Gain', 'High protein diet for Mike', 'MUSCLE_GAIN', 60, 1, NOW() + INTERVAL '1 day'),
('Emma Swimmer Diet', 'Nutrition for competitive swimming', 'MAINTENANCE', 90, 3, NOW() + INTERVAL '2 days'),
('David Cutting Phase', 'Diet for cutting phase', 'CUTTING', 45, 1, NOW() + INTERVAL '1 week');

-- Update athletes with their diet IDs
UPDATE athletes SET diet_id = 1 WHERE id = 1;
UPDATE athletes SET diet_id = 2 WHERE id = 2;
UPDATE athletes SET diet_id = 3 WHERE id = 3;
UPDATE athletes SET diet_id = 4 WHERE id = 4;

-- Insert diet dishes
INSERT INTO diet_dishes (diet_id, dish_id, week_day, amount, meal_type) VALUES
(1, 1, 'MONDAY', 150.0, 'LUNCH'),
(1, 2, 'MONDAY', 100.0, 'LUNCH'),
(1, 3, 'MONDAY', 200.0, 'LUNCH'),
(1, 4, 'MONDAY', 150.0, 'DINNER'),
(1, 5, 'MONDAY', 150.0, 'DINNER'),
(1, 6, 'MONDAY', 200.0, 'SNACK'),
(2, 1, 'MONDAY', 200.0, 'BREAKFAST'),
(2, 7, 'MONDAY', 300.0, 'POST_WORKOUT'),
(2, 8, 'MONDAY', 150.0, 'BREAKFAST'),
(2, 9, 'MONDAY', 200.0, 'LUNCH'),
(3, 10, 'MONDAY', 150.0, 'BREAKFAST'),
(3, 11, 'MONDAY', 200.0, 'LUNCH'),
(3, 12, 'MONDAY', 150.0, 'DINNER'),
(4, 4, 'MONDAY', 200.0, 'LUNCH'),
(4, 13, 'MONDAY', 250.0, 'DINNER'),
(4, 14, 'MONDAY', 150.0, 'POST_WORKOUT');

-- Insert completion diet statistics
INSERT INTO completion_diet_statistics (diet_id, athlete_id, complete_at) VALUES
(1, 1, NOW() - INTERVAL '1 day'),
(1, 1, NOW() - INTERVAL '2 days'),
(2, 2, NOW() - INTERVAL '1 day'),
(3, 3, NOW() - INTERVAL '3 days'),
(4, 4, NOW() - INTERVAL '1 week');

-- Insert conversations
INSERT INTO conversations (trainer_id, athlete_id, created_at, updated_at, last_activity_at) VALUES
(1, 2, NOW() - INTERVAL '1 week', NOW(), NOW()),
(1, 4, NOW() - INTERVAL '3 days', NOW() - INTERVAL '1 hour', NOW() - INTERVAL '1 hour'),
(3, 5, NOW() - INTERVAL '2 weeks', NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day'),
(3, 6, NOW() - INTERVAL '5 days', NOW(), NOW());

-- Insert conversation users
INSERT INTO conversation_users (conversation_id, user_id, last_read_message_id, unread_count, joined_at) VALUES
(1, 1, NULL, 0, NOW() - INTERVAL '1 week'),
(1, 2, NULL, 2, NOW() - INTERVAL '1 week'),
(2, 1, NULL, 0, NOW() - INTERVAL '3 days'),
(2, 4, NULL, 1, NOW() - INTERVAL '3 days'),
(3, 3, NULL, 0, NOW() - INTERVAL '2 weeks'),
(3, 5, NULL, 0, NOW() - INTERVAL '2 weeks'),
(4, 3, NULL, 0, NOW() - INTERVAL '5 days'),
(4, 6, NULL, 3, NOW() - INTERVAL '5 days');

-- Insert messages
INSERT INTO messages (message_id, conversation_id, sender_id, receiver_id, sender_name, content, message_type, sent_at, delivered_at, read_at, status) VALUES
('msg1', 1, 1, 2, 'John Trainer', 'Hi Sarah, how was your workout today?', 'TEXT', NOW() - INTERVAL '6 hours', NOW() - INTERVAL '6 hours', NULL, 'DELIVERED'),
('msg2', 1, 2, 1, 'Sarah Athlete', 'It was good! I completed all sets.', 'TEXT', NOW() - INTERVAL '5 hours', NOW() - INTERVAL '5 hours', NOW() - INTERVAL '4 hours', 'READ'),
('msg3', 1, 1, 2, 'John Trainer', 'Great! Any pain or discomfort?', 'TEXT', NOW() - INTERVAL '4 hours', NOW() - INTERVAL '4 hours', NULL, 'DELIVERED'),
('msg4', 2, 1, 4, 'John Trainer', 'Mike, remember our session tomorrow at 10am', 'TEXT', NOW() - INTERVAL '2 hours', NOW() - INTERVAL '2 hours', NULL, 'DELIVERED'),
('msg5', 3, 5, 3, 'David Lifter', 'Coach, I need to reschedule our Friday session', 'TEXT', NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day', 'READ'),
('msg6', 3, 3, 5, 'Mike Coach', 'No problem, what time works for you?', 'TEXT', NOW() - INTERVAL '23 hours', NOW() - INTERVAL '23 hours', NOW() - INTERVAL '22 hours', 'READ'),
('msg7', 4, 6, 3, 'Lisa Swimmer', 'I improved my lap time by 2 seconds!', 'TEXT', NOW() - INTERVAL '3 hours', NOW() - INTERVAL '3 hours', NULL, 'DELIVERED'),
('msg8', 4, 3, 6, 'Admin User', 'That''s amazing progress Lisa!', 'TEXT', NOW() - INTERVAL '2 hours', NOW() - INTERVAL '2 hours', NULL, 'DELIVERED'),
('msg9', 4, 6, 3, 'Lisa Swimmer', 'Thanks! The new drills helped a lot', 'TEXT', NOW() - INTERVAL '1 hour', NOW() - INTERVAL '1 hour', NULL, 'DELIVERED');

-- Insert active sessions
INSERT INTO active_sessions (user_id, conversation_id, session_id, connected_at, last_ping_at, user_agent, ip_address) VALUES
(1, 1, 'sess1', NOW() - INTERVAL '10 minutes', NOW(), 'Mozilla/5.0 (Windows NT 10.0)', '192.168.1.1'),
(2, 1, 'sess2', NOW() - INTERVAL '5 minutes', NOW() - INTERVAL '1 minute', 'Mozilla/5.0 (iPhone)', '192.168.1.2'),
(3, 3, 'sess3', NOW() - INTERVAL '30 minutes', NOW(), 'Mozilla/5.0 (Macintosh)', '192.168.1.3'),
(6, 4, 'sess4', NOW() - INTERVAL '15 minutes', NOW() - INTERVAL '5 minutes', 'Mozilla/5.0 (Android)', '192.168.1.4');

-- Insert sessions
INSERT INTO sessions (title, date_time, session_type, trainer_id, athlete_id, expected_duration_minutes, actual_duration_minutes, completed) VALUES
('Initial Assessment', NOW() - INTERVAL '1 week', 'ASSESSMENT', 1, 1, 60, 55, TRUE),
('Strength Training', NOW() - INTERVAL '3 days', 'TRAINING', 1, 1, 60, 60, TRUE),
('Cardio Session', NOW() - INTERVAL '2 days', 'TRAINING', 2, 2, 45, 50, TRUE),
('Swim Technique', NOW() - INTERVAL '1 day', 'TRAINING', 3, 3, 90, 95, TRUE),
('Powerlifting Form', NOW() - INTERVAL '5 days', 'TRAINING', 1, 4, 120, 110, TRUE),
('Nutrition Consultation', NOW() + INTERVAL '1 day', 'CONSULTATION', 1, 1, 30, NULL, FALSE),
('Follow Up', NOW() + INTERVAL '3 days', 'FOLLOW_UP', 1, 2, 45, NULL, FALSE),
('Swim Assessment', NOW() + INTERVAL '2 days', 'ASSESSMENT', 3, 3, 60, NULL, FALSE);

-- Insert notes
INSERT INTO notes (user_id, content) VALUES
(1, 'Sarah shows good form on squats but needs to work on depth'),
(1, 'Mike struggles with endurance - suggest more interval training'),
(3, 'Emma has excellent swimming technique but needs work on turns'),
(1, 'David is progressing well with deadlifts - consider increasing weight next session'),
(2, 'I felt some knee discomfort during yesterday''s workout'),
(4, 'I prefer morning sessions if possible'),
(3, 'Need to focus on breathing technique during freestyle'),
(1, 'Client prefers vegetarian options in meal plan');

-- Insert workout notes
INSERT INTO workout_notes (workout_id, note_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4);

-- Insert workout exercise notes
INSERT INTO workout_exercise_notes (workout_exercise_id, note_id) VALUES
(1, 1),
(9, 2),
(13, 3),
(16, 4);

-- Insert diet notes
INSERT INTO diet_notes (diet_id, note_id) VALUES
(1, 8),
(2, 6),
(3, 7),
(4, 5);

-- Insert diet dish notes
INSERT INTO diet_dish_notes (diet_dish_id, note_id) VALUES
(1, 8),
(7, 6),
(10, 7),
(13, 5);

-- Insert session notes
INSERT INTO session_notes (session_id, note_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 8);

-- Insert registration keys
INSERT INTO key_gen (key, trainer_id) VALUES
('ABCD1234', 1),
('EFGH5678', 1),
('IJKL9012', 2),
('MNOP3456', 3),
('QRST7890', 3);