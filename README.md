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
TBD

### 3) Codebase
- Multiple gradle module **Spring Boot** project;
- Entrypoint of Spring Boot app is located in `ppac-service-assembly` module;
- Database domain entities are located in `domain` package under `ppac-service-common` module;
- Business classes are located in `data_classes` package under `ppac-service-common` module;
- DTO needed in various places of the codebase are placed in `ppac-service-integration` module.
- Our service interfaces are declared in `ppac-service-common` module and these are used everywhere, while the implementation beans are located in `ppac-service-impl` module, to have the codebase in pluggable way.
- Feature flags are used. They are all disabled in default `application.yml` and are enabled in particular `yml` file for each env per need. 
## Build
TBD