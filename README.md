# Weather Metrics Service

Small app that collects weather readings from sensors and lets you query stats
(min, max, sum, avg) over a date range.

Tech
- Backend: Java 17, Spring Boot, Gradle
- DB: PostgreSQL (via Docker Compose)
- Observability: Spring Boot Actuator
- Tests: JUnit + Spring Test

Why PostgreSQL?
- Easy to run locally, reliable, good for time‑range queries.
- Can add indexes or move to TimescaleDB later if needed.

## Quick start

Prereqs
- Java 17+
- Docker + Docker Compose (for PostgreSQL)
- Git

1) Start the database
   docker compose up -d


2) Run the app
- macOS/Linux:
  ./gradlew bootRun

- Windows:
  gradlew.bat bootRun


3) Health check
   GET http://localhost:8080/actuator/health


Default creds (from docker-compose):
- URL: jdbc:postgresql://localhost:5432/weather
- User: weather
- Password: weather

You can override with env vars:
SPRING_DATASOURCE_URL SPRING_DATASOURCE_USERNAME SPRING_DATASOURCE_PASSWORD SPRING_PROFILES_ACTIVE=local


## API

Base URL: http://localhost:8080/api/v1

1) Ingest new reading
   POST /metrics Content-Type: application/json

{ "sensorId": "sensor-1", "timestamp": "2025-11-13T10:30:00Z", "metrics": { "temperature": 22.4, "humidity": 0.56, "windSpeed": 5.2 } }


2) Query stats
   GET /metrics/summary ?sensors=sensor-1,sensor-2 # optional, defaults to all &metrics=temperature,humidity # required &stat=avg # one of: min|max|sum|avg &from=2025-11-06 # optional (YYYY-MM-DD) &to=2025-11-13 # optional; if missing, uses latest data


Example:
GET /api/v1/metrics/summary?sensors=sensor-1&metrics=temperature,humidity&stat=avg&from=2025-11-06&to=2025-11-13


Sample response:
{ "range": { "from": "2025-11-06", "to": "2025-11-13" }, "results": [ { "sensorId": "sensor-1", "metric": "temperature", "stat": "avg", "value": 21.9 }, { "sensorId": "sensor-1", "metric": "humidity", "stat": "avg", "value": 0.58 } ] }


Validation and errors
- Bad input returns 400 with details from the global exception handler.
- Unknown sensor or metric returns 404.
- Server errors return 500.

## Run tests
./gradlew test


## Project layout (short)
/src /main/java/... # controllers, services, repositories, models, exceptions /main/resources application.yml # config /config # e.g., ApiExceptionHandler.java docker-compose.yml build.gradle README.md


## Notes and next steps
- Add indexes on (sensor_id, timestamp).
- Support more stats (p50/p95) and grouping by hour/day.
- Add React UI to post readings and run queries.
- Optional: stream ingestion with Kafka; cache hot queries with Redis; deploy to AWS (RDS/Postgres, ECS/Lambda).
