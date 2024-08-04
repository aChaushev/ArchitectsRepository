-- AppConfig->DataSourceInitializer

-- Insert roles
INSERT INTO roles
VALUES (1, 'USER'),
       (2, 'ADMIN');

-- Insert users
-- PASSWORDS ARE "123"
INSERT
INTO `users` (`id`, `email`, `password`, `username`)
VALUES ('1', 'u1@u1',
        '61eafc22507b9bee09dc56b6669fa5cbffe0bde660d316057999e26787c9aaa694fd270abb7d7044d8b7688bb5b63860', 'user1');
INSERT
INTO `users` (`id`, `email`, `password`, `username`)
VALUES ('2', 'u2@u2',
        '5efc9fa94a00560866818c36823ab3ca13c36cee639000155cea193e4f08153cb5b117526495f113a45cd0e31adbd4b8', 'user2');
INSERT
INTO `users` (`id`, `email`, `password`, `username`)
VALUES ('3', 'u3@u3',
        '973e0c11e0d9c3539ed25890752aca05ad2b7254659df78cea1b7f597164683e27332d77ba616b7c9a4440161239124b', 'user3');

-- Insert user_roles mapping
INSERT INTO `users_roles` (`user_id`, `role_id`)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (3, 1);

-- Insert project types
INSERT INTO project_types (id, project_type_name, description) VALUES (1, 'RESIDENTIAL', 'Residential architects design homes, working with homeowners to design a custom home or adjust the design or layout of an existing home.');
INSERT INTO project_types (id, project_type_name, description) VALUES (2, 'COMMERCIAL', 'A commercial architect designs buildings for commercial purposes, such as skyscrapers, large office buildings, condos, and hotels, as well as bridges, schools and museums.');
INSERT INTO project_types (id, project_type_name, description) VALUES (3, 'LANDSCAPE', 'Landscape architects work on creating beautiful outdoor spaces as opposed to commercial properties or entire homes. Such spaces might include parks, college campuses, and garden areas.');
INSERT INTO project_types (id, project_type_name, description) VALUES (4, 'INTERIOR', 'Interior design architects work on the inside of buildings, getting the most out of both big and small spaces, using knowledge of color theory, function and feel of the materials');
INSERT INTO project_types (id, project_type_name, description) VALUES (5, 'URBAN', 'Urban design architect takes on the challenge of building for a much larger space, such as an entire block of houses or a whole town.');


-- Insert projects
INSERT INTO `projects` (`id`, `description`, `imageurl`, `input_date`, `name`, `price`,
                                             `arch_project_type_id`, `architect_id`)
VALUES ('2',
        'The Heydar Aliyev Center is a 57,500 m2 (619,000 sq ft) building complex in Baku, Azerbaijan, designed by Iraqi-British architect Zaha Hadid and noted for its distinctive architecture and flowing, curved style that eschews sharp angles.',
        'https://api.gharpedia.com/wp-content/uploads/2019/02/Heydar-Aliyev-Cultural-Centre-by-Zaha-Hadid-01-0101070001.jpg',
        '2012-05-10', 'Heydar Aliyev Cultural Centre', '250000000', '2', '2');

INSERT INTO `projects` (`id`, `description`, `imageurl`, `input_date`, `name`, `price`,
                                             `arch_project_type_id`, `architect_id`)
VALUES ('3',
        'Fallingwater is a house designed by the architect Frank Lloyd Wright in 1935. Situated in the Mill Run section of Stewart township, in the Laurel Highlands of southwest Pennsylvania, about 70 miles (110 km) southeast of Pittsburgh in the United States,[4] it is built partly over a waterfall on the Bear Run river. ',
        'https://upload.wikimedia.org/wikipedia/commons/thumb/b/bc/Fallingwater3.jpg/1920px-Fallingwater3.jpg',
        '1935-04-01', 'Fallingwater house', '3400000', '1', '3');
