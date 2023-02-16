# Setup
## Database installation
Execute `docker-compose up -d` in this directory to start a Postgres database container. Open `docker-componse.yml` to find db credentials and use them in your favorite db client tool.

## Database migration
Make sure to replace any path to your machine path
<br><br>
`docker run --rm --network=host -v C:\Users\s.stan\Desktop\projects\ppac-service\db\flyway\db-admin-migrations:/flyway/sql flyway/flyway -url=jdbc:postgresql://localhost:5434/postgres-local -user=db_admin -password=postgres migrate`