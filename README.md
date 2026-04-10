# PlaceMentor

An adaptive learning and placement preparation platform that tracks performance, identifies weak areas, suggests personalized learning paths, and adapts dynamically.

## Tech Stack

- **Backend:** Java 17, Spring Boot 3.2, Spring Security (JWT), JPA/Hibernate, MySQL
- **Frontend:** React 18, Vite, React Router, Axios

## Features

- JWT Authentication (Student/Admin roles)
- 10-question MCQ tests with auto-evaluation
- Topic-wise performance tracking (Arrays, Graphs, DP, Trees, Sorting)
- Skill gap detection (WEAK / MEDIUM / STRONG)
- Auto-generated weekly/daily learning roadmaps
- Adaptive recommendations (difficulty matched to skill level)
- Dashboard with readiness score, accuracy charts, and level badges

## Setup

### Prerequisites
- Java 17+
- Maven
- MySQL (running on localhost:3306)
- Node.js 18+

### Backend
```bash
cd backend
mvn spring-boot:run
```
> Starts on http://localhost:8080. Auto-creates database and seeds 30 questions + admin user.

### Frontend
```bash
cd frontend
npm install
npm run dev
```
> Starts on http://localhost:5173

### Default Admin
- Email: `admin@placementor.com`
- Password: `admin123`

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /auth/register | Register user |
| POST | /auth/login | Login, get JWT |
| GET | /api/test/start | Start test |
| POST | /api/test/submit | Submit & evaluate |
| GET | /api/dashboard/{id} | Dashboard analytics |
| GET | /api/performance/{id} | Topic performance |
| GET | /api/skill-gap/{id} | Skill gap analysis |
| GET | /api/roadmap/{id} | Learning roadmap |
| GET | /api/recommendations/{id} | Question recommendations |
