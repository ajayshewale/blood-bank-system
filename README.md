# blood-bank-system

This application implements the whole backend system for managing multiple blood banks. The whole appliaction is written in java with Spring boot framework.

## Requirements
- [Java](https://www.java.com/en/)
- [Postgres](https://www.postgresql.org/)
- [java spring boot](https://spring.io/projects/spring-boot)
- [docker](https://www.docker.com/)


## Install the following
- #### postgresql
    - `brew install postgres` (Mac OsX command)
    - `brew services start postgresql` (start postgres)
    
- #### Setting up user and database with postgres
    - `create user root with encrypted password 'admin';` (create use)
    - `create database blood_bank; (create database);`
    - `grant all privileges on database blood_bank to root;` (grant permission to the user on database auth_wizard)

## How to run this project
- Clone the repository
- traverse to blood bank folder: *cd blood-bank*
- Build project: *mvn clean install*
- populate properties file values
- traverse to rest web folder: *cd rest-web*
- deploy the project: *mvn spring-boot:run*

## Verify
- After deployment, open swagger-ui using endpoint (localhost:8080/blood-bank/swagger-ui.html)
- In swagger-UI, there are two endpoint all functionality required
