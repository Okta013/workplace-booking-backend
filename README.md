## Сервис бронирования рабочих мест

[![Coverage](http://sonarqube.lan/api/project_badges/measure?project=workplace-booking&metric=coverage&token=50638edbc2a4eb739b2282cb6900612354b41fbd)](http://sonarqube.lan/dashboard?id=workplace-booking)
[![Code Smells](http://sonarqube.lan/api/project_badges/measure?project=workplace-booking&metric=code_smells&token=50638edbc2a4eb739b2282cb6900612354b41fbd)](http://sonarqube.lan/dashboard?id=workplace-booking)

### Functions

### Environment variables

```properties
DATABASE_URL=path to base
DATABASE_USER=login
DATABASE_PASSWORD=password
MAIL_HOST=host
MAIL_USERNAME=username of email
MAIL_PASSWORD=password of email
MAIL_PORT=port
EMAIL_SUBJECT_LINE=subject
EMAIL_MESSAGE_BODY=body
ENABLE_EMAIL=true/false for send email
SECRET_KEY=key for auth
EXPIRATION_TIME=jwt lifecycle
USER_PASSWORD = default user pass
```

### DB

```postgresql
DROP ROLE IF EXISTS "workplace-booking";
CREATE ROLE "workplace-booking" LOGIN PASSWORD 'workplace-booking';

DROP DATABASE IF EXISTS "workplace-booking-db";
CREATE DATABASE "workplace-booking-db" OWNER = "workplace-booking";
```

### Local developing

#### in IDEA

In order to run the service in IDEA, it's necessary to set
the parameter `Active profile` to `local` in IDEA run configuration.

#### Docker

For local development here is docker-compose with database located at ./.local. Database has next configuration:

```properties
DATABASE_URL=workplace-booking-db
DATABASE_USER=workplace-booking
DATABASE_PASSWORD=workplace-booking
MAIL_HOST=smtp.gmail.com
MAIL_USERNAME=staccato013@gmail.com
MAIL_PASSWORD="yubx mmvl ymwa miip"
MAIL_PORT=587
EMAIL_SUBJECT_LINE=Your password for log in to the system
EMAIL_MESSAGE_BODY=Hi! To log in using your email address and a one-time password:
ENABLE_EMAIL=false
SECRET_KEY======================================SecretnyiSecret================================
EXPIRATION_TIME=86400000
USER_PASSWORD = userPassword
```
#### Swagger

URL: http://localhost:8080/swagger-ui/index.html

#### Authentication for admin

"email": "admin@mail.ru",
"password": "AdminPass"