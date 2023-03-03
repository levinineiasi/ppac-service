# Setup
## Database installation
Execute `docker-compose -f .\docker-compose-db.yml up -d` in this directory to start a Postgres database container. Open `docker-componse.yml` to find db credentials and use them in your favorite db client tool.

## Database migration
Make sure to replace any path to your machine path
<br><br>
`docker run --rm --network=host -v C:\Users\ion.ion\Desktop\projects\ppac-service\db\flyway\db-admin-migrations:/flyway/sql flyway/flyway -url=jdbc:postgresql://localhost:5434/postgres-local -user=db_admin -password=postgres migrate`

## Backend setup
```echo "some_api_token" | docker login ghcr.io -u levinineiasi --password-stdin```

Execute `docker-compose -f .\docker-compose-service.yml up -d` in this directory to start backend service.
