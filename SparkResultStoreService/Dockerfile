FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the source code
COPY . .

# Make the entrypoint script executable
RUN chmod +x entrypoint.sh

# Set the entrypoint to the script
ENTRYPOINT ["./entrypoint.sh"]

