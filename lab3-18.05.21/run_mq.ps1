# https://stackoverflow.com/questions/37694987/connecting-to-postgresql-in-a-docker-container-from-outside
# https://stackoverflow.com/questions/60193781/postgres-with-docker-compose-gives-fatal-role-root-does-not-exist-error
# https://www.optimadata.nl/blogs/1/n8dyr5-how-to-run-postgres-on-docker-part-1
Start-Service -Name com.docker.service
docker run --name unruffled_bouman -e POSTGRES_PASSWORD=root -d -p 5432:5432 postgres
docker run --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management