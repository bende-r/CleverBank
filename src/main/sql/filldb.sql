INSERT INTO cleverbank.banks (bank_name)
VALUES ('CleverBank'),
       ('JPMorgan Chase'),
       ('Wells Fargo'),
       ('Citibank'),
       ('Goldman Sachs');

INSERT INTO cleverbank.users (user_name, user_surname, user_middlename)
VALUES ('Anna', 'Smirnova', 'Andreevna'),
       ('Alexey', 'Ivanov', 'Sergeevich'),
       ('Elena', 'Petrova', 'Ivanovna'),
       ('Sergey', 'Sidorov', 'Nikolaevich'),
       ('Marina', 'Kozlova', 'Dmitrievna'),
       ('Ivan', 'Pavlov', 'Alexandrovich'),
       ('Olga', 'Fedorova', 'Vladimirovna'),
       ('Dmitry', 'Morozov', 'Petrovich'),
       ('Tatiana', 'Andreeva', 'Sergeevna'),
       ('Pavel', 'Kuznetsov', 'Igorevich'),
       ('Irina', 'Grigorieva', 'Anatolievna'),
       ('Andrei', 'Sokolov', 'Denisovich'),
       ('Natalia', 'Mikhailova', 'Valentinovna'),
       ('Alexandra', 'Kozlova', 'Olegovna'),
       ('Vladimir', 'Petrov', 'Igorevich'),
       ('Oksana', 'Antonova', 'Andreevna'),
       ('Gleb', 'Ignatiev', 'Pavlovich'),
       ('Evgenia', 'Semenova', 'Artemovna'),
       ('Ilya', 'Kovalev', 'Sergeevich'),
       ('Elena', 'Savelieva', 'Mikhailovna');
/*LPAD(FLOOR(random() * 10000000000)::text, 10, '0')::text,*/
DO
$$
    DECLARE
        i          integer   := 1;
        currencies varchar[] := ARRAY ['USD', 'EUR', 'RUB', 'BYN'];
        characters varchar[] := ARRAY ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'];
    BEGIN
        WHILE i <= 40
            LOOP
                INSERT INTO cleverbank.scores (score_number, bank_id, currency, balance, user_id)
                VALUES (characters[1 + floor(random() * 26)] ||
                        array_to_string(ARRAY(SELECT characters[1 + floor(random() * 36)] FROM generate_series(1, 9)),
                                        ''),
                        random() * 4 + 1,
                        currencies[1 + floor(random() * 4)],
                        LPAD(FLOOR(random() * 1000000)::text, 6, '0')::money,
                        random() * 19 + 1);
                i := i + 1;
            END LOOP;
    END
$$;
