# Smart Online Booking System

Centralized reservation platform for rooms, halls, courts, and similar resources. Customers book available slots. Staff maintain inventory. Admins review live statistics. Double-booking is prevented with a pessimistic lock inside a single database transaction.

## Tech stack

| Layer | Stack |
| --- | --- |
| Backend | Java 17, Spring Boot 3, Spring Security, Spring Data JPA, Hibernate, JWT, Lombok, Maven |
| Frontend | React 18, Vite, Tailwind CSS, Axios, React Router, React Query |
| Database | MySQL 8 |
| Docs / ops | Springdoc OpenAPI, Docker Compose, Postman, JUnit + Mockito |

## Architecture

Layered Spring Boot API:

`controller → service → repository → entity`

JWT stateless security, DTO mapping, global exception handling, and a polymorphic `NotificationService` (dashboard, email stub, SMS stub) composed by `NotificationDispatcher`.

### OOP used

- **Encapsulation** — entities hide persistence fields; services expose use-cases.
- **Inheritance** — `AbstractNotificationService` shared send pipeline.
- **Polymorphism** — `NotificationService` implementations selected at runtime.
- **Abstraction** — booking and notification contracts.
- **Composition** — `Booking` belongs to `User` + `Slot`; dispatcher owns channel list.

## Double-booking rule

1. Load the slot with `SELECT … FOR UPDATE` (`PESSIMISTIC_WRITE`).
2. Reject with **HTTP 409** if the slot is unavailable or already has an active booking.
3. Persist the booking (`CONFIRMED`).
4. Mark the slot unavailable.
5. Commit.

Cancel restores slot availability and keeps the booking row as history.

## Project structure

```
backend/          Spring Boot API
frontend/          React + Vite SPA
postman/           API collection
docker-compose.yml
```

## API

| Method | Path | Access |
| --- | --- | --- |
| POST | `/api/auth/register` | Public |
| POST | `/api/auth/login` | Public |
| GET | `/api/users/profile` | Authenticated |
| GET | `/api/resources` | Authenticated |
| POST/PUT/DELETE | `/api/resources` | ADMIN, STAFF |
| GET | `/api/slots` `/api/slots/available` | Authenticated |
| POST/PUT | `/api/slots` | ADMIN, STAFF |
| POST | `/api/bookings` | Authenticated |
| GET | `/api/bookings/my` | Authenticated |
| DELETE | `/api/bookings/{id}` | Owner, STAFF, ADMIN |
| GET | `/api/admin/dashboard` | ADMIN |
| GET | `/api/admin/bookings` | ADMIN |
| GET | `/api/admin/users` | ADMIN |

Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Seed users

| Role | Email | Password |
| --- | --- | --- |
| ADMIN | admin@smartbooking.com | Admin@123 |
| STAFF | staff@smartbooking.com | Staff@123 |
| CUSTOMER | customer@smartbooking.com | Customer@123 |

Public registration always creates a **CUSTOMER**.

## Local run

### Prerequisites

- JDK 17
- Maven 3.9+
- Node.js 20+
- MySQL 8 (or Docker)

### Database

```sql
CREATE DATABASE smart_booking;
```

Default JDBC: `root` / `root` on `localhost:3306`. Override with env vars from `backend/.env.example`.

### Backend

```bash
cd backend
mvn spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

UI: [http://localhost:5173](http://localhost:5173)

### Tests

```bash
cd backend
mvn test
```

Uses H2 (`test` profile). Data seeder is disabled in tests.

### Docker

```bash
docker compose up --build
```

- Frontend: http://localhost:5173
- Backend: http://localhost:8080
- MySQL: localhost:3306

## Roles

- **CUSTOMER** — search resources, book/cancel own slots, profile.
- **STAFF** — manage resources and slots.
- **ADMIN** — staff capabilities plus user list and dashboard statistics.

## Notifications

Current: dashboard/status logs via `DashboardNotificationService`.  
Future: replace `EmailNotificationService` / `SmsNotificationService` stubs with SMTP or SMS providers without changing `BookingService`.
