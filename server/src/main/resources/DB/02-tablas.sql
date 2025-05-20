-- Create enum types
CREATE TYPE "MediaType" AS ENUM ('IMAGE', 'AUDIO', 'VIDEO');
CREATE TYPE "BodyPart" AS ENUM ('LEG', 'ARM', 'SHOULDER', 'ABDOMINALS', 'BACK', 'CHEST');
CREATE TYPE "WeekDay" AS ENUM ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY');
CREATE TYPE "MessageType" AS ENUM ('IMAGE', 'AUDIO', 'VIDEO','TEXT');

-- Create tables
CREATE TABLE "resources" (
    "id" SERIAL PRIMARY KEY,
    "type" "MediaType" NOT NULL,
    "path" VARCHAR(255) NOT NULL
);

CREATE TABLE "user" (
    "id" SERIAL PRIMARY KEY,
    "name" VARCHAR(50) NOT NULL,
    "password" VARCHAR(250) NOT NULL,
    "email" VARCHAR(250) NOT NULL,
    "creationTime" TIMESTAMP NOT NULL,
    "userImage" INTEGER REFERENCES "resources"("id") ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE "certifications" (
    "id" SERIAL PRIMARY KEY,
    "name" VARCHAR(50) NOT NULL,
    "issuin_organization" VARCHAR(75) NOT NULL,
    "optainedDate" TIMESTAMP NOT NULL
);

CREATE TABLE "trainer" (
    "id" SERIAL PRIMARY KEY,
    "user_id" INTEGER NOT NULL REFERENCES "user"("id") ON DELETE CASCADE ON UPDATE CASCADE,
    "specialization" VARCHAR(50) NOT NULL,
    "years_of_experience" INTEGER NOT NULL,
    "bio" TEXT NOT NULL,
    "certifications" INTEGER[] -- Array of certification IDs
);

CREATE TABLE "socials" (
    "id" SERIAL PRIMARY KEY,
    "trainer_id" INTEGER NOT NULL REFERENCES "trainer"("id") ON DELETE CASCADE ON UPDATE CASCADE,
    "media" VARCHAR(25) NOT NULL,
    "links" VARCHAR(150) NOT NULL
);

CREATE TABLE "sportsmans" (
    "id" SERIAL PRIMARY KEY,
    "user_id" INTEGER NOT NULL REFERENCES "user"("id") ON DELETE CASCADE ON UPDATE CASCADE,
    "trainer_id" INTEGER REFERENCES "trainer"("id") ON DELETE SET NULL ON UPDATE CASCADE,
    "age" INTEGER NOT NULL,
    "weight" DOUBLE PRECISION,
    "height" DOUBLE PRECISION,
    "sex" BOOLEAN NOT NULL -- 1 Hombre 0 Mujer
);

CREATE TABLE "sportsman_status" (
    "id" SERIAL PRIMARY KEY,
    "user_id" INTEGER NOT NULL REFERENCES "user"("id"),
    "status" BOOLEAN NOT NULL,
    "last_active" TIMESTAMP NOT NULL,
    "needs_attention" BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE "notes" (
    "id" SERIAL PRIMARY KEY,
    "user_id" INTEGER NOT NULL REFERENCES "user"("id") ON DELETE CASCADE ON UPDATE CASCADE,
    "message" TEXT NOT NULL
);

CREATE TABLE "exercises" (
    "id" SERIAL PRIMARY KEY,
    "name" VARCHAR(200) NOT NULL,
    "body_part" "BodyPart" NOT NULL,
    "reps" INTEGER NOT NULL,
    "sets" INTEGER NOT NULL,
    "description" TEXT NOT NULL,
    "author" INTEGER REFERENCES "trainer"("id") ON DELETE SET NULL ON UPDATE CASCADE,
    "video" INTEGER REFERENCES "resources"("id") ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE "exercise_optional_links" (
    "exercise_id" INTEGER NOT NULL REFERENCES "exercises"("id") ON DELETE CASCADE,
    "optional_id" INTEGER NOT NULL REFERENCES "exercises"("id") ON DELETE CASCADE,
    CONSTRAINT "PK_Exercise_Optional" PRIMARY KEY ("exercise_id", "optional_id")
);

CREATE TABLE "chat" (
    "id" SERIAL PRIMARY KEY,
    "trainer_id" INTEGER REFERENCES "trainer"("id") ON DELETE SET NULL ON UPDATE CASCADE,
    "sportsman_id" INTEGER REFERENCES "sportsmans"("id") ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE "messages" (
    "id" SERIAL PRIMARY KEY,
    "chat_id" INTEGER NOT NULL REFERENCES "chat"("id") ON DELETE CASCADE ON UPDATE CASCADE,
    "user_id" INTEGER NOT NULL REFERENCES "user"("id") ON DELETE CASCADE ON UPDATE CASCADE,
    "message_text" VARCHAR(250) NOT NULL,
    "message_type" "MediaType" DEFAULT 'TEXT' NULL
);

CREATE TABLE "workouts" (
    "id" SERIAL PRIMARY KEY,
    "name" VARCHAR(100) NOT NULL,
    "description" TEXT,
    "sportsman_id" INTEGER NOT NULL REFERENCES "sportsmans"("id") ON DELETE CASCADE ON UPDATE CASCADE,
    "trainer_id" INTEGER NOT NULL REFERENCES "trainer"("id") ON DELETE CASCADE ON UPDATE CASCADE,
    "requires_assistance" BOOLEAN NOT NULL DEFAULT FALSE,
    "creation_date" TIMESTAMP NOT NULL,
    "duration_weeks" INTEGER NOT NULL
);

CREATE TABLE "workout_exercises" (
    "id" SERIAL PRIMARY KEY,
    "workout_id" INTEGER NOT NULL REFERENCES "workouts"("id") ON DELETE CASCADE ON UPDATE CASCADE,
    "exercise_id" INTEGER NOT NULL REFERENCES "exercises"("id") ON DELETE CASCADE ON UPDATE CASCADE,
    "week_day" "WeekDay" NOT NULL,
    "exercise_order" INTEGER NOT NULL,
    "reps" INTEGER NOT NULL,
    "sets" INTEGER NOT NULL,
    "note_id" INTEGER REFERENCES "notes"("id") ON DELETE SET NULL ON UPDATE CASCADE,
    "is_option" BOOLEAN NOT NULL DEFAULT FALSE,
    "parent_exercise" INTEGER REFERENCES "workout_exercises"("id") ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create index for workout_exercises
CREATE INDEX "idx_workout_exercises_workout_weekday_order" ON "workout_exercises" ("workout_id", "week_day", "exercise_order");

CREATE TABLE "workout_progress" (
    "id" SERIAL PRIMARY KEY,
    "workout_id" INTEGER NOT NULL REFERENCES "workouts"("id") ON DELETE CASCADE ON UPDATE CASCADE,
    "exercise_id" INTEGER NOT NULL REFERENCES "workout_exercises"("id") ON DELETE CASCADE ON UPDATE CASCADE,
    "week_day" "WeekDay" NOT NULL,
    "completion_date" TIMESTAMP NOT NULL,
    "completed" BOOLEAN NOT NULL DEFAULT FALSE,
    "feedback" TEXT,
    "actual_reps" INTEGER,
    "actual_sets" INTEGER,
    "perceived_difficulty" INTEGER
);

CREATE TABLE "workout_notes" (
    "workout_id" INTEGER NOT NULL REFERENCES "workouts"("id") ON DELETE CASCADE ON UPDATE CASCADE,
    "note_id" INTEGER NOT NULL REFERENCES "notes"("id") ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "PK_Workout_Note" PRIMARY KEY ("workout_id", "note_id")
);

CREATE TABLE "allergies" (
    "id" SERIAL PRIMARY KEY,
    "name" VARCHAR(100) NOT NULL
);

CREATE TABLE "sportsman_allergies" (
    "sportsman_id" INTEGER NOT NULL REFERENCES "sportsmans"("id"),
    "allergy_id" INTEGER NOT NULL REFERENCES "allergies"("id"),
    PRIMARY KEY ("sportsman_id", "allergy_id")
);

CREATE TABLE "diets" (
    "id" SERIAL PRIMARY KEY,
    "name" VARCHAR(100) NOT NULL,
    "sportsman_id" INTEGER NOT NULL REFERENCES "sportsmans"("id"),
    "initial_date" TIMESTAMP NOT NULL,
    "duration_days" BIGINT NOT NULL,
    "description" TEXT
);