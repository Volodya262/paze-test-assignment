# Paze test assignment
## General information
Conducts a payment through the PAZE demo stand. In case of success, redirects to the success page, otherwise to failure.
Event sourcing is used to store information about the payment status.

## Commands for work
You must have docker and docker-compose installed.
All commands must be executed from the console with java 17 and higher.

If sdkman is installed, ```sdk use java 17.0.4.fx-librca```

- **Start the environment for running tests and development** ```docker-compose up```
- **build, test** ```./gradlew build```
- **Start and pass the token** ```./gradlew bootRun --args='--client.paze.bearer-token=...'```
