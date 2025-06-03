# Power Plant Simulator

A Kotlin/Java Spring Boot application that simulates power plant operations and stores data in MongoDB.

## Features

- REST API for simulation management
- File upload with content-type and size restrictions
- MongoDB integration for data persistence
- Health and readiness endpoints via Spring Boot Actuator

## Prerequisites

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/)
- (Optional) [Gradle](https://gradle.org/) if building locally

## Configuration

Create a `.env` file in the project root with the following variables:
MONGODB_USERNAME=your_mongo_user 
MONGODB_PASSWORD=your_mongo_password 
MONGODB_DATABASE=power_plant_db 
MONGODB_HOST=mongodb 
SPRING_PROFILES_ACTIVE=default

## Running the Application

1. **Build and start the services:**

   ```bash
   docker-compose up --build
   ```
This will:
- Start a MongoDB container with the credentials from `.env`
- Build and run the Spring Boot application container

2. **Access the API:**

   The application will be available at [http://localhost:8080](http://localhost:8080)

3. **Stopping the services:**
   docker-compose down

## Customization

- Application properties can be adjusted in `src/main/resources/application.properties`.
- MongoDB connection details are set via environment variables and passed to the application at runtime.

## k8s Deployment
To deploy the application on Kubernetes, ensure you have a Kubernetes cluster running and `kubectl` configured.
1. **Build the Docker image:**
create a tag like this: v0.0.1

2. Kubernetes manifests are located in the `k8s` directory. You can customize the deployment by editing the files in `k8s/overlays/development`.
```bash
kubectl apply -k k8s/overlays/development
```

## License

MIT License