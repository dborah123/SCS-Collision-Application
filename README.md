# :ship: South China Sea Collision Application :ship:
## :ocean: Application Description:
REST API written in Kotlin using Spring Framework. Using Spring JPA to store and access data in PostgreSQL database.

## :ocean: Purpose
In recent years, there have been a bevy of geopolitical conflicts stemming from the South China Sea. Due to the disputed nature of this land, there are many countries vying from control over this area. Consequently, there have been a few standoffs between navy vessels of these countries, sometimes even purposely sailing close to one another in order to intimidate. This application is a proof-of-concept for a service that records these geopolitical conflicts, tallying close-calls and monitoring aggressive countries. Through this application, I hope that people will consider ways in which to keep track of this situation and promote more transparency.

## :ocean: Usage
This application keeps track of three elements: Ships, Countries, and Incidents. Each supports GET, POST, PUT, and DELETE requests. To get the two closest ships at the current time, go to the URL `ships/monitor/`

## :ocean: Building
1. Initialize a Spring project using the [Spring Initilizer](https://start.spring.io/) with the following dependecies:
  a. Spring Data JPA
  b. Spring Web
  c. PostgreSQL Driver
2. Create a PostgreSQL database and connect the project with this database using this `application.properties` file format:
  ```
  spring.datasource.url=jdbc:postgresql://localhost:5432/[database name]
  spring.datasource.username=[username]
  spring.datasource.password=[password]
  spring.jpa.hibernate.ddl-auto=create-drop
  spring.jpa.show-sql=true
  spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
  spring.jpa.properties.hibernate.format_sql=true
  ```
  Note: `spring.jpa.hibernate.ddl-auto=create-drop` will initialize when app is run and drop tables when app is terminated
  
3. clone repo into code section of Spring Project folder
