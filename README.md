# roomba-hoover


# Robotic Hoover Service - Roomba Hoover

This is the absolute beginner version of the robot vacuum cleaner application. This is application that simulates the operation of a robotic hoover (like a Roomba) in a rectangular room. The application provides a REST API to navigate the hoover through the room, clean patches of dirt, and return the final position and number of cleaned patches.

## Features

- Navigate the robotic hoover in a rectangular room
- Clean patches of dirt
- Return the final position of the hoover
- Return the number of cleaned patches

## Prerequisites

- Java 17 or later
- Docker
- Kubernetes (kubectl configured to access your cluster)
- Maven (for building the project)

## Getting Started

### Build and Run Locally

1. **Clone the repository:**

    ```sh
    git clone https://github.com/your-username/robotic-hoover.git
    cd robotic-hoover
    ```

2. **Build the application:**

    ```sh
    mvn clean install
    ```

3. **Run the application:**

    ```sh
    mvn spring-boot:run
    ```

4. **Test the application:**

   You can use Postman or any other API testing tool to test the application. The API endpoint is:

    ```
    POST http://localhost:8080//api/robot/move
    ```

   Example request body:

    ```json
    {
      "roomSize" : [5, 5],
      "coords" : [1, 2],
      "patches" : [
        [1, 0],
        [2, 2],
        [2, 3]
      ],
      "instructions" : "NNESEESWNWW"
    }
    ```

   Example response body:

    ```json
    {
      "coords" : [1, 3],
      "patches" : 1
    }
    ```

### Docker

1. **Build the Docker image:**

    ```sh
    docker build -t <your-dockerhub-username>/robotic-hoover:latest .
    ```

2. **Run the Docker container:**

    ```sh
    docker run -p 8080:8080 <your-dockerhub-username>/robotic-hoover:latest
    ```

### Kubernetes

1. **Push the Docker image to Docker Hub:**

    ```sh
    docker push <your-dockerhub-username>/robotic-hoover:latest
    ```

2. **Deploy to Kubernetes:**

   Apply the deployment and service configuration:

    ```sh
    kubectl apply -f deployment.yaml
    kubectl apply -f service.yaml
    ```

3. **Access the service:**

   Use the external IP address assigned by the LoadBalancer service to access the application.

## Configuration

### Security Configuration

The application uses Basic Authentication. The credentials are stored in the `application.properties` file:

```properties
app.security.user.name=admin
app.security.user.password=123456


## API Documentation

The API documentation is available through Swagger. Once the application is running, you can access the Swagger UI at:
http://localhost:8080/swagger-ui/

## License

This project is licensed under the MIT License.

## Acknowledgements

	•	Spring Boot
	•	Docker
	•	Kubernetes
	•	Swagger