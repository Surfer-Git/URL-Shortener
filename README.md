
# URL shortener

Spring Boot based URL shortener
## Run Locally

- make sure you have **docker installed**.
- clone the repo (**main** branch)
- run following command to build the image
```bash
./mvnw spring-boot:build-image -DskipTests  
```
- once the image is built, run the docker compose command
```bash
docker compose up -d
```
- access the service at : http://localhost:8080/
## Features
- there are 2 users - admin & sample-user.
- for login credentials (refer tests or flyway-migration script).
- user can shorten url and keep it as public or private.
- user can specify url expiration time (deafult will be 365 days).
- public urls : will be visible to everyone.
- private urls : visible to the user only.

### Logic for shortening of urls
- refer `UrlUtils.java` class

### Spring Security : authentication
- http basic auth
- passwords are hashed along with user specific salt.
## Future Scope
- Add capability to register user
- Add capability to delete the existing url
- Modify URL shortening logic (counter based 62 base encoding) 
## Acknowledgements
This project was a learning project and was inspired by :
 - [siva-labs url shortener project](https://github.com/sivaprasadreddy/spring-boot-url-shortener)

