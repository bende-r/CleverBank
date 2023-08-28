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
/*
DO
$$
    DECLARE
        i integer := 1;
    BEGIN
        -- Цикл для вставки данных
        WHILE i <= 4
            LOOP
                INSERT INTO cleverbank.scores (score_number, bank_id, currency, balance, user_id)
                VALUES (LPAD(FLOOR(random() * 10000000000)::text, 10, '0'), LPAD(FLOOR(random() * 10)::text, 1, '0'),
                        'USD',
                        LPAD(FLOOR(random() * 1000000)::text, 6, '0'), LPAD(FLOOR(random() * 100)::text, 2, '0'));
                i := i + 1;
            END LOOP;
    END
$$;
*/