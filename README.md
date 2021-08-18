# Interlogica code challenge
## South Africa Mobile Numbers

## Read Me First
The implemented solution is composed by:
* Backend: Java 11 + Spring Boot + H2 Database
* Frontend: Angular 12 Web App

# Getting Started

## Run

### Requirements:
* Java 11+ JDK installed
To run the application you must execute the 'mvnw' script by command line, specifying the command:

* Linux: ./mvnw clean spring-boot:run
* Win:  .\mwnw.cmd clean spring-boot:run

The application is listening on port 8080, you can reach it simply by in your web browser:

http://localhost:8080

After the application is running, you first need to register as a user by clicking on the 'Sign Up' link. Then you need to perform the login operation by inserting your username and password on the 'Login' page. After this last step you're able to use che csv files upload/download and the mobile number check features.


## Notes
* The backend application implements a set of REST API, most of which uses a JWT authentication mechanism. 
* The webapp source code is available in the 'webapp' folder. If you want to rebuild it, you need to install npm and the Angular CLI on you machine. The application is configured to automatically put the dist files inside the src/main/resources/static of the backend application.