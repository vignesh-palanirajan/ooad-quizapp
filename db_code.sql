CREATE DATABASE quizapp;

USE quizapp;

-- CREATE TABLE users (
--     id INT AUTO_INCREMENT PRIMARY KEY,
--     username VARCHAR(50) UNIQUE NOT NULL,
--     password VARCHAR(255) NOT NULL,
--     email TEXT 
-- );
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    role VARCHAR(20) NOT NULL DEFAULT 'Participant',
    CHECK (
        email REGEXP '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$'
        OR email IS NULL
    )
);

INSERT INTO users (username, password, email, role) 
VALUES ('admin', 'admin', 'admin@admin.com', 'admin');



CREATE TABLE questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question TEXT NOT NULL,
    option_a VARCHAR(255) NOT NULL,
    option_b VARCHAR(255) NOT NULL,
    option_c VARCHAR(255) NOT NULL,
    option_d VARCHAR(255) NOT NULL,
    correct_option CHAR(1) NOT NULL
);

INSERT INTO questions (question, option_a, option_b, option_c, option_d, correct_option) VALUES
-- General Knowledge
('What is the capital of France?', 'Berlin', 'Madrid', 'Paris', 'Rome', 'C'),
('Which is the smallest continent?', 'Europe', 'Australia', 'Antarctica', 'South America', 'B'),
('How many colors are in the rainbow?', '5', '6', '7', '8', 'C'),
('Which ocean is the largest?', 'Indian', 'Atlantic', 'Pacific', 'Arctic', 'C'),
('Which country is famous for pizza?', 'Germany', 'Italy', 'Spain', 'France', 'B'),
('What is the capital of Japan?', 'Seoul', 'Beijing', 'Tokyo', 'Bangkok', 'C'),
('Who was the first president of the United States?', 'Thomas Jefferson', 'George Washington', 'Abraham Lincoln', 'John Adams', 'B'),
('What is the national flower of India?', 'Rose', 'Lotus', 'Jasmine', 'Sunflower', 'B'),
('Which is the longest river in the world?', 'Amazon', 'Nile', 'Yangtze', 'Mississippi', 'B'),
('Which gas is most abundant in Earth’s atmosphere?', 'Oxygen', 'Nitrogen', 'Carbon Dioxide', 'Hydrogen', 'B'),
('The Great Wall of China is visible from the Moon.', 'True', 'False', '', '', 'B'),
('Australia is both a country and a continent.', 'True', 'False', '', '', 'A'),
('The currency of Brazil is Peso.', 'True', 'False', '', '', 'B'),
('There are 7 continents on Earth.', 'True', 'False', '', '', 'A'),
('Mount Everest is the tallest mountain in the world.', 'True', 'False', '', '', 'A'),

-- Science
('Which planet is known as the Red Planet?', 'Earth', 'Mars', 'Jupiter', 'Saturn', 'B'),
('Which element has the chemical symbol O?', 'Oxygen', 'Gold', 'Osmium', 'Iron', 'A'),
('What is the hardest natural substance on Earth?', 'Gold', 'Iron', 'Diamond', 'Platinum', 'C'),
('What is the human bodys largest organ?', 'Liver', 'Skin', 'Brain', 'Heart', 'B'),
('Which gas do plants absorb from the atmosphere?', 'Oxygen', 'Carbon Dioxide', 'Nitrogen', 'Hydrogen', 'B'),
('What does DNA stand for?', 'Deoxyribonucleic Acid', 'Dinucleic Acid', 'Dioxygen Acid', 'Dicarboxylic Acid', 'A'),
('Which blood type is the universal donor?', 'A', 'B', 'O', 'AB', 'C'),
('How many bones are there in an adult human body?', '206', '210', '300', '150', 'A'),
('What is the speed of light?', '300,000 km/s', '150,000 km/s', '100,000 km/s', '200,000 km/s', 'A'),
('What vitamin do you get from sunlight?', 'Vitamin A', 'Vitamin B', 'Vitamin C', 'Vitamin D', 'D'),
('Humans have four lungs.', 'True', 'False', '', '', 'B'),
('The sun is a star.', 'True', 'False', '', '', 'A'),
('Water boils at 100°C at sea level.', 'True', 'False', '', '', 'A'),
('Sound travels faster than light.', 'True', 'False', '', '', 'B'),
('DNA stands for Deoxyribonucleic Acid.', 'True', 'False', '', '', 'A'),

-- Mathematics
('What is 8 + 6?', '12', '13', '14', '15', 'C'),
('What is the square root of 144?', '10', '11', '12', '13', 'C'),
('What is 10 multiplied by 10?', '10', '50', '100', '200', 'C'),
('What is the value of pi (approx)?', '3.14', '2.72', '1.62', '4.18', 'A'),
('How many sides does a hexagon have?', '4', '5', '6', '7', 'C'),
('What is 25% of 200?', '25', '50', '75', '100', 'B'),
('If a triangle has angles 60°, 60°, and 60°, what type is it?', 'Scalene', 'Right-angled', 'Equilateral', 'Isosceles', 'C'),
('Solve: 12 ÷ 4 + 5 × 3?', '9', '18', '19', '21', 'C'),
('What is the sum of angles in a triangle?', '90°', '180°', '270°', '360°', 'B'),
('What is the Roman numeral for 50?', 'L', 'X', 'C', 'V', 'A'),
('Zero is an even number.', 'True', 'False', '', '', 'A'),
('A triangle has four sides.', 'True', 'False', '', '', 'B'),
('Pi is a rational number.', 'True', 'False', '', '', 'B'),
('The square root of 16 is 4.', 'True', 'False', '', '', 'A'),
('100 is a prime number.', 'True', 'False', '', '', 'B'),

-- History
('Who discovered America?', 'Christopher Columbus', 'Marco Polo', 'Vasco da Gama', 'James Cook', 'A'),
('In which year did World War II end?', '1939', '1943', '1945', '1950', 'C'),
('Who was the first Emperor of China?', 'Ming', 'Qin Shi Huang', 'Tang', 'Han', 'B'),
('Which ancient wonder was in Egypt?', 'Colossus of Rhodes', 'Hanging Gardens of Babylon', 'Great Pyramid of Giza', 'Statue of Zeus', 'C'),
('Which country built the Great Wall?', 'India', 'Japan', 'China', 'Russia', 'C'),
('Who was the first female Prime Minister of the UK?', 'Margaret Thatcher', 'Angela Merkel', 'Theresa May', 'Indira Gandhi', 'A'),
('When did the French Revolution begin?', '1789', '1800', '1850', '1901', 'A'),
('Who painted the Mona Lisa?', 'Leonardo da Vinci', 'Vincent van Gogh', 'Pablo Picasso', 'Claude Monet', 'A'),
('Which year did India gain independence?', '1945', '1947', '1950', '1965', 'B'),
('Who wrote "The Communist Manifesto"?', 'Karl Marx', 'Adam Smith', 'John Locke', 'Plato', 'A'),
('The Battle of Plassey was fought in 1757.', 'True', 'False', '', '', 'A'),
('Mahatma Gandhi was born in 1872.', 'True', 'False', '', '', 'B'),
('The Berlin Wall fell in 1989.', 'True', 'False', '', '', 'A'),
('The first Olympic Games were held in Rome.', 'True', 'False', '', '', 'B'),
('Julius Caesar was a Roman Emperor.', 'True', 'False', '', '', 'B'),

-- Technology
('What does CPU stand for?', 'Central Processing Unit', 'Central Program Unit', 'Computer Processing Unit', 'Computer Program Unit', 'A'),
('Which company created the iPhone?', 'Samsung', 'Nokia', 'Apple', 'Microsoft', 'C'),
('What is the main language used for web development?', 'Java', 'Python', 'HTML', 'C++', 'C'),
('What does RAM stand for?', 'Read-Access Memory', 'Random-Access Memory', 'Run-All Memory', 'Read-Apply Memory', 'B'),
('Which social media platform is owned by Meta?', 'Twitter', 'Facebook', 'Snapchat', 'LinkedIn', 'B'),
('What year was Google founded?', '1995', '1998', '2000', '2003', 'B'),
('Which programming language is used for Android development?', 'Swift', 'Java', 'C#', 'Ruby', 'B'),
('What is the full form of HTTP?', 'Hyper Transfer Text Protocol', 'Hypertext Transfer Protocol', 'Hypertext Processing Technology', 'High-Tech Transfer Protocol', 'B'),
('Which company created Windows?', 'Apple', 'Google', 'Microsoft', 'IBM', 'C'),
('Who is known as the father of computers?', 'Alan Turing', 'Charles Babbage', 'Bill Gates', 'Steve Jobs', 'B'),
('HTML is a programming language.', 'True', 'False', '', '', 'B'),
('Android is an operating system.', 'True', 'False', '', '', 'A'),
('Wi-Fi uses radio waves.', 'True', 'False', '', '', 'A'),
('Python is only used for web development.', 'True', 'False', '', '', 'B'),
('The first computer virus was created in the 1980s.', 'True', 'False', '', '', 'A'),

-- Geography
('Which is the largest desert in the world?', 'Sahara', 'Gobi', 'Kalahari', 'Thar', 'A'),
('Which country has the most population?', 'USA', 'China', 'India', 'Russia', 'B'),
('What is the longest mountain range?', 'Himalayas', 'Rockies', 'Andes', 'Alps', 'C'),
('Which ocean lies between Africa and Australia?', 'Atlantic', 'Indian', 'Pacific', 'Arctic', 'B'),
('Which continent has the most countries?', 'Asia', 'Africa', 'Europe', 'South America', 'B'),
('What is the capital of Canada?', 'Toronto', 'Vancouver', 'Ottawa', 'Montreal', 'C'),
('Which is the smallest country in the world?', 'Vatican City', 'Monaco', 'Liechtenstein', 'San Marino', 'A'),
('Which US state is known as the "Sunshine State"?', 'California', 'Florida', 'Texas', 'Hawaii', 'B'),
('What is the currency of Japan?', 'Yen', 'Won', 'Rupee', 'Peso', 'A'),
('Which country has the longest coastline?', 'USA', 'Australia', 'Canada', 'Russia', 'C');

INSERT INTO questions (question, option_a, option_b, option_c, option_d, correct_option) VALUES
-- General Knowledge
('What is the largest ocean on Earth?', 'Atlantic', 'Indian', 'Arctic', 'Pacific', 'D'),
('How many continents are there?', '5', '6', '7', '8', 'C'),
('Which is the smallest country in the world?', 'Monaco', 'Maldives', 'Vatican City', 'Liechtenstein', 'C'),
('What is the capital of Japan?', 'Seoul', 'Beijing', 'Tokyo', 'Shanghai', 'C'),
('Which animal is known as the "King of the Jungle"?', 'Tiger', 'Lion', 'Elephant', 'Leopard', 'B'),

-- Science & Nature
('What gas do humans breathe in?', 'Oxygen', 'Carbon Dioxide', 'Nitrogen', 'Hydrogen', 'A'),
('Which part of the plant conducts photosynthesis?', 'Root', 'Stem', 'Leaf', 'Flower', 'C'),
('What is the boiling point of water in Celsius?', '50', '100', '150', '200', 'B'),
('What planet is known as the "Blue Planet"?', 'Earth', 'Mars', 'Neptune', 'Venus', 'A'),
('Which gas makes up the majority of Earth’s atmosphere?', 'Oxygen', 'Carbon Dioxide', 'Nitrogen', 'Argon', 'C'),

-- Math
('What is 9 × 9?', '72', '81', '90', '99', 'B'),
('What is the square root of 64?', '6', '7', '8', '9', 'C'),
('If a triangle has angles 90°, 45°, and 45°, what type of triangle is it?', 'Scalene', 'Isosceles', 'Right', 'Equilateral', 'C'),
('What is the value of π (Pi) to two decimal places?', '3.12', '3.14', '3.16', '3.18', 'B'),
('Solve: 15 + (6 ÷ 2) × 3', '27', '30', '33', '36', 'B'),

-- History
('Who was the first President of the United States?', 'Thomas Jefferson', 'Abraham Lincoln', 'George Washington', 'John Adams', 'C'),
('In which year did World War II end?', '1943', '1945', '1947', '1950', 'B'),
('Which ancient civilization built the pyramids?', 'Romans', 'Greeks', 'Egyptians', 'Persians', 'C'),
('Who was known as the "Iron Lady"?', 'Margaret Thatcher', 'Indira Gandhi', 'Angela Merkel', 'Hillary Clinton', 'A'),
('What was the name of the ship that brought the Pilgrims to America in 1620?', 'Titanic', 'Mayflower', 'Santa Maria', 'Endeavour', 'B'),

-- Technology
('Who founded Microsoft?', 'Steve Jobs', 'Mark Zuckerberg', 'Bill Gates', 'Elon Musk', 'C'),
('What does CPU stand for?', 'Central Processing Unit', 'Computer Personal Unit', 'Central Power Unit', 'Central Processor Unit', 'A'),
('Which company developed the iPhone?', 'Samsung', 'Apple', 'Nokia', 'Google', 'B'),
('What does HTTP stand for?', 'Hyper Text Transfer Protocol', 'Hyperlink Text Transfer Program', 'Home Transfer Text Protocol', 'Hyperlink Transfer Text Path', 'A'),
('Which programming language is known for its "snake" logo?', 'C++', 'Python', 'Java', 'JavaScript', 'B'),

-- Entertainment & Sports
('Who directed the movie "Inception"?', 'Christopher Nolan', 'Steven Spielberg', 'James Cameron', 'Martin Scorsese', 'A'),
('What is the national sport of Canada?', 'Cricket', 'Basketball', 'Ice Hockey', 'Football', 'C'),
('How many players are there in a standard soccer team?', '9', '10', '11', '12', 'C'),
('Which superhero is known as the "Dark Knight"?', 'Superman', 'Iron Man', 'Batman', 'Spiderman', 'C'),
('Which artist painted the Mona Lisa?', 'Van Gogh', 'Michelangelo', 'Leonardo da Vinci', 'Picasso', 'C'),
('The movie “Titanic” was released in 1997.', 'True', 'False', '', '', 'A'),
('Harry Potter’s middle name is James.', 'True', 'False', '', '', 'A'),
('There are 5 Marvel Avengers in the original team.', 'True', 'False', '', '', 'A'),
('Friends is a science fiction TV show.', 'True', 'False', '', '', 'B'),
('The Oscars are awarded for music albums.', 'True', 'False', '', '', 'B'),

-- Literature & Language
('Who wrote "Romeo and Juliet"?', 'Charles Dickens', 'William Shakespeare', 'Jane Austen', 'Mark Twain', 'B'),
('What is the plural of "ox"?', 'Oxen', 'Oxes', 'Ox', 'Oxis', 'A'),
('Which word is a synonym for "happy"?', 'Sad', 'Angry', 'Joyful', 'Lonely', 'C'),
('What is the longest word in English without a vowel?', 'Myth', 'Rhythm', 'Crypt', 'Sky', 'B'),
('Which language has the most native speakers in the world?', 'English', 'Spanish', 'Mandarin', 'Hindi', 'C'),
('William Shakespeare wrote “The Odyssey”.', 'True', 'False', '', '', 'B'),
('The word “Palindrome” refers to a word spelled the same forwards and backwards.', 'True', 'False', '', '', 'A'),
('J.K. Rowling wrote “The Hobbit”.', 'True', 'False', '', '', 'B'),
('English is a Germanic language.', 'True', 'False', '', '', 'A'),
('A haiku has 5-7-5 syllable structure.', 'True', 'False', '', '', 'A'),

-- Geography & Capitals
('Which is the tallest mountain in the world?', 'K2', 'Mount Everest', 'Kilimanjaro', 'Denali', 'B'),
('Which country has the most time zones?', 'Russia', 'USA', 'France', 'China', 'C'),
('Which is the largest desert in the world?', 'Gobi', 'Sahara', 'Antarctica', 'Kalahari', 'C'),
('What is the capital of Canada?', 'Toronto', 'Montreal', 'Ottawa', 'Vancouver', 'C'),
('Which country is known as the "Land of the Rising Sun"?', 'China', 'Japan', 'Korea', 'Vietnam', 'B'),
('The Amazon River flows through Egypt.', 'True', 'False', '', '', 'B'),
('Africa is the second-largest continent.', 'True', 'False', '', '', 'A'),
('Russia is the largest country by area.', 'True', 'False', '', '', 'A'),
('The Sahara Desert is in South America.', 'True', 'False', '', '', 'B'),
('Japan is located in the Pacific Ocean.', 'True', 'False', '', '', 'A'),

-- Miscellaneous
('Which planet has the most moons?', 'Earth', 'Jupiter', 'Saturn', 'Mars', 'C'),
('How many bones are there in the adult human body?', '204', '206', '208', '210', 'B'),
('What is the chemical symbol for gold?', 'Au', 'Ag', 'Fe', 'Hg', 'A'),
('Which is the fastest land animal?', 'Cheetah', 'Leopard', 'Tiger', 'Horse', 'A'),
('Which is the longest river in the world?', 'Amazon', 'Nile', 'Yangtze', 'Mississippi', 'B'),
('Chess originated in China.', 'True', 'False', '', '', 'B'),
('A group of lions is called a pride.', 'True', 'False', '', '', 'A'),
('The human body has 306 bones.', 'True', 'False', '', '', 'B'),
('You can sneeze with your eyes open.', 'True', 'False', '', '', 'B'),
('Gold is heavier than silver.', 'True', 'False', '', '', 'A');

CREATE TABLE scores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    score INT,
    total_questions INT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

ALTER TABLE scores ADD COLUMN scoring_type VARCHAR(20);


