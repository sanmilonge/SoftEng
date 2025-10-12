# Use official MySQL image
FROM mysql:8.0

# Set environment variables
ENV MYSQL_ROOT_PASSWORD=example
ENV MYSQL_DATABASE=world
#------Modified by Sanmi Longe------
# Copy the world.sql file into the entrypoint folder
# The original Dockerfile expected a 'test_db' folder with multiple SQL files.
# MySQL runs any .sql files here on first startup
COPY world.sql /docker-entrypoint-initdb.d/


# Expose default MySQL port
EXPOSE 3306