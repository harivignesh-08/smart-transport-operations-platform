# Openeye Frontend

Smart Fleet Operations Platform - React-based frontend with role-based access control.

## Features

- **Role-Based Access Control**: Different interfaces for Dispatcher, Operator, and Admin roles
- **Real-time Location Tracking**: Dispatcher can share live GPS location
- **Fleet Management**: Manage vehicles and drivers (Operator/Admin only)
- **Emergency Management**: Report and track emergencies with severity levels
- **Responsive Design**: Works seamlessly on desktop, tablet, and mobile devices
- **Modern UI**: Clean, professional interface with dark theme

## Project Structure

```
frontend/
├── src/
│   ├── components/          # Reusable components
│   │   ├── Layout.jsx
│   │   ├── ProtectedRoute.jsx
│   │   └── Sidebar.jsx
│   ├── context/             # React Context
│   │   └── AuthContext.jsx
│   ├── pages/               # Page components
│   │   ├── LoginPage.jsx
│   │   ├── Dashboard.jsx
│   │   ├── Vehicles.jsx
│   │   ├── Drivers.jsx
│   │   ├── Location.jsx
│   │   └── Emergencies.jsx
│   ├── services/            # API integration
│   │   └── api.js
│   ├── styles/              # CSS styles
│   │   ├── auth.css
│   │   ├── layout.css
│   │   ├── sidebar.css
│   │   ├── dashboard.css
│   │   ├── table.css
│   │   ├── location.css
│   │   └── emergencies.css
│   ├── App.jsx              # Main app component
│   ├── App.css              # App styles
│   ├── index.css            # Global styles
│   └── main.jsx             # Entry point
├── index.html
├── vite.config.js
├── tsconfig.json
├── package.json
└── README.md
```

## Installation

### Prerequisites
- Node.js 16+ or higher
- npm or yarn

### Setup

1. Install dependencies:
```bash
npm install
```

2. Create `.env.local` file:
```bash
cp .env.example .env.local
```

3. Update the API URL if needed (default: `http://localhost:8080`)

## Development

Start the development server:
```bash
npm run dev
```

The frontend will be available at `http://localhost:5173`

## Build

Build for production:
```bash
npm run build
```

Preview production build:
```bash
npm run preview
```

## Role-Based Access

### Dispatcher
- View and share their current location
- Report emergencies with GPS coordinates
- View emergency alerts
- Access to: Dashboard, My Location, Emergencies

### Operator
- Manage vehicles (CRUD operations)
- Manage drivers (CRUD operations)
- Manage trips
- View fleet analytics
- Access to: Dashboard, Vehicles, Drivers, Trips, Emergencies

### Admin
- Full access to all features
- View user management
- Full fleet management
- Emergency management
- Access to: All pages

## API Integration

The frontend connects to the API Gateway at `http://localhost:8080`. All endpoints are configured in `src/services/api.js`.

### Available Endpoints

- **Authentication**: Login, Register, Get Users
- **Vehicles**: CRUD operations, Status management
- **Drivers**: CRUD operations
- **Trips**: CRUD operations
- **Tracking**: Live tracking, Location reporting
- **Emergencies**: Get emergencies, Report emergency
- **Notifications**: Get notifications
- **AI Predictions**: ETA, Maintenance, Fuel, Delay, Driver, Health

## Authentication

Authentication uses JWT tokens stored in `localStorage`. The token is automatically included in all API requests via an axios interceptor.

## Styling

The frontend uses a custom CSS-based design system with:
- CSS custom properties (variables) for theming
- Responsive grid and flexbox layouts
- Dark theme optimized for 24/7 operations
- Color palette: Blue, Cyan, Green, Amber, Red

## Browser Support

- Chrome/Chromium (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Troubleshooting

### API Connection Error
- Verify the API Gateway is running on `http://localhost:8080`
- Check network tab for failed requests
- Ensure CORS is properly configured

### Login Issues
- Clear browser cookies and localStorage
- Check network connectivity
- Verify credentials

### Build Errors
- Clear `node_modules` and reinstall: `rm -rf node_modules && npm install`
- Clear Vite cache: `rm -rf dist && npm run build`

## Performance

- Uses React 19 with modern hooks
- Lazy component loading with React Router
- Efficient data fetching with axios
- Optimized CSS with CSS-in-JS approach
- Minimal external dependencies

## License

MIT License

## Support

For issues and feature requests, please contact the development team.
