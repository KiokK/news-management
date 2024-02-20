## Technologies

- java 17
- springframework.boot 3.2
- spring-cloud
- spring-data-jpa
- spring aop
- openfeign
- redis
- lombok
- mapstruct
- testcontainers
- spring-boot-test
- postgresql
- liquibase
- docker

## Structure
 - [bd_bases](bd_bases)
   - [create_db.sh](bd_bases/create_db.sh) - the only one script for db creation
 - [auth-service](auth-service) localhost:9000
   - [User](auth-service/src/main/java/ru/clevertec/authservice/model/User.java) entity
   - [db/changelog/](auth-service/src/main/resources/db/changelog)
 - [news-service](news-service) localhost:8082
   - [News](news-service/src/main/java/ru/clevertec/news/model/News.java) entity
   - [Comment](news-service/src/main/java/ru/clevertec/news/model/Comment.java) entity
   - [db/changelog/](news-service/src/main/resources/db/changelog)
   - configs:
     - [RedisCacheConfig](news-service/src/main/java/ru/clevertec/news/config/RedisCacheConfig.java)
 - [app-config](app-config) localhost:8888 - cloud config service
   - [configs/](app-config/src/main/resources/configs) contains .yaml property files
 - custom starters:
   - [exception-handler-starter](exception-handler-starter)
   - [loggin-starter](loggin-starter)
   - [cache-starter](cache-starter)
 - [docker-compose.yaml](docker-compose.yaml)

## Run

### Docker-compose (*Settings):

Prod settings from app-config includes by: 
```yaml
environment:
  CONFIGHOST: http://app-config:8888
  SPRING_PROFILES_ACTIVE: dev,redis
```
Delete them to disable prod profile and delete:
```yaml
  app-config:
    build:
      dockerfile: DockerfileConfig
  ...
```

### Build and run:
! For building db containers (db-news-comments and db-users) should be run
```
./gradlew build

docker-compose up -d
```
### Rebuild:
```
./gradlew build
```
With cache cleaning:
```
docker-compose rm --all 
docker-compose pull 
docker-compose build --no-cache 
docker-compose up -d --force-recreate
```

## Endpoints OpenApi:

news (and comment) service

http://localhost:8082/swagger-ui/index.html#/

user service

http://localhost:9000/swagger-ui/index.html#/

### Get prod configs:

- http://localhost:8888/news-service/prod,redis
- http://localhost:8888/news-service/prod
- http://localhost:8888/auth-service/prod
