# End-to-End Application - Angular UI

This is the frontend Angular application for the End-to-End project.

## Features

- **Authentication**
  - User Login
  - User Registration
  - Password Reset with OTP
  - Change Password

- **User Management**
  - View all users
  - Create new users
  - Edit existing users
  - Delete users
  - Activate/Deactivate users
  - Search users by name
  - Filter active users only

## Prerequisites

- Node.js (version 16 or higher)
- npm (comes with Node.js)
- Angular CLI (`npm install -g @angular/cli`)

## Installation

1. Navigate to the UI directory:
```bash
cd UI
```

2. Install dependencies:
```bash
npm install
```

## Running the Application

1. Start the development server:
```bash
ng serve
```

2. Open your browser and navigate to `http://localhost:4200`

## Build for Production

```bash
ng build --configuration production
```

## Project Structure

```
src/
├── app/
│   ├── components/
│   │   ├── login/
│   │   ├── register/
│   │   └── user-list/
│   ├── models/
│   │   ├── user.model.ts
│   │   └── auth.model.ts
│   ├── services/
│   │   ├── user.service.ts
│   │   └── auth.service.ts
│   ├── app.component.ts
│   ├── app.component.html
│   ├── app.module.ts
│   └── app-routing.module.ts
├── index.html
├── main.ts
└── styles.css
```

## API Endpoints

The application connects to the following backend endpoints:

- **Authentication**: `http://localhost:8080/api/auth`
- **Users**: `http://localhost:8080/api/users`

## Technologies Used

- Angular 16
- Bootstrap 5
- Bootstrap Icons
- RxJS
- TypeScript

## Development

### Adding New Components

```bash
ng generate component components/component-name
```

### Adding New Services

```bash
ng generate service services/service-name
```

## Troubleshooting

### CORS Issues
If you encounter CORS issues, make sure your backend is configured to allow requests from `http://localhost:4200`.

### API Connection Issues
Ensure your Spring Boot backend is running on `http://localhost:8080` before starting the Angular application.

## Contributing

1. Follow the existing code style
2. Add proper error handling
3. Include loading states
4. Test all functionality before committing 