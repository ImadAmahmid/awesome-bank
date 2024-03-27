<div>

<h3 align="center">Awesome Banking App</h3>

</div>

A banking up built quickly yet with love to XXXX Dojo , using Java 17 and Spring Boot 3 (Spring 6) with multiple layers, each set in a separate module for decoupling’s of concerns and more flexibility of implementation sake.
In this readme you will find how to navigate the project as well as how to run using either java or inside of a docker container as a microservice.

# How to run:
If you run using docker compose you can benefit from a microservice architecture instantiating two
instances of the bank service with postgres database. The nginx server load balancer will split the traffic 
to target the two instances behind. Please check out the readme within the docker 
compose folder in order to find the step-by-step guide. When you add Kafka to the profiles within the dockerfile
it will also activate the kafka producer that will intercept biEvents received from the application domain and then
publish them on a kafka topic. I also introduced a Kafka consumer that will therefore consume these events,
and randomly throw errors to simulate the retry mechanism.

You can also run using Intellij! If you do not the swagger generator plugin install and configured 
you can spin up a mvn install first to generate these resources for you.

Third way of running the project is using a plain old java after a :

```
mvn clean install

java -jar -Dspring.profiles.active=postgres ./application/target/bank-application.jar
or: to have some dummy users created and an account created with some operation
java -jar -Dspring.profiles.active=postgres,dev ./application/target/bank-application.jar
or: to have a kafka consumer receiving bi events and publishing them (and later on consumer by another kafka consumer)
first run the docker-compose with zookeper and a broker, then:
java -jar -Dspring.profiles.active=postgres,dev,kafka,kafka-local ./application/target/bank-application.jar
```
Once that's done you can access the application swagger doc here and make some tests:

[Swagger definition localhost URL](http://localhost:8383/swagger-ui.html#)

# Acrhitecture details: 

### Bank-domain:
Contains the business logic of the banking app.  

### Bank-dal
Contains an implementation of the persistence layer using JPA.
The entities and repositories will be nested in this project 
as well as the mappers to convert these entities to domain objects.

### Bank-api 
Contract first driven module, that takes care of the generation of the API 
resources from a swagger file. These resources will be implemented by the api-bank
module and call the adapter layer, the adapter will then 
reach to the services of the bank-domain and map the result back to the api.

### Bank-security 
A new module to ensure the communication with the server is secure. The security is
done through a registering of user at the application load when dev profile is active.
The users are stored in an in memory cache, but a user repository would be a nice thing 
to have in the future.
This module also exposes an end point to authenticate. The users are user, admin & manager and 
they all have a very strong password (password) 
It will be a nice thing to add also a new end point to register users dynamically and 
persist the user in a database.

### Bank Application module
A small module that will spin up the spring boot app, enables the swagger api
and expose our API for everyone!
The application can run either with a postgres database or for a quick spin up using h2 embedded db.
The auditing is also enabled by default, this way we can track every operation back to its issuer!

You can use the spring profiles: h2 , postgres for this purpose

### Bank Bi module
This module provides a base for using BI events:
- Bi event consumer that will listen to the Bi event Events from the application context and buffers them in memory, when the size
of the buffer is filled it publishes throw the Bi event publisher. The consumer is Async in order not to hinder the perfs of the application.
- Bi event published will subscribe BI event listeners and publish the Bi events to them
- Bi event subscriber that will receive the BI events whenever the publisher publish them. The Kafka producer is one of these listeners.


### bank kafka messaging
Contains a Kafka producer that will publish the BI events that happened in the application when he receives them from the Bi
event publisher. 
A consumer is also listening to the topic of the Kafka producer and will consume these events. To showcase of the retry mechanism in case
a read operation is not successful, hence not committed, the Dummy Notification Service
will throw an error when the consumed event has an odd account id! 
The kafka implementation uses Avro and schema registry in order to control the quality of the data stored and insure a compatibility 
between the versions of the Operation's schema.

Some integration tests are also implemented but could use a bit of improvement.


### Nice to have later:
Add tracing to logs via a filter 
Add validation to operations

### Enhancements
Remove the newBalance from the AccountOperations response
Add terraform deployment and/or kubernetes with helm
Add auth with Google and Github
Add splunk with some dashboards
Add a front-end app with ReactJs to authenticate user and make operations.