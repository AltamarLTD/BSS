# ALTA MAR STORE

Alta Mar is a large fish wholesaler. This project is an open source implementation of an online store for this company. Was written with a purpose
increasing the efficiency of sales, as well as to optimize work with clients.

# Requirements

Java 8, Maven, Git + Docker(optional)

# Used technologies
+ Spring Boot
+ Spring Data JPA
+ Spring Security
+ Hibernate
+ MySql
+ Junit
+ Mockito
+ Docker

# Building

Follow next steps to create JAR file which can be deployed locally.

####Step 1: 
- Download or clone a project:
```bash
https://github.com/Illidan777/AltaMarShop
```
####Step 2: 
- Move to project directory:
```bash
cd AltaMarShop
```
####Step3:
- Run below command to build project:
```bash
mvn clean package
```
## Running

Run your local server with the command:
```bash
java -jar target/altamar.jar 
```
or
```bash
docker-compose up
```
## Verifying

To check the server is started go to [http://localhost:8080/shop](http://localhost:8080/shop) 
and verify response status is `200 OK`.








