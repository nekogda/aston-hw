# hw05-hibernate

В репозитории содержится проект ДЗ.

### Build instructions

```shell
$ git clone https://github.com/nekogda/aston-hw.git
$ cd aston-hw
$ ./mvnw clean package
```

Артефакт в формате `war`. Проверялся развертыванием на 10-м [tomcat](https://tomcat.apache.org/download-10.cgi).

### Run instructions

Подразумевается, что у вас запущен интсанс PostgreSQL и к нему можно подключится с помощью:

```text
jdbc:postgresql://localhost:5432/postgres
username=postgres
password=postgres
```
Перед запуском нужно будет применить миграции:

```shell
$ ./mvnw -pl hw05-hibernate org.liquibase:liquibase-maven-plugin:update
```

Docker, если понадобится:

```shell
docker run --rm \
    -e POSTGRES_USER=postgres \
    -e POSTGRES_PASSWORD=postgres \
    -e POSTGRES_DB=postgres \
    -p 5432:5432 --name pgdb postgres:16.1 postgres -c log_statement=all
```

### Description

Необходимо было перевсти hw03 на работу с Hibernate.

#### API

Примеры запросов для проверки

```shell
#1 - multiple get
curl -v -X GET http://localhost:8080/hw05-hibernate-1.0-SNAPSHOT/app/users
#2 - single get
curl -v -X GET http://localhost:8080/hw05-hibernate-1.0-SNAPSHOT/app/users/fooUser
#3 - create
curl -v -X POST http://localhost:8080/hw05-hibernate-1.0-SNAPSHOT/app/users -H 'Content-Type: application/json' -d '{"login": "fooUserA", "password": "password"}'
#4 - change password
curl -v -X PUT http://localhost:8080/hw05-hibernate-1.0-SNAPSHOT/app/users/fooUser -H 'Content-Type: application/json' -d '{"oldPassword": "password", "newPassword": "newPassword"}'
#5 - logIn
curl -v -X POST http://localhost:8080/hw05-hibernate-1.0-SNAPSHOT/app/login -H 'Content-Type: application/json' -d '{"login": "fooUser", "password": "password"}'
```