
-- WORKOUTS (3 por deportista)
DO $$
DECLARE
    sm_id INTEGER;
    tr_id INTEGER;
    i INTEGER := 1;
BEGIN
    FOR sm_id, tr_id IN SELECT id, trainer_id FROM sportsmans LOOP
        FOR i IN 1..3 LOOP
            INSERT INTO "workouts" ("name", "description", "sportsman_id", "trainer_id", "requires_assistance", "creation_date", "duration_weeks")
            VALUES (
                'Entreno #' || i || ' para deportista ' || sm_id,
                'Plan personalizado nivel ' || i,
                sm_id,
                tr_id,
                (i % 2 = 0),
                NOW() - (i || ' weeks')::interval,
                4
            );
        END LOOP;
    END LOOP;
END $$;

-- WORKOUT_EXERCISES y PROGRESO
DO $$
DECLARE
    w_id INTEGER;
    e_id INTEGER;
    order_num INTEGER := 1;
    ex_idx INTEGER := 1;
BEGIN
    FOR w_id IN SELECT id FROM workouts LOOP
        FOR e_id IN SELECT id FROM exercises ORDER BY random() LIMIT 5 LOOP
            INSERT INTO "workout_exercises" ("workout_id", "exercise_id", "week_day", "exercise_order", "reps", "sets", "note_id", "is_option")
            VALUES (w_id, e_id, 'MONDAY', order_num, 10 + ex_idx, 3 + ex_idx % 2, NULL, FALSE);

            INSERT INTO "workout_progress" ("workout_id", "exercise_id", "week_day", "completion_date", "completed", "feedback", "actual_reps", "actual_sets", "perceived_difficulty")
            VALUES (w_id, currval('workout_exercises_id_seq'), 'MONDAY', NOW(), TRUE, 'Completado sin problema.', 10 + ex_idx, 3 + ex_idx % 2, 3 + ex_idx % 4);

            order_num := order_num + 1;
            ex_idx := ex_idx + 1;
        END LOOP;
        order_num := 1;
        ex_idx := 1;
    END LOOP;
END $$;
