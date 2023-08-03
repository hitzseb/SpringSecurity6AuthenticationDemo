# Authentication Demo
This is a Spring Boot RESTful API that provides authentication using Spring Security 6 and Java JWT.

## Getting Started
To get started with the application, follow these steps:

Clone the repository to your local machine.
Open the project in your IDE of choice.
Build and run the project.

## Running the Application with Maven
To run the application using Maven, navigate to the root directory of the project and run the following command:
```
mvn spring-boot:run
```
This will compile the code, package it into an executable JAR file, and run the application. You can then access the API and the administrator interface by visiting http://localhost:8080 in your web browser.

Note that you must have Maven installed on your machine in order to run this command. You can download Maven from the official website:
[https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)

If you encounter any issues while running the application with Maven, try running the following command to clean the project:
```
mvn clean
```
This will remove any previously compiled files and dependencies, and should help to resolve any errors you may be experiencing.

## RESTful API
To check the Api documentation run the application and go to  
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Technologies Used
The application was built using the following technologies:

- Spring Boot
- Spring Data JPA
- Spring Security
- Java Mail Sender
- Springdoc
- MySQL
- JJWT