# Setup
## Database installation
Execute `docker-compose -f docker-compose-db.yml up -d` in this directory to start a MariaDB database container. Open `docker-componse.yml` to find db credentials and use them in your favorite db client tool.
<br><br>
create schema if not exists `mariadb-local` character set latin1 collate latin1_swedish_ci;
<br><br>
https://stackoverflow.com/questions/63233254/utf8mb4-unicode-breaking-mariadb-jdbc-driver

## Database migration
Make sure to replace any path to your machine path
<br><br>
`docker run --rm --network=host -v C:\Users\ion.ion\Desktop\projects\ppac-service\db\flyway\db-admin-migrations:/flyway/sql flyway/flyway -url=jdbc:mariadb://localhost:3306/mariadb-local -user=db_admin -password=mariadb migrate`

## Backend setup
```echo "some_api_token" | docker login ghcr.io -u levinineiasi --password-stdin```
<br><br>
Make sure to edit ```docker-compose-service.yml``` file to put actual image id, instead of placeholder ```COMMIT_SHA```
<br><br>
Execute `docker-compose -f .\docker-compose-service.yml up -d` in this directory to start backend service.
