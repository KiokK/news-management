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
- spring security
- oauth2

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

! For building db containers (db-news-comments and db-users) should be run.
In docker-compose up two containers: 

```yaml
  db-news-comments:
    container_name: db_news_comments
    image: postgres:alpine
    ...
    ports:
      - "5432:5432"

  db-users:
    container_name: db_users
    image: postgres:alpine
    ...
    ports:
      - "5433:5432"
```

build and up:
```
./gradlew build

docker-compose up -d
```

### Rebuild (if necessary):
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

### * Get token

Postman:

localhost:9000/oauth2/token

![img.png](img.png)
Body params: grant_type: client_credentials
![img_1.png](img_1.png)

Response example:
```yaml
{
    "access_token": "eyJraWQiOiI2NGE0MjQ1Mi1hYmQwLTQ2ZmUtOTdjOC0yYjhmMGJkMmJhYmQiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJjbGllbnQiLCJhdWQiOiJjbGllbnQiLCJuYmYiOjE3MDk4MjM4MzEsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6OTAwMCIsImV4cCI6MTcwOTgyNDEzMSwiaWF0IjoxNzA5ODIzODMxLCJqdGkiOiIyYzc2YTE4Zi0xZTE3LTRlYmEtODQwNi00YzM1Nzg3NjEyOWYifQ.BmDx3-tnAKxh7BUBYwUZxKO95atwg7yCl3iHDzWgdeg0dssiLq9tM-VC-2_wCYbYPW6Ds4TptQjoCipcAyQ-4rPiMePypQbKu5zIGsu57SJcmP1dqpa3IbmO0-YI1VevRPoDZOqGqcOfXRmhoc_CJi9JnGdPYQJCAuJskGTO27yhz3sFve2AvMBz6RvQKs6Rn7WMW5i3txvA1gTqBY4X5HZ0jTKPnwugShi2ZYABwVCVmFDms8eY3U8bkyvqbt9ekG6pmweIpglcs2E8grQsouONkfrSBJPbhrZZh--Z5fR1m5r1ET5EFHOHOWAtIZ919qWAvoxMIzUBO04FyrM6Nw",
    "token_type": "Bearer",
    "expires_in": 300
}
```

* copy `access_token` for swagger auth (~5 min useful/ than regenerate)

## Endpoints OpenApi:

news (and comment) service

http://localhost:8082/swagger-ui/index.html#/

user service

http://localhost:9000/swagger-ui/index.html#/

### Get prod configs:

- http://localhost:8888/news-service/prod,redis
- http://localhost:8888/news-service/prod
- http://localhost:8888/auth-service/prod
