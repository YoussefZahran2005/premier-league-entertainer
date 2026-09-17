#  Premier League Entertainer

> A simple Spring Boot REST API for managing and searching Premier League player statistics, backed by PostgreSQL.

![Java](https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge\&logo=openjdk\&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F?style=for-the-badge\&logo=springboot\&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-4169E1?style=for-the-badge\&logo=postgresql\&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge\&logo=apachemaven\&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-JPA-59666C?style=for-the-badge\&logo=hibernate\&logoColor=white)

---

## About

**Premier League Entertainer** is a backend-focused project built with **Java, Spring Boot, Spring Data JPA, Hibernate, and PostgreSQL**.

The application provides a REST API for storing, searching, creating, updating, and deleting Premier League player statistics.

The project was created as a **follow-up learning project inspired by a project presented on the YouTube channel of software engineer Eric Cospa**.

The original project provided a useful starting point and, most importantly, the **idea and practical direction for building the application**. I am grateful to Eric Cospa for sharing the project publicly and providing that inspiration.

However, rather than simply reproducing the original implementation, I used the project as an opportunity to **rebuild, modify, and improve the application according to my own understanding and development practices**.

A significant portion of the implementation was changed or added independently, including exception handling, repository/service logic, API behavior, and other structural decisions.

**Every line of the current implementation was worked on and understood by me.** The purpose of using an existing project as inspiration was to learn from it and then develop my own implementation—not to simply copy the original source.

---

## What I Changed & Added

While working through the original project idea, I identified several areas where I wanted to apply different practices.

### Exception Handling

Instead of returning `null` when a player could not be found, the application uses a dedicated exception:

```java
PlayerNotFoundException
```

annotated with:

```java
@ResponseStatus(HttpStatus.NOT_FOUND)
```

This allows the API to return a meaningful:

```text
404 Not Found
```

rather than allowing a missing value to potentially propagate through the application and result in a `NullPointerException`.

### Additional Implementation Changes

The project also contains my own changes and decisions across the:

* Controller layer
* Service layer
* Repository layer
* Entity model
* Search functionality
* Update behavior
* Exception handling
* API structure
* Database integration

The goal was not simply to reproduce an existing tutorial project, but to **use the original idea as a foundation and make the resulting application my own learning project**.

---

## Features

* Retrieve all Premier League players
* Search players by name
* Search players by team
* Search players by position
* Search players by nationality
* Combine multiple search filters
* Add new players
* Update existing players
* Delete players
* PostgreSQL persistence
* Automatic database schema updates through Hibernate
* Dedicated handling for missing players

---

## Technology Stack

| Technology            | Purpose                       |
| --------------------- | ----------------------------- |
| **Java 25**           | Programming language          |
| **Spring Boot 4.1.1** | Application framework         |
| **Spring MVC**        | REST API                      |
| **Spring Data JPA**   | Data access                   |
| **Hibernate**         | ORM                           |
| **PostgreSQL**        | Relational database           |
| **Maven**             | Build & dependency management |

---

## Architecture

The application follows a simple layered architecture:

```text
                 HTTP Request
                      │
                      ▼
              ┌───────────────┐
              │   Controller  │
              └───────┬───────┘
                      │
                      ▼
              ┌───────────────┐
              │    Service    │
              └───────┬───────┘
                      │
                      ▼
              ┌───────────────┐
              │   Repository  │
              └───────┬───────┘
                      │
                      ▼
              ┌───────────────┐
              │ Hibernate/JPA │
              └───────┬───────┘
                      │
                      ▼
                PostgreSQL
```

Responses follow the reverse path back to the client.

This separation keeps HTTP handling, business logic, and persistence responsibilities distinct.

---

## Project Structure

```text
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── pl/
│   │           └── premier_league_bro/
│   │               ├── PremierLeagueBroApplication.java
│   │               │
│   │               ├── controller/
│   │               │   └── PlayerController.java
│   │               │
│   │               ├── entity/
│   │               │   └── Player.java
│   │               │
│   │               ├── exception/
│   │               │   └── PlayerNotFoundException.java
│   │               │
│   │               ├── repository/
│   │               │   └── PlayerRepository.java
│   │               │
│   │               └── service/
│   │                   └── PlayerService.java
│   │
│   └── resources/
│       └── application.properties
│
├── test/
│   └── java/
│       └── PremierLeagueBroApplicationTests.java
│
├── docs/
│   └── premier_league_player_api_study_guide.md
│
└── pom.xml
```

---

# REST API

Base URL:

```text
http://localhost:8080/api/v1/player
```

## Get Players

```http
GET /api/v1/player
```

Without parameters, this returns all players.

### Available Filters

| Parameter  | Behavior                       |
| ---------- | ------------------------------ |
| `team`     | Exact team match               |
| `name`     | Case-insensitive partial match |
| `position` | Case-insensitive partial match |
| `nation`   | Case-insensitive partial match |

### Examples

Search by name:

```http
GET /api/v1/player?name=salah
```

Search by team:

```http
GET /api/v1/player?team=Liverpool
```

Search by position:

```http
GET /api/v1/player?position=FW
```

Search by nationality:

```http
GET /api/v1/player?nation=ENG
```

Combine filters:

```http
GET /api/v1/player?team=Liverpool&position=FW&nation=ENG
```

---

## Add a Player

```http
POST /api/v1/player
Content-Type: application/json
```

Example request:

```json
{
  "player": "Example Player",
  "nation": "ENG",
  "pos": "FW",
  "age": 25.0,
  "mp": 20,
  "starts": 15,
  "min": 1350,
  "gls": 8,
  "ast": 4,
  "pk": 1,
  "crdy": 2,
  "crdr": 0,
  "xg": 7.5,
  "xag": 3.2,
  "team": "Liverpool"
}
```

Response:

```text
201 Created
```

The created player is returned in the response body.

---

## Update a Player

```http
PUT /api/v1/player
Content-Type: application/json
```

The player name must already exist.

Currently, the update operation changes:

* Player name
* Team
* Position
* Nation

Example:

```json
{
  "player": "Example Player",
  "nation": "ENG",
  "pos": "MF",
  "team": "Arsenal"
}
```

If the player does not exist:

```text
404 Not Found
```

---

## Delete a Player

```http
DELETE /api/v1/player
Content-Type: application/json
```

Example:

```json
{
  "player": "Example Player"
}
```

Successful deletion:

```text
204 No Content
```

---

# Data Model

The `Player` entity is mapped to the `players` table.

| Field    | Type         | Description               |
| -------- | ------------ | ------------------------- |
| `player` | `String`     | Player name / primary key |
| `nation` | `String`     | Player nationality        |
| `pos`    | `String`     | Player position           |
| `age`    | `BigDecimal` | Player age                |
| `mp`     | `Integer`    | Matches played            |
| `starts` | `Integer`    | Matches started           |
| `min`    | `BigDecimal` | Minutes played            |
| `gls`    | `BigDecimal` | Goals                     |
| `ast`    | `BigDecimal` | Assists                   |
| `pk`     | `BigDecimal` | Penalty kicks             |
| `crdy`   | `BigDecimal` | Yellow cards              |
| `crdr`   | `BigDecimal` | Red cards                 |
| `xg`     | `BigDecimal` | Expected goals            |
| `xag`    | `BigDecimal` | Expected assisted goals   |
| `team`   | `String`     | Player's team             |

The player's name currently acts as the primary key:

```java
@Id
private String player;
```

Therefore, player names must be unique.

---

# Database Setup

The application uses PostgreSQL.

Create a database named:

```text
player_stats
```

The current local configuration expects:

```text
Host:     localhost
Port:     5432
Database: player_stats
Username: postgres
```

The application uses Hibernate's:

```properties
spring.jpa.hibernate.ddl-auto=update
```

which allows Hibernate to automatically create and update the database schema based on the entity model.

> **Security note:** The current development configuration contains the database password directly in `application.properties`. This is acceptable only as a local learning setup. For production or shared repositories, credentials should be supplied through environment variables or another secure configuration mechanism.

---

# Running the Application

### 1. Start PostgreSQL

Make sure your PostgreSQL server is running and the `player_stats` database exists.

### 2. Start Spring Boot

On Windows:

```bash
.\mvnw.cmd spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

### 3. Run Tests

```bash
.\mvnw.cmd test
```

The current test suite contains a context-load test that verifies that the Spring application starts successfully.

More comprehensive controller, service, repository, and integration tests can be added in future iterations.

---

# Repository Layer

`PlayerRepository` extends:

```java
JpaRepository<Player, String>
```

This provides standard persistence operations including:

* `findAll`
* `findById`
* `save`
* `delete`
* `count`
* `existsById`

Custom repository methods provide searching by:

* Team
* Player name
* Position
* Nation
* Team + position

The main combined search query also supports optional filters.

---

# Service Layer

`PlayerService` contains the application's business logic.

Current operations include:

* Retrieve all players
* Search players
* Add a player
* Update a player
* Delete a player

The service is also responsible for handling the case where an update targets a player that does not exist.

Instead of returning `null`, it throws:

```java
PlayerNotFoundException
```

which results in an HTTP `404 Not Found` response.

---

# Current Limitations

This is intentionally a relatively small learning project, so several areas can be improved in future versions.

* No authentication or authorization
* No pagination
* No advanced validation
* Limited automated test coverage
* No dedicated endpoint for retrieving one player
* Player name is used as the primary key
* Update functionality currently changes only selected player information
* Database credentials are currently stored in application configuration
* No frontend is currently included

---

# Future Development

Possible future improvements include:

* React frontend
* Improved DTO-based API design
* Global exception handling
* Bean Validation
* More comprehensive automated tests
* Environment-based configuration
* Pagination and sorting
* Dedicated player endpoints
* Improved REST resource design
* API documentation with OpenAPI/Swagger

---

# Acknowledgements

Special thanks to **Eric Cospa** for providing the original project idea and demonstrating the general concept through his software engineering content on YouTube.

His project served as a useful starting point for this learning exercise and gave me the opportunity to explore the same general problem while making substantial changes and implementation decisions of my own.

The current repository should therefore be viewed as an **independent follow-up implementation inspired by that project**, rather than a direct reproduction of the original source.

Thank you for the inspiration and for sharing your work publicly.

---

<p align="center">
  Built with Java, Spring Boot, PostgreSQL, and a lot of debugging.
</p>
