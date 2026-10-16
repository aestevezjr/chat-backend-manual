# Real-Time Chat — Backend

Spring Boot backend for a real-time chat over WebSocket/STOMP, with user presence, typing indicators and persisted message history. Written by hand to practise layered testing.

**Frontend:** [chat-react-manual](https://github.com/aestevezjr/chat-react-manual) · **Demo video:** see the Real-Time Chat card on [agustinestevez.vercel.app](https://agustinestevez.vercel.app/en/#projects)

## Stack

- Java 17, **Spring Boot 4**
- Spring WebSocket with a **STOMP** simple broker
- Spring Data JPA on **SQLite** (Hibernate community dialect)
- Tests: JUnit 5, Mockito, AssertJ, MockMvc, `@DataJpaTest`

## How it works

Clients connect to the STOMP endpoint `/ws-chat`, send to `/app/*` destinations and subscribe to `/topic/*`.

| Client sends to | Effect | Broadcast on |
| --- | --- | --- |
| `/app/chat.join` | Registers the user and announces the join | `/topic/messages`, `/topic/users` |
| `/app/chat.send` | Saves the message and delivers it | `/topic/messages` |
| `/app/chat.typing` | Relays "is typing" state | `/topic/typing` |

When a socket disconnects, a session listener removes the user, broadcasts the updated user list, posts a "left the chat" message and clears their typing state.

REST endpoints:

| Method | Path | Returns |
| --- | --- | --- |
| `GET` | `/api/messages` | Message history |
| `GET` | `/api/users` | Connected users |

CORS allows the frontend origin configured in `app.cors.allowed-origins` (default `http://localhost:5173`).

## Project structure

```
src/main/java/com/aestevez/chatbackend/
  config/        WebSocket (STOMP) and CORS configuration
  websocket/     STOMP controller and connect/disconnect listener
  controllers/   REST endpoints
  services/      message history, connected users
  repositories/  JPA repository
  models/, dtos/, mappers/
websocket-test-client/   plain HTML page to try the STOMP endpoint by hand
```

## Tests

**16 tests**, all passing:

| Suite | Tests | Scope |
| --- | --- | --- |
| `MessageServiceTest` | 4 | Service logic with Mockito |
| `MessageControllerTest` | 4 | REST layer with MockMvc, including CORS rules |
| `MessageRepositoryTest` | 4 | JPA queries with `@DataJpaTest` on an in-memory database |
| `MessageMapperTest` | 3 | Entity ↔ DTO mapping |
| `ChatBackendManualApplicationTests` | 1 | Context loads |

```bash
./mvnw test
```

## Running locally

Requires Java 17+.

```bash
./mvnw spring-boot:run     # http://localhost:8080
```

Messages are stored in `chat.db` (SQLite) in the working directory. Then start the [frontend](https://github.com/aestevezjr/chat-react-manual).
