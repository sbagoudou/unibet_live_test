# Unibet France - Test technique

Cette application a été conçue en Java (version 17) avec Spring Boot 3.
Elle expose plusieurs APIs ainsi que quelques batchs pour la prise de paris autour d'un événement simulé en live. 

## Pré-requis au démarrage de l'application

Avant de démarrer l'application, s'assurer d'avoir installé les éléments suivants : 
* [JDK 17](https://www.oracle.com/fr/java/technologies/downloads/#java17)
* [Maven 3.8+](https://maven.apache.org/download.cgi)

## Démarrage de l'application

La web app peut être démarrée de plusieurs façons. 
* Ouvrir le projet avec l'IDE de votre choix, ouvrir la classe UnibetLiveTestApplication.java et utiliser les outils de l'IDE pour démarrer l'app
* Builder le projet avec maven (mvn clean install à la racine) et le lancer à l'aide de la commande java (java -jar target/unibet-live-test-0.0.1-SNAPSHOT.jar)

## Swagger

Un client Swagger a été intégré dans l'application afin de visualiser rapidement et facilement les APIs exposées ainsi que leur documentation. Une fois l'app démarrée, le Swagger UI est accessible à [cette adresse](http://localhost:8887/swagger-ui/index.html)

### Reference Documentation

https://spring.io/projects/spring-boot

https://spring.io/projects/spring-data

https://springdoc.org/
