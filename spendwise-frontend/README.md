# SpendWise Expense Tracker Frontend

React + Vite frontend for the supplied Spring Boot ExpenseTracker backend.

## Requirements
- Node.js 18 or newer
- Spring Boot backend running on `http://localhost:8080`
- Backend CORS must allow `http://localhost:3000` (the supplied SecurityConfig already allows this origin)

## Run locally
```powershell
npm install
npm run dev
```
Open http://localhost:3000.

## API configuration
By default, the frontend uses `http://localhost:8080`. To change it, copy `.env.example` to `.env` and set `VITE_API_BASE_URL`.

## Included pages
- `/register` and `/login`: uses `/api/v1/auth/register` and `/api/v1/auth/login`
- `/`: dashboard backed by current-month expenses, total and categories
- `/expenses`: list current-month expenses, create, update, delete, filter/search
- `/categories`: list, create, update, delete categories and fetch monthly totals

## Notes
- JWT is stored in localStorage under `expense_token` and attached by the shared Axios client.
- The backend currently exposes current-month expense listing and month/year listing; the UI uses the current-month list by default.
- Verify the app against your own backend and database before deployment. This starter was not runtime-tested against your local database.
