# Приложение Clever-Bank

Данное JAVA приложение разработано для работы с базами данных банков. Его главной задачей является реализация операций
по управлению средствами на счетах пользователей: пополнение счёта, списание средств, перевод со счёта на счёт.

## Начало работы

Пред запуском приложения у вас на комьютере должно быть установлено следующее программное обеспечение:

- JAVA 8
- JDK 17
- Intellij Idea
- PostgreSQL
- Gradle

При установке PostgreSQL вам необходимо создать учётную запись пользователя в базе данных и запомнить имя пользователя и
пароль. Они в дальнейшем понадобятся для авторизации в базе данных. Так же необходимо убедиться, что при установке
PostgreSQL был выбран порт по умолчанию (<b>5432</b>).

## Запуск

Для запуска программы вам необходимо загрузить zip-архив из данного репозитория и распаковать его на своём
компьютере,после чего открыть в Intellij Idea. Либо загрузить проект напрямую в Intellij Idea, используя её встроенные
средства для работы с GitHub. А далее необходимо в меню Build собрать проект и запустить класс CleverBankApp.

Либо в корневой папке проекта вызвать команду:

```
gradle build
gradle run
```

Либо

```
gradlew.bat run
```

Данные команды запустят сборщик проектов и запустят сам проект.

## Использование

После запуска программы вам будет необходимо ввести имя учётной записи в PostgreSQL:

```
 Enter PostgreSQL Username:
> postgres
```

А так же пароль от этой учётной записи:

```
Enter password:
> postgres
```

Далее вы получите сообщение о успешном подключении к базе данных. После этого перед вами появится сообщение,
спрашивающее, желаете ли вы создать базу данных:

```
Generate a database? Y/N
```

В случае если вы введёте Y, и если база данных не существует, то она будет создана. Далее появится сообщение,
спрашивающее, желаете ли вы заполнить базу данных информацией:

```
Fill the database with information? Y/N
```

Если вы введёте Y, в базу данных будут введены заранее подготовленные записи. Эту операцию можно проводить
неограниченное количество раз при каждом запуске приложения, но это приведёт к тому, что данные начнут повторятся.

Файл со скриптом для создания базы данных и её заполнения находится по следующему пути: */src/main/sql*

Создаваемая база данных содержит следующие таблицы:

- Banks
    - id
    - bank_name
- Users
    - id
    - user_name
    - user_surname
    - user_middlename
- Scores
    - id
    - score_number
    - bank_id
    - currency
    - balance
    - user_id
- Transactions
    - id
    - sender_score_number
    - recipient_score_number
    - transfer_amount
    - date

Между таблицами содержаться отношения, которые можно отследить по названию полей, а так же сама база данных
соответствует трьетьей нормальной форме баз данных

## CRUD

В проекте реализован полный набор CRUD(Create-создание, Read-чтение, Update-обновление, Delete-удаление) операций для
работы с таблицами в базе данных.
Чтобы воспользоваться данными операциями вам необходмо в главном меню программы

```
---------Menu---------
1. Score operations
2. Tables operations
3. Account statement
4. Exit
```

выбрать пункт под номером 2. В данном пунке ты увидите список доступный операций:

```
---------Tables---------
1. Show tables
2. Select an entry from the table
3. Add an entry to the table
4. Delete an entry from the table
5. Edit an entry in the table
6. Exit
```

**1. Вывод таблицы.** В данном пункте вам необходимо ввести имя таблицы, которую вы хотите просмотреть. Например:

```
-----Tables list-----
Banks
Users
Scores
Transactions

Enter the name of the table you want to view: Banks
```

и будет выведенследующий результат:

```
id	bank_name	
1	CleverBank	
2	JPMorgan Chase	
3	Wells Fargo	
4	Citibank	
5	Goldman Sachs	
```

**2. Выбор записей из таблицы.** В данном пункте вам необходимо ввести имя таблицы, поле таблицы и значение этого поля,
по которому будет происходить поиск записей. Пример:

```
-----Tables list-----
Banks
Users
Scores
Transactions

Enter the name of the table from which you want to get an entry: scores
Enter the name of the field by which the selection will take place: bank_id
Enter the value by which the selection will take place: 2
```

Будет получен следующий результат:

```
id	score_number	bank_id	     currency	balance	      user_id	
3	AJ95HAFLHI	    2	        RUB	$102,353.00	18	
6	SVNONAB39R	    2	        USD	$706,202.00	4	
9	IP679ATUV0	    2	        RUB	$31,431.00	16	
11	DZNS4J7D4D	    2	        USD	$199,038.00	12	
15	YXM2655V94	    2	        USD	$173,233.00	5	
18	RNSM501SX6	    2	        USD	$447,815.00	2	
32	C789X8ZHBE	    2	        RUB	$341,981.00	8	
35	E6CE81P5KM	    2	        USD	$730,106.00	11	
37	TODZOTQ9QD	    2	        RUB	$447,457.00	10	

```

**3. Добавление записи в таблицу.**  В данном пункте реализована функция, которая, после того как вы ввели имя таблицы,
находит имена всех столбцов в таблице. Затем последовательно выводит их и считывает введённые зачения, после чего
добавляет запись в таблицу. Пример:

```
-----Tables list-----
Banks
Users
Scores
Transactions

Enter the name of the table you want to add the entry to: users
Enter a value for the field user_name: Ivan
Enter a value for the field user_surname: Ivanov
Enter a value for the field user_middlename: Ivanovich
```

После успешного добавления записи появляется сообщение:
*Row has been added to the table*

**4. Удаление записи из таблицы.** В данном пункте для удаления записи вам наобходимо просто ввести индентификатор(id)
записи в таблице, которую вы хотите удалить. Пример:

```
-----Tables list-----
Banks
Users
Scores
Transactions

Enter the name of the table you want to delete an entry to: banks
Enter the ID of the record you want to delete: 5
```

После успешного выполнения опрарации появится сообщение:
*Row has been deleted from the table*

**5. Изменение записи в таблице.** В данном пункте вам необходимо проделать операцию, аналогичную операции добавления
строки, только нужно сначала ввести id изменяемой строки. Пример:

```
-----Tables list-----
Banks
Users
Scores
Transactions

Enter the name of the table you want to change the row for: banks
Enter the ID of the row you want to change: 4
Enter a value for the field bank_name: NemBank
```

После успешного выполнения опрарации появится сообщение:
*Row has been updated in the table*

## Операции со счетами

Если в главном меню вы выбрали пункт 1, то вы попадёте в меню операций по работе со счетами. В ней доступно три
действия:

- пополнение счёта
- списание средств со счёта
- перевод со счёта на счёт

В каждом из этих пунктов всё,что требуется, это ввести номер счёта (номера в счучае перевода) и сумму перевода. Например
рассмотрим перевод со счёта на счёт:

 ```
Enter input score number: IKVTX7NZ8D
Enter output score number: UXPXIC3ZHN
Enter deposit amount: 10000.01
 ```

После успешного выполнения появятся сообщения:

*Row has been added to the table* - значит что строка добавлена в таблицу *Transactions*
*Transfer was complite*
*PDF-file is created and saved in C:\****\Clever-Bank\checks\3-9-2023-20-53-11.pdf* - показывает куда был сохранён чек
после данной операции.

Созданный чек имеет следующий вид:

```
--------------------------------------------------
|                  Bank check                    |
| Check:                              3043920447 |
| 3-9-2023                              20:53:11 |
| Operation type:                    Transaction |
| Sender's bank:                         NemBank |
| Recipient's bank:                      NemBank |
| Output score:                       UXPXIC3ZHN |
| Input score:                        IKVTX7NZ8D |
| Sum:                                  10000,01 |
--------------------------------------------------
```

После операций по пополнению счёта будел такой чек:

```
--------------------------------------------------
|                   Bank check                   |
| Check:                              1343362973 |
| 3-9-2023                              14:04:17 |
| Operation type:                        Deposit |
| Recipient's bank:                  Wells Fargo |
| Input score:                        IUPG32YK7L |
| Sum:                                     12222 |
--------------------------------------------------
```

Все чеки сохраняются в автоматически создаваемую папку checks в корне проекта

## Выписки со счёта

Для получения выписки по всем операциям со счётом вам необходимо в главном меню нужно выбрать пункт 3.
Далее необходимо ввести id пользователя, по чьим счетам вы хотите получить выписки. Далее специальный метод получит
информацию по каждому счёту, принадлежащему пользователю и добавит их в папку account_statements, создаваемую
автоматически в корне проекта. Выписка по операцием со счётом имеет следующий вид:

```
---------------------------------------------------------------------
|                         Account Statement                         |
| Client:                                  Alexey Ivanov Sergeevich |
| Score number:                                          URTRIE707F |
| Period:                                       3.8.2023 - 3.9.2023 |
| Currency:                                                     BYN |
| Balance:                                                   179745 |
| Date of statement creation:                      3.9.2023 14:9:36 |
---------------------------------------------------------------------
|        Date         |          Note           |      Amount       |
---------------------------------------------------------------------
| 2023-09-03 12:09:00 | Receipt from TWMINR1G27 | $1,233.00
| 2023-09-03 12:25:00 | Receipt from URTRIE707F | $1,333.00
---------------------------------------------------------------------
```

Для получения выписки по всех поступлениям и списаниям средств в главном меню нужно выбрать пунтк 4.
Далее необходимо ввести id пользователя, по чьим счетам вы хотите получить выписки. Далее специальный метод получит
информацию по каждому счёту, принадлежащему пользователю и добавит их в папку money_statements, создаваемую
автоматически в корне проекта. Выписка по операцием со счётом имеет следующий вид:

```
---------------------------------------------------------------------
|                         Account Statement                         |
| Client:                                 Andrei Sokolov Denisovich |
| Score number:                                          IKVTX7NZ8D |
| Period:                                       3.9.2003 - 3.9.2023 |
| Currency:                                                     RUB |
| Balance:                                                932903,31 |
| Date of statement creation:                     3.9.2023 22:59:49 |
---------------------------------------------------------------------
|               Income           |               Outcome            |
---------------------------------------------------------------------
|                       23580,31 |                           -32311 |
---------------------------------------------------------------------
```

## Завершение работы

Чтобы завершить работу приллжения в главном меню необходимо выбрать пункт 4. После его выбора соединение с базой данных
будет закрыто и программа завершиться с кодом 0.