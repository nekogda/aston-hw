# hw06-spring-boot

В репозитории содержится проект ДЗ.

### Build instructions

```shell
$ git clone https://github.com/nekogda/aston-hw.git
$ cd aston-hw/hw06-spring-boot
$ ./mvnw clean package
```

### Run instructions

Подразумевается, что у вас запущен интсанс PostgreSQL и к нему можно подключится с помощью:

```text
jdbc:postgresql://localhost:5432/postgres
username=postgres
password=postgres
```
Docker, если понадобится:

```shell
docker run --rm \
    -e POSTGRES_USER=postgres \
    -e POSTGRES_PASSWORD=postgres \
    -e POSTGRES_DB=postgres \
    -p 5432:5432 --name pgdb postgres:16.1 postgres -c log_statement=all
```

Запуск приложения (во время старта будут применяться миграции):

```shell
./mvnw spring-boot:run
```

### Description

Необходимо было перевсти hw05 на SpringBoot.

#### API

Swagger UI по адресу:

http://localhost:8080/swagger-ui/index.html
