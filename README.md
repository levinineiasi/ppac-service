# PPAC Service
http://localhost:8080/swagger-ui/index.html
## System overview
[![Main CI/CD](https://github.com/levinineiasi/ppac-service/actions/workflows/cicd-full.yaml/badge.svg)](https://github.com/levinineiasi/ppac-service/actions/workflows/cicd-full.yaml)
### 1) Pipeline
Each **push/merged PR** in `main` branch will trigger github action that does the following things:
- execute build;
- run unit tests;
- run **Detekt** for static code analysis, which can fail the build if code issues are above threshold. See `detekt.yaml` file in root of repository;
- build docker image using `ppac-service-assembly/Dockerfile`
    We used **Skaffold** which handles the workflow for building, pushing and deploying your application, allowing you to focus on what matters most: writing code.

**Out of scope for now**:
- show coverage and testing report;
- push docker image to repo;
- deploy on various envs.

### 2) Architecture
- Spring boot service which connects to API to get countries data;
- Data API is virtualized on local environment using `Wiremock`;
- Exposes API to compute single route between two countries.

**Out of scope for now**:
- automatic swagger file generation of exposed API

### 3) Codebase
- Multiple gradle module **Spring Boot** project;
- Entrypoint of Spring Boot app is located in `ppac-service-assembly` module;
- Gateway abstraction is used to communicate with external API. Rest communication is done using  [Feign](https://github.com/OpenFeign/feign). Depending on Spring profile, different client beans are injected at runtime.
- DTO needed in various places of the codebase are placed in `ppac-service-integration` module. 
  - For example, gateway implementation has its own DTO with details regarding serialisation and is placed in gateway module. Gateway interface depends only on `countries-service-integration` and hides its internal details. 
- Our service interfaces are declared in `ppac-service-common` module and these are used everywhere, while the implementation beans are located in `countries-service-impl` module, to have the codebase in pluggable way.
- Feature flags are used. They are all disabled in default `application.yml` and are enabled in particular `yml` file for each env per need. 
## Build
### 1. Using Docker
- Make sure you have `Docker` daemon installed together with `docker` and `docker-compose` cli available in your path
- `cd local`
- `docker-compose build` to build app image
- `docker-compose up` to create 3 containers:
  - `ppac-data-wiremock-api` which is using Wiremock to mock data feeder API;
  - `ppac-service-local` our service deployed locally using `local` profile, while listening to port `8080` for connections. This instance connects to **Wiremock data feeder API**;
  - `ppac-service-qa` our service deployed locally using `qa` profile, while listening to port `8081` for connections. This instance connects to **real API provided in requirements**.
### 2. Using local cli tools 
- Make sure to install `Gradle 7.2+`, `Java 17`
- Execute `gradle clean build` to build the server
- To run server execute `gradle bootRun --args='--spring.profiles.active=${SPRING_PROFILES}'` Either set env variable or replace `${SPRING_PROFILES}` with `local` or `qa`
  - If you run using `qa` profile it will connect to real API https://raw.githubusercontent.com/mledoze/countries/master/countries.json;
  - If you run using `local` profile, you will need wiremock API listening to port 9000, which you can start using command `docker-compose up ppac-data-wiremock-api` from `local` directory;
- Call API `http://localhost:8080/v1/routing/CZE/ITA`