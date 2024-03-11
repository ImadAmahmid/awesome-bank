<div>

<h3 align="center">Awesome Banking App</h3>

</div>

# Overview
This folder is a very quick start to testing high availability and how high number of transactions for one account would not lead the app to an illegal state.
It will spin up the following components as
docker services
and link them up as necessary:

- Postgres: a database where all the account and operation data are persisted
- Nginx: a nginx server is run as a reverse proxy (or loadbalancer) using the least connected strategy. This proxy will
  forward the client requests to one of
  the bank apps running. In case an app is down another one will be targeted and the one that is down would be
  temporarily deactivated.
- Banks: 2 banks will be run using the same configuration to target the same database instance. 

# How to run?

#### spin up maven install

`mvn clean install`
then copy the jar inside of the /application/target to the docker-compose folder

`cp ./application/target/application-0.0-SNAPSHOT.jar`

#### Create a docker volume 
This step is not mandatory but to persist the postgres data and keep your account info, nobody wants their money gone :)
`docker volume create pgdata`

#### Move to the docker-compose/postgres directory and run

```
cd docker-compose/postgres

docker-compose up 
```

###Enjoy

PS:

- when you change a file inside your workspace you will have to run the next docker-compose with force build flag to
  force the creation of new
  docker images
