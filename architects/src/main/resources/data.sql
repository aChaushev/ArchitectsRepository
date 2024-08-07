-- Create roles table if not exists
CREATE TABLE IF NOT EXISTS roles (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
    );

-- Insert roles
INSERT INTO roles
VALUES (1, 'USER'),
       (2, 'ADMIN');

-- Create users table if not exists
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL
    );

-- Insert users
-- PASSWORDS ARE "123"
INSERT INTO users (id, email, password, username)
VALUES ('1', 'u1@u1', '61eafc22507b9bee09dc56b6669fa5cbffe0bde660d316057999e26787c9aaa694fd270abb7d7044d8b7688bb5b63860', 'user1');
INSERT INTO users (id, email, password, username)
VALUES ('2', 'u2@u2', '5efc9fa94a00560866818c36823ab3ca13c36cee639000155cea193e4f08153cb5b117526495f113a45cd0e31adbd4b8', 'user2');
INSERT INTO users (id, email, password, username)
VALUES ('3', 'u3@u3', '973e0c11e0d9c3539ed25890752aca05ad2b7254659df78cea1b7f597164683e27332d77ba616b7c9a4440161239124b', 'user3');

-- Create users_roles table if not exists
CREATE TABLE IF NOT EXISTS users_roles (
    user_id INT,
    role_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
    );

-- Insert user_roles mapping
INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (3, 1);

-- Create project_types table if not exists
CREATE TABLE IF NOT EXISTS project_types (
    id INT PRIMARY KEY,
    project_type_name VARCHAR(255) NOT NULL,
    description TEXT
    );

-- Insert project types
INSERT INTO project_types (id, project_type_name, description)
VALUES (1, 'RESIDENTIAL', 'Residential architects design homes, working with homeowners to design a custom home or adjust the design or layout of an existing home.');
INSERT INTO project_types (id, project_type_name, description)
VALUES (2, 'COMMERCIAL', 'A commercial architect designs buildings for commercial purposes, such as skyscrapers, large office buildings, condos, and hotels, as well as bridges, schools and museums.');
INSERT INTO project_types (id, project_type_name, description)
VALUES (3, 'LANDSCAPE', 'Landscape architects work on creating beautiful outdoor spaces as opposed to commercial properties or entire homes. Such spaces might include parks, college campuses, and garden areas.');
INSERT INTO project_types (id, project_type_name, description)
VALUES (4, 'INTERIOR', 'Interior design architects work on the inside of buildings, getting the most out of both big and small spaces, using knowledge of color theory, function and feel of the materials');
INSERT INTO project_types (id, project_type_name, description)
VALUES (5, 'URBAN', 'Urban design architect takes on the challenge of building for a much larger space, such as an entire block of houses or a whole town.');

-- Create projects table if not exists
CREATE TABLE IF NOT EXISTS projects (
    id INT PRIMARY KEY,
    description TEXT,
    imageurl VARCHAR(255),
    input_date DATE,
    name VARCHAR(255),
    price DECIMAL(15, 2),
    arch_project_type_id INT,
    architect_id INT,
    FOREIGN KEY (arch_project_type_id) REFERENCES project_types(id),
    FOREIGN KEY (architect_id) REFERENCES users(id)
    );

-- Insert projects
INSERT INTO projects (id, description, imageurl, input_date, name, price, arch_project_type_id, architect_id)
VALUES ('1', 'The Guggenheim Museum Bilbao is a museum of modern and contemporary art in Bilbao (Biscay), Spain. It is one of several museums affiliated to the Solomon R. Guggenheim Foundation and features permanent and visiting exhibits of works by Spanish and international artists.', 'https://upload.wikimedia.org/wikipedia/commons/thumb/c/c7/Museo_Guggenheim%2C_Bilbao_%2831273245344%29.jpg/1920px-Museo_Guggenheim%2C_Bilbao_%2831273245344%29.jpg', '1997-10-18', 'Guggenheim Museum Bilbao', '230000000', '2', '1');

INSERT INTO projects (id, description, imageurl, input_date, name, price, arch_project_type_id, architect_id)
VALUES ('2', 'The Heydar Aliyev Center is a 57,500 m2 (619,000 sq ft) building complex in Baku, Azerbaijan, designed by Iraqi-British architect Zaha Hadid and noted for its distinctive architecture and flowing, curved style that eschews sharp angles.', 'https://api.gharpedia.com/wp-content/uploads/2019/02/Heydar-Aliyev-Cultural-Centre-by-Zaha-Hadid-01-0101070001.jpg', '2012-05-10', 'Heydar Aliyev Cultural Centre', '250000000', '2', '2');

INSERT INTO projects (id, description, imageurl, input_date, name, price, arch_project_type_id, architect_id)
VALUES ('3', 'Fallingwater is a house designed by the architect Frank Lloyd Wright in 1935. Situated in the Mill Run section of Stewart township, in the Laurel Highlands of southwest Pennsylvania, about 70 miles (110 km) southeast of Pittsburgh in the United States,[4] it is built partly over a waterfall on the Bear Run river.', 'https://upload.wikimedia.org/wikipedia/commons/thumb/b/bc/Fallingwater3.jpg/1920px-Fallingwater3.jpg', '1935-04-01', 'Fallingwater house', '3400000', '1', '3');
