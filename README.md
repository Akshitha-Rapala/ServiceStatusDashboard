# Service Status Dashboard
I selected Option-2 which is creating a small internal dashboard for viewing the health of platform services.

The project was created as a time-boxed first iteration and
intentionally favors clarity, maintainability and useful operational
behavior over infrastructure complexity.

## Architecture

Frontend:
React + TypeScript + Vite

Backend:
Kotlin + Java + Spring Boot + Maven

The React client communicates with the Spring Boot application through
a REST API.

Kotlin is the primary backend language. A Java validation component is
called from Kotlin to demonstrate Java/Kotlin interoperability.

## Requirements

- Java 21
- Maven
- Node.js
- npm

## Run the Backend

From the project root:

cd backend

mvn clean test
mvn spring-boot:run

Backend:

http://localhost:8080

## Run the Frontend

Open another terminal:

cd frontend

npm install
npm run dev

Frontend:

http://localhost:5173

## API

GET /api/v1/services

Returns all service statuses.

Example:

GET /api/v1/services?status=OPERATIONAL

GET /api/v1/services?status=DEGRADED

GET /api/v1/services?status=DOWN

Supported filters:

OPERATIONAL
DEGRADED
DOWN

Filtering is case-insensitive.

## Example Response


{
  "services": [
    
    {
      "id": "authentication",
      "name": "Authentication",
      "description": "Identity, sign-in, and session management.",
      "status": "OPERATIONAL"
    },
    {
      "id": "payments",
      "name": "Payments",
      "description": "Payment processing and billing operations.",
      "status": "OPERATIONAL"
    },
    {
      "id": "notifications",
      "name": "Notifications",
      "description": "Email, push, and in-app message delivery.",
      "status": "DEGRADED"
    },
    {
      "id": "search",
      "name": "Search",
      "description": "Indexing and search across the platform.",
      "status": "OPERATIONAL"
    },
    {
      "id": "ai",
      "name": "AI",
      "description": "AI-powered inference and platform assistance.",
      "status": "DOWN"
    }
  ],
  "summary": {
    "total": 5,
    "operational": 3,
    "degraded": 1,
    "down": 1
  },
  "generatedAt": "2026-09-19T21:49:14.838281700Z"
}


{
  "services": [    
    
    {
      "id": "authentication",
      "name": "Authentication",
      "description": "Identity, sign-in, and session management.",
      "status": "OPERATIONAL"
    },
    
    {
      "id": "payments",
      "name": "Payments",
      "description": "Payment processing and billing operations.",
      "status": "OPERATIONAL"
    },
    
    {
      "id": "notifications",
      "name": "Notifications",
      "description": "Email, push, and in-app message delivery.",
      "status": "DEGRADED"
    },
    
    {
      "id": "search",
      "name": "Search",
      "description": "Indexing and search across the platform.",
      "status": "OPERATIONAL"
    },
    
    {
      "id": "ai",
      "name": "AI",
      "description": "AI-powered inference and platform assistance.",
      "status": "DOWN"
    }
    
  ],  
  
  "summary": {
    "total": 5,
    "operational": 3,
    "degraded": 1,
    "down": 1
  },
  "generatedAt": "2026-09-19T21:49:14.838281700Z"  
}

## Error Handling

Invalid filters return HTTP 400 with a structured response containing:

- timestamp
- HTTP status
- error
- message
- request path
- request ID

Unexpected errors are handled centrally and return an HTTP 500 response
without exposing internal implementation details.

## Validation

The optional status query parameter is validated by a Java component.

Valid values are:

OPERATIONAL
DEGRADED
DOWN

A missing status value means that all services should be returned.

## Observability

The application includes lightweight production style observability.

### Request IDs

Every request receives an X-Request-ID.

A client-provided X-Request-ID is preserved. Otherwise, the backend
generates a UUID.

The request ID is:

- returned as an HTTP response header
- included in backend logs
- included in structured API error responses

### Logging

SLF4J is used for application logging.

Logs include the current request ID using MDC so an individual request
can be correlated with its application logs.

### Actuator

Health and Metrics:

http://localhost:8080/actuator/health
http://localhost:8080/actuator/info
http://localhost:8080/actuator/metrics
http://localhost:8080/actuator/metrics/service.status.dashboard.requests
http://localhost:8080/actuator/metrics/http.server.requests


## Accessibility

Status is communicated using text in addition to color.

Interactive controls support keyboard navigation.

Focus indicators remain visible.

ARIA attributes are used for filter state, live updates, errors, and
service status descriptions.

Semantic HTML elements are used where appropriate.

## Assumptions

Service health information is mocked for this first iteration.

The dashboard is assumed to be an internal application.

Authentication and authorization are not included.

## Engineering Tradeoffs

Mocked data is used instead of live service integrations so that the
implementation can focus on the core dashboard experience and code
quality.

A database was intentionally not introduced because the current
requirement only displays current service health and does not require
historical data.

Spring Boot Actuator and Micrometer provide lightweight health and
metrics capabilities without introducing an external monitoring stack.

The application uses the browser Fetch API instead of another HTTP
client dependency because the required functionality is small.

Plain CSS is used instead of a UI framework to keep dependencies and
complexity low.

## With Another Day

I would consider adding:

- real service health-check integrations
- automatic status polling
- service latency and uptime information
- historical incident/status data
- authentication and RBAC
- persistent storage
- automated frontend tests
- integration tests
- distributed tracing
- production monitoring integration
