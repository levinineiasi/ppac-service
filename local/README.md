# Guide
Purpose of this folder is to build current code and run backend service on machines that don't have anything installed, but Docker.

## Steps
1. Follow `./db/README` file to setup and migrate the database. Stop after migrating the database.
2. In this folder execute ```docker-compose up -d``` to build code and start backend service, which connects to previously set database.