# Premier League Player REST API --- Pre-Implementation Study Guide

## 1. Project Goal

Build a simple **RESTful API** using:

-   **Java**
-   **Spring Boot**
-   **Spring Data JPA**
-   **PostgreSQL**
-   **Maven**

The API will manage Premier League player data stored in a PostgreSQL
database.

The main goal is not to build a huge application. The goal is to
understand the **complete backend request flow** and why each layer
exists.

------------------------------------------------------------------------

# 2. Architecture Overview

The project follows a **layered architecture**:

``` text
                    HTTP Request
                         │
                         ▼
              ┌─────────────────────┐
              │     Controller      │
              │ Handles HTTP/API    │
              └──────────┬──────────┘
                         │
                         ▼
              ┌─────────────────────┐
              │       Service       │
              │ Business logic      │
              └──────────┬──────────┘
                         │
                         ▼
              ┌─────────────────────┐
              │     Repository      │
              │ Database access     │
              └──────────┬──────────┘
                         │
                         ▼
                  ┌─────────────┐
                  │ PostgreSQL  │
                  └─────────────┘
```

For a response, the flow goes back upward:

``` text
PostgreSQL
    ↓
Repository
    ↓
Service
    ↓
Controller
    ↓
HTTP Response
```

### The core idea

Each layer has **one main responsibility**:

  Layer        Main responsibility
  ------------ ---------------------------------
  Entity       Represents database data
  Repository   Talks to the database
  Service      Contains business logic
  Controller   Handles HTTP requests/responses

A useful rule:

> **Controller = HTTP, Service = logic, Repository = database, Entity =
> data model.**

------------------------------------------------------------------------

# 3. Entity Layer --- `Player`

The `Player` class represents a player in the application and maps to a
database table.

Example structure:

``` java
@Entity
@Table(name = "player_stats")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String team;
    private String position;
    private Integer appearances;
    private Integer goals;
    private Integer assists;
}
```

## What to understand before coding

### `@Entity`

Tells JPA:

> "This Java class represents persistent database data."

### `@Table`

Controls which database table the entity maps to.

``` java
@Table(name = "player_stats")
```

means the class maps to:

``` text
player_stats
```

### `@Id`

Marks the primary key.

### `@GeneratedValue`

Allows the database/JPA to generate the ID.

### Important mental model

You are creating a bridge:

``` text
Java object                 PostgreSQL row

Player                      player_stats
────────────────────        ─────────────────
id             ───────────► id
name           ───────────► name
team           ───────────► team
position       ───────────► position
goals          ───────────► goals
assists        ───────────► assists
```

This is the basic idea behind **ORM (Object-Relational Mapping)**.

------------------------------------------------------------------------

# 4. Repository Layer --- `PlayerRepository`

The repository is responsible for **accessing the database**.

``` java
public interface PlayerRepository
        extends JpaRepository<Player, Long> {
}
```

You immediately receive many operations from `JpaRepository`, including:

``` text
findAll()
findById()
save()
delete()
deleteById()
count()
existsById()
```

You do **not** need to manually write SQL for basic CRUD.

## Custom queries

You can add methods such as:

``` java
List<Player> findByNameContainingIgnoreCase(String name);
```

Spring Data JPA interprets the method name and creates the appropriate
query.

For example:

``` text
GET /api/v1/players/search?name=salah
```

could eventually call:

``` java
playerRepository.findByNameContainingIgnoreCase("salah");
```

### Important concept

The repository should generally answer:

> "How do I retrieve or persist this data?"

It should not contain your application's business rules.

------------------------------------------------------------------------

# 5. Service Layer --- `PlayerService`

The service contains the **business logic**.

Example responsibilities:

``` text
Create player
Get all players
Get player by ID
Search players
Update player
Delete player
```

A simplified structure:

``` java
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }
}
```

The repository is injected into the service.

## Why have a service layer?

You could technically make the controller call the repository directly.

But then your controller starts becoming responsible for:

``` text
HTTP
+
business rules
+
database access
```

That becomes messy.

Instead:

``` text
Controller
    ↓
"Please create this player."
    ↓
Service
    ↓
"Is this valid? What business rules apply?"
    ↓
Repository
    ↓
"Save it."
```

### Mental model

> **The service is the brain between the API and the database.**

It doesn't necessarily have complicated logic in a small CRUD project.
Its value becomes much clearer as the application grows.

------------------------------------------------------------------------

# 6. Controller Layer --- `PlayerController`

The controller handles incoming HTTP requests.

Example endpoints:

``` text
GET    /api/v1/players
GET    /api/v1/players/{id}
GET    /api/v1/players/search?name=salah
POST   /api/v1/players
PUT    /api/v1/players/{id}
DELETE /api/v1/players/{id}
```

A controller might look conceptually like:

``` java
@RestController
@RequestMapping("/api/v1/players")
public class PlayerController {

    private final PlayerService playerService;

    // endpoints...
}
```

## What the controller should do

The controller should mainly:

1.  Receive the HTTP request.
2.  Extract request data.
3.  Call the service.
4.  Return an HTTP response.

Avoid putting complicated business logic here.

------------------------------------------------------------------------

# 7. HTTP Methods

Know what each operation means before implementing the endpoints.

  Method   Purpose
  -------- -----------------------
  GET      Retrieve data
  POST     Create data
  PUT      Update/replace data
  PATCH    Partially update data
  DELETE   Delete data

For this project:

``` text
GET     → Read
POST    → Create
PUT     → Update
DELETE  → Delete
```

This is the foundation of CRUD APIs.

------------------------------------------------------------------------

# 8. HTTP Status Codes

Your controller should return meaningful HTTP status codes.

Important ones for this project:

``` text
200 OK
201 Created
204 No Content
400 Bad Request
404 Not Found
500 Internal Server Error
```

Typical usage:

### Successful GET

``` text
200 OK
```

### Successful POST

``` text
201 Created
```

### Successful DELETE

``` text
204 No Content
```

### Invalid request

``` text
400 Bad Request
```

### Player doesn't exist

``` text
404 Not Found
```

------------------------------------------------------------------------

# 9. `ResponseEntity`

You may see code such as:

``` java
return new ResponseEntity<>(playerDto, HttpStatus.CREATED);
```

Think of `ResponseEntity` as:

> **A Java object that lets you control the HTTP response.**

It can contain:

``` text
Body
Headers
Status code
```

For example:

``` java
ResponseEntity<PlayerDto>
```

means:

> "I'm returning an HTTP response whose body contains a `PlayerDto`."

You don't always need `ResponseEntity`, but it is useful when you want
explicit control over status codes and headers.

------------------------------------------------------------------------

# 10. DTOs --- Recommended Addition

For the first version, you may see tutorials return the entity directly.

However, once the basic CRUD works, introduce DTOs.

Example:

``` text
Player
    ↓
PlayerDto
    ↓
HTTP response
```

And:

``` text
HTTP request
    ↓
CreatePlayerRequestDto
    ↓
Player
    ↓
Database
```

## Why?

Your database entity and your API contract are **not necessarily the
same thing**.

DTOs give you control over what your API exposes.

This is an important step toward writing professional Spring Boot APIs.

------------------------------------------------------------------------

# 11. PostgreSQL Database

The database might contain:

``` text
player_stats
────────────────────────────────────────
id
name
team
position
appearances
goals
assists
```

Example row:

``` text
1 | Mohamed Salah | Liverpool | Forward | 30 | 18 | 10
```

Your application communicates with PostgreSQL through JPA/Hibernate.

Conceptually:

``` text
Java
 ↓
Spring Data JPA
 ↓
Hibernate
 ↓
JDBC
 ↓
PostgreSQL
```

You don't need to memorize every internal layer yet.

Just understand:

> **JPA/Hibernate translates between Java objects and relational
> database operations.**

------------------------------------------------------------------------

# 12. Configuration

Spring Boot needs to know how to connect to PostgreSQL.

Typically this is configured in:

``` text
src/main/resources/application.properties
```

or:

``` text
src/main/resources/application.yml
```

Conceptually:

``` properties
spring.datasource.url=...
spring.datasource.username=...
spring.datasource.password=...
```

You will also configure JPA/Hibernate behavior.

### Important distinction

Your application has two separate worlds:

``` text
Spring Boot application
        ↕
    Database
```

Spring Boot does not magically contain the PostgreSQL database.

It **connects to it**.

------------------------------------------------------------------------

# 13. Maven

Maven manages:

-   Dependencies
-   Building the application
-   Running tests
-   Packaging the application

Your `pom.xml` will contain dependencies such as:

``` text
Spring Web
Spring Data JPA
PostgreSQL Driver
Validation
```

You don't need to memorize Maven syntax.

Understand the purpose:

> **Maven tells the project what libraries it needs and helps build/run
> the project.**

------------------------------------------------------------------------

# 14. Suggested Project Structure

A clean starting structure:

``` text
src/
└── main/
    ├── java/
    │   └── .../
    │       ├── controller/
    │       │   └── PlayerController.java
    │       │
    │       ├── service/
    │       │   └── PlayerService.java
    │       │
    │       ├── repository/
    │       │   └── PlayerRepository.java
    │       │
    │       └── entity/
    │           └── Player.java
    │
    └── resources/
        └── application.properties
```

Later, after you understand the basics:

``` text
controller/
dto/
entity/
exception/
mapper/
repository/
service/
```

Don't create every package on day one just because it looks
professional.

------------------------------------------------------------------------

# 15. The Complete CRUD Flow

## Create

``` text
POST /api/v1/players
        ↓
PlayerController
        ↓
PlayerService
        ↓
PlayerRepository.save()
        ↓
PostgreSQL
        ↓
Created player
        ↓
201 Created
```

## Read

``` text
GET /api/v1/players/1
        ↓
Controller
        ↓
Service
        ↓
Repository.findById(1)
        ↓
PostgreSQL
        ↓
Player
        ↓
200 OK
```

## Update

``` text
PUT /api/v1/players/1
        ↓
Controller
        ↓
Service
        ↓
Find existing player
        ↓
Modify data
        ↓
Repository.save()
        ↓
PostgreSQL
        ↓
200 OK
```

## Delete

``` text
DELETE /api/v1/players/1
        ↓
Controller
        ↓
Service
        ↓
Repository.deleteById(1)
        ↓
PostgreSQL
        ↓
204 No Content
```

------------------------------------------------------------------------

# 16. What You Should Understand Before Coding

You do **not** need to master everything beforehand.

Make sure you roughly understand these concepts:

### Java

-   Classes and objects
-   Interfaces
-   Constructors
-   Generics
-   Exceptions
-   Lists

### Spring

-   `@SpringBootApplication`
-   `@RestController`
-   `@Service`
-   `@Repository`
-   Dependency Injection
-   `@Autowired` vs constructor injection

### REST

-   HTTP methods
-   URL paths
-   Request body
-   Path variables
-   Query parameters
-   HTTP status codes
-   JSON

### Database

-   Tables
-   Rows
-   Columns
-   Primary keys
-   Basic SQL
-   Relationships at a basic level

### JPA

-   `@Entity`
-   `@Id`
-   `@GeneratedValue`
-   `JpaRepository`
-   Basic derived query methods

You don't need deep Hibernate knowledge yet.

------------------------------------------------------------------------

# 17. Recommended Implementation Order

Don't build everything simultaneously.

## Phase 1 --- Database + Entity

1.  Create Spring Boot project.
2.  Configure PostgreSQL.
3.  Create `Player`.
4.  Run the application.
5.  Confirm the database/table is working.

## Phase 2 --- Repository

1.  Create `PlayerRepository`.
2.  Extend `JpaRepository`.
3.  Test `findAll()`, `findById()`, and `save()`.

## Phase 3 --- Service

Implement:

``` text
getAllPlayers()
getPlayerById()
createPlayer()
updatePlayer()
deletePlayer()
searchPlayers()
```

## Phase 4 --- Controller

Expose the REST endpoints.

## Phase 5 --- Test the API

Use Postman, Bruno, or another API client.

Test:

``` text
POST
GET all
GET by ID
GET search
PUT
DELETE
```

## Phase 6 --- Improve

Only after CRUD works, add:

-   DTOs
-   Validation
-   Exception handling
-   Pagination
-   Sorting/filtering
-   Tests

------------------------------------------------------------------------

# 18. Good Questions to Ask While Building

Instead of only copying code, constantly ask:

### Entity

> What does this Java class represent in the database?

### Repository

> How does Spring know how to retrieve this data?

### Service

> Why shouldn't this logic live in the controller?

### Controller

> What HTTP request am I receiving, and what response should I return?

### JPA

> What Java object is being converted into what database row?

### Dependency Injection

> What object does this class need, and who provides it?

### REST

> What resource am I operating on, and which HTTP method represents the
> operation?

These questions will teach you much more than memorizing annotations.

------------------------------------------------------------------------

# 19. Don't Overengineer V1

For the first implementation, **do not add**:

-   Authentication/JWT
-   Microservices
-   Docker/Kubernetes
-   Redis
-   Kafka
-   Complex relationships
-   Advanced security
-   Cloud deployment
-   Huge test suites

Those are useful later, but they would distract from the main lesson.

Your first target is simply:

``` text
Spring Boot
     ↓
REST Controller
     ↓
Service
     ↓
Repository
     ↓
JPA/Hibernate
     ↓
PostgreSQL
```

If you can build that yourself and explain every arrow, you've gotten
real value from the project.

------------------------------------------------------------------------

# 20. Definition of "Finished"

Consider V1 finished when you can:

-   Start the Spring Boot application.
-   Connect successfully to PostgreSQL.
-   Create a player through HTTP.
-   Retrieve players.
-   Search by name.
-   Retrieve a player by ID.
-   Update a player.
-   Delete a player.
-   Return appropriate HTTP status codes.
-   Explain the Controller → Service → Repository flow without looking
    at notes.
-   Explain what `@Entity`, `@Id`, `@Service`, `@RestController`, and
    `JpaRepository` do.
-   Open PostgreSQL from your database tool/VS Code and actually see the
    records your API created.

That last point is especially useful: **make the connection between an
HTTP request and the physical database row visible to yourself.**

------------------------------------------------------------------------

# Final Mental Model

If you remember only one thing before starting, remember this:

``` text
                    CLIENT
                      │
                  HTTP Request
                      │
                      ▼
                CONTROLLER
             "What was requested?"
                      │
                      ▼
                  SERVICE
              "What should happen?"
                      │
                      ▼
                REPOSITORY
              "Get/save the data."
                      │
                      ▼
                POSTGRESQL
                 "Store it."
```

And on the way back:

``` text
PostgreSQL
    ↓
Repository
    ↓
Service
    ↓
Controller
    ↓
HTTP Response
    ↓
Client
```

**Build it slowly enough that you understand the arrows, not just the
code.**
