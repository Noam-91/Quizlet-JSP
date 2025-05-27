CREATE DATABASE IF NOT EXISTS quizlet;
USE quizlet;

DROP TABLE IF EXISTS User;
CREATE TABLE User(
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE KEY,
    password VARCHAR(255) NOT NULL,
    firstname VARCHAR(100),
    lastname VARCHAR(100),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_admin BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT
);

DROP TABLE IF EXISTS Category;
CREATE TABLE Category(
    category_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT DEFAULT 0,
    updated_by BIGINT
);

DROP TABLE IF EXISTS Quiz;
CREATE TABLE Quiz(
    quiz_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    category_id BIGINT,
    name VARCHAR(255) NOT NULL,
    duration INT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    Foreign Key (category_id) REFERENCES Category(category_id),
    Foreign Key (user_id) REFERENCES User(user_id)
);

DROP TABLE IF EXISTS Question;
CREATE TABLE Question(
    question_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_id BIGINT,
    description VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT DEFAULT 0,
    updated_by BIGINT,
    Foreign Key (category_id) REFERENCES Category(category_id)
);

DROP TABLE IF EXISTS Choice;
CREATE TABLE Choice(
    choice_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_id BIGINT,
    description VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT DEFAULT 0,
    updated_by BIGINT,
    Foreign Key (question_id) REFERENCES Question(question_id)
);

DROP TABLE IF EXISTS QuizQuestion;
CREATE TABLE QuizQuestion(
    qq_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    quiz_id BIGINT,
    question_id BIGINT,

    Foreign Key (quiz_id) REFERENCES Quiz(quiz_id),
    Foreign Key (question_id) REFERENCES Question(question_id)
);

DROP TABLE IF EXISTS QuizQuestionUserChoice;
CREATE TABLE QuizQuestionUserChoice(
    qquc_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    qq_id BIGINT,
    choice_id BIGINT,

    Foreign Key (qq_id) REFERENCES QuizQuestion(qq_id),
    Foreign Key (choice_id) REFERENCES Choice(choice_id)
);

DROP TABLE IF EXISTS Contact;
CREATE TABLE Contact(
    contact_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    subject VARCHAR(255) NOT NULL,
    message VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_solved BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT
);

INSERT User(email, password, firstname, lastname, is_active, is_admin)
VALUES('admin', '$2a$10$85njtko.4ENCZQF4.i1QjuoT2UYJujVPVUCWYvi3N44/mPkGquE/u', 'Admin', 'Admin', TRUE, TRUE);

INSERT Category(category_id, name, is_active)
VALUES('1', 'Photoshop', TRUE),
       ('2', 'Electrical Engineering', TRUE),
       ('3', 'Dota2', TRUE);

-- questions and answers about photoshop
INSERT Question(question_id, category_id, description, is_active)
VALUES('1', '1', 'Which of the following tools is primarily used for non-destructively hiding parts of a layer without permanently erasing them?', TRUE),
       ('2', '1', 'When adjusting the colors in an image to make them more vibrant or less saturated, which adjustment layer is commonly used?', TRUE),
       ('3', '1', 'Which file format is best suited for saving a photographic image with full color depth and transparency, typically used for web graphics?', TRUE),
       ('4', '1', 'Which of the following tools in Photoshop can be used for selection?', TRUE),
       ('5', '1', 'Which features in Photoshop are used for non-destructive editing?', TRUE);
INSERT Choice(choice_id, question_id, description, is_active, is_correct)
VALUES('1', '1', 'Eraser Tool', TRUE, FALSE),
       ('2', '1', 'Lasso Tool', TRUE, FALSE),
       ('3', '1', 'Magic Wand Tool', TRUE, FALSE),
       ('4', '1', 'Layer Mask', TRUE, TRUE),
       ('5', '2', 'Brightness/Contrast', TRUE, FALSE),
       ('6', '2', 'Curves', TRUE, TRUE),
       ('7', '2', 'Hue/Saturation', TRUE, TRUE),
       ('8', '2', 'Black & White', TRUE, FALSE),
       ('9', '3', 'JPEG', TRUE, FALSE),
       ('10', '3', 'GIF', TRUE, FALSE),
       ('11', '3', 'PNG', TRUE, TRUE),
       ('12', '3', 'TIFF', TRUE, FALSE),
       ('13', '4', 'Lasso Tool', TRUE, TRUE),
       ('14', '4', 'Brush Tool', TRUE, FALSE),
       ('15', '4', 'Magic Wand Tool', TRUE, TRUE),
       ('16', '4', 'Marquee Tool', TRUE, TRUE),
       ('17', '4', 'Clone Stamp Tool', TRUE, FALSE),
       ('18', '5', 'Adjustment Layers', TRUE, TRUE),
       ('19', '5', 'Layer Masks', TRUE, TRUE),
       ('20', '5', 'Eraser Tool', TRUE, FALSE),
       ('21', '5', 'Smart Objects', TRUE, TRUE),
       ('22', '5', 'Flatten Image', TRUE, FALSE);

-- questions and answers about EE
INSERT Question(question_id, category_id, description, is_active)
VALUES('6', '2', 'In a simple DC circuit, if the voltage across a resistor is doubled while the resistance remains constant, what happens to the current flowing through the resistor?', TRUE),
       ('7', '2', 'When a capacitor is connected to a DC voltage source through a resistor, what is the state of the capacitor after a long period (approaching steady-state conditions)?', TRUE),
       ('8', '2', 'In an AC circuit containing a resistor and an inductor in series, the total opposition to current flow is called:', TRUE),
       ('9', '2', 'Which of the following are true statements about Ohm''s Law?', TRUE),
       ('10', '2', 'Which of the following components can store energy?', TRUE);
INSERT Choice(choice_id, question_id, description, is_active, is_correct)
VALUES('23', '6', 'It is halved.', TRUE, FALSE),
       ('24', '6', 'It remains the same.', TRUE, FALSE),
       ('25', '6', 'It is doubled.', TRUE, TRUE),
       ('26', '6', 'It increases by a factor of four.', TRUE, FALSE),
       ('27', '7', 'It acts as a short circuit, with current flowing continuously.', TRUE, FALSE),
       ('28', '7', 'It acts as an open circuit, blocking the flow of DC current.', TRUE, TRUE),
       ('29', '7', 'It continuously discharges and recharges.', TRUE, FALSE),
       ('30', '7', 'Its voltage fluctuates sinusoidally.', TRUE, FALSE),
       ('31', '8', 'Resistance', TRUE, FALSE),
       ('32', '8', 'Reactance', TRUE, FALSE),
       ('33', '8', 'Conductance', TRUE, FALSE),
       ('34', '8', 'Impedance', TRUE, TRUE),
       ('35', '9', 'It defines the relationship between voltage, current, and resistance', TRUE, TRUE),
       ('36', '9', 'It is expressed as V = IR', TRUE, TRUE),
       ('37', '9', 'It only applies to superconductors.', TRUE, FALSE),
       ('38', '9', 'It is not valid for non-linear components like diodes.', TRUE, TRUE),
       ('39', '9', 'It explains electromagnetic induction.', TRUE, FALSE),
       ('40', '10', 'Resistor', TRUE, FALSE),
       ('41', '10', 'Capacitor', TRUE, TRUE),
       ('42', '10', 'Inductor', TRUE, TRUE),
       ('43', '10', 'Transformer', TRUE, FALSE),
       ('44', '10', 'Battery', TRUE, TRUE);


-- questions and answers about dota2.
INSERT Question(question_id, category_id, description, is_active)
VALUES('11', '3', 'Which of the following is a benefit provided by activating a Black King Bar (BKB)?', TRUE),
       ('12', '3', 'When attempting to "stack" multiple neutral creep camps simultaneously, approximately how many seconds before the minute mark does the current camp spawn box typically become empty, allowing new creeps to spawn and the stack to register?', TRUE),
       ('13', '3', 'Which hero''s Aghanim''s Scepter upgrade grants them a new ability called "Tree Grab," allowing them to pick up a tree and use it as a temporary weapon with bonus damage?', TRUE),
       ('14', '3', 'Which of the following actions grant gold to a player in Dota 2?', TRUE),
       ('15', '3', 'Which of the following are Intelligence heroes in Dota 2?', TRUE);
INSERT Choice(choice_id, question_id, description, is_active, is_correct)
VALUES('45', '11', '100% magic resistance.', TRUE, FALSE),
       ('46', '11', 'Causes the caster''s model size to increase by 30%', TRUE, TRUE),
       ('47', '11', 'Applies spell immunity', TRUE, FALSE),
       ('48', '11', 'Provides a large boost to attack damage.', TRUE, FALSE),
       ('49', '12', '0-3 seconds', TRUE, FALSE),
       ('50', '12', '5-7 seconds', TRUE, TRUE),
       ('51', '12', '10-12 seconds', TRUE, FALSE),
       ('52', '12', '15-17 seconds', TRUE, FALSE),
       ('53', '13', 'Timbersaw', TRUE, FALSE),
       ('54', '13', 'Nature''s Prophet', TRUE, FALSE),
       ('55', '13', 'Tiny', TRUE, TRUE),
       ('56', '13', 'Treant Protector', TRUE, FALSE),
       ('57', '14', 'Last-hitting enemy creeps', TRUE, TRUE),
       ('58', '14', 'Assisting in a hero kill', TRUE, TRUE),
       ('59', '14', 'Destroying enemy towers', TRUE, TRUE),
       ('60', '14', 'Using Midas Hand', TRUE, TRUE),
       ('61', '14', 'Killing Roshan', TRUE, TRUE),
       ('62', '15', 'Nature''s Prophet', TRUE, FALSE),
       ('63', '15', 'Wind Ranger', TRUE, FALSE),
       ('64', '15', 'Chen', TRUE, FALSE),
       ('65', '15', 'Storm Spirit', TRUE, TRUE),
       ('66', '15', 'Ogre Magi', TRUE, FALSE);
