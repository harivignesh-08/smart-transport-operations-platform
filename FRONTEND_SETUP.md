# Openeye Frontend - Complete Setup Guide

## Overview

Openeye is a modern React-based Fleet Operations Platform with role-based access control. The frontend is built with Vite, React 18, and provides a professional interface for three different user roles.

## Features

### Dispatcher Role
- Share current GPS location with dispatch team
- Report emergencies with automatic location capture
- View emergency alerts and status
- Basic dashboard overview
- Minimal interface focused on critical operations

**Pages**: Dashboard, My Location, Emergencies

### Operator Role
- Full fleet management (vehicles, drivers, trips)
- View live tracking information
- Manage and monitor operations
- Access analytics dashboard
- Report and manage emergencies

**Pages**: Dashboard, Vehicles, Drivers, Trips, Tracking, Analytics, Emergencies

### Admin Role
- Complete platform access
- User management
- Full fleet and asset management
- Emergency management with full control
- Advanced analytics and reporting
- System health monitoring

**Pages**: All pages including Users Management

## Installation & Setup

### Prerequisites
- Node.js 16+ or higher
- npm 8+ or yarn/pnpm
- Backend API Gateway running on `http://localhost:8080`

### Step 1: Install Dependencies

```bash
cd frontend
npm install --legacy-peer-deps
```

### Step 2: Configure Environment

Create `.env.local` file:
```bash
cp .env.example .env.local
```

Update the API URL if needed (default is `http://localhost:8080`):
```
VITE_API_URL=http://localhost:8080
```

### Step 3: Start Development Server

```bash
npm run dev
```

The frontend will be available at `http://localhost:5173`

### Step 4: Access the Application

1. Navigate to `http://localhost:5173`
2. Use the following test credentials:
   - **Dispatcher**: username: `dispatcher`, password: `any`
   - **Operator**: username: `operator`, password: `any`
   - **Admin**: username: `admin`, password: `any`

## Project Architecture

### Directory Structure

```
frontend/
├── src/
│   ├── components/
│   │   ├── Layout.jsx           # Main layout wrapper
│   │   ├── ProtectedRoute.jsx   # Role-based route protection
│   │   └── Sidebar.jsx          # Navigation sidebar
│   │
│   ├── context/
│   │   └── AuthContext.jsx      # Authentication state management
│   │
│   ├── pages/
│   │   ├── LoginPage.jsx        # Authentication (login/register)
│   │   ├── Dashboard.jsx        # Role-specific dashboard
│   │   ├── Vehicles.jsx         # Vehicle management (CRUD)
│   │   ├── Drivers.jsx          # Driver management (CRUD)
│   │   ├── Location.jsx         # Dispatcher location sharing
│   │   └── Emergencies.jsx      # Emergency management
│   │
│   ├── services/
│   │   └── api.js               # Axios API client & endpoints
│   │
│   ├── styles/
│   │   ├── index.css            # Global styles
│   │   ├── auth.css             # Authentication styles
│   │   ├── layout.css           # Layout styles
│   │   ├── sidebar.css          # Sidebar styles
│   │   ├── dashboard.css        # Dashboard styles
│   │   ├── table.css            # Table & form styles
│   │   ├── location.css         # Location page styles
│   │   └── emergencies.css      # Emergency page styles
│   │
│   ├── App.jsx                  # Main app with routing
│   ├── App.css                  # App-level styles
│   ├── main.jsx                 # Entry point
│   └── index.css                # CSS variables & reset
│
├── index.html
├── vite.config.js
├── tsconfig.json
├── package.json
└── README.md
```

## API Integration

### Available Endpoints

All endpoints configured in `src/services/api.js`:

#### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `GET /api/users` - Get all users

#### Vehicle Management
- `GET /api/vehicles` - List all vehicles
- `GET /api/vehicles/{id}` - Get vehicle details
- `POST /api/vehicles` - Create new vehicle
- `PUT /api/vehicles/{id}` - Update vehicle
- `DELETE /api/vehicles/{id}` - Delete vehicle
- `PUT /api/vehicles/{id}/status` - Update vehicle status
- `PUT /api/vehicles/{id}/availability` - Update availability

#### Driver Management
- `GET /api/drivers` - List all drivers
- `POST /api/drivers` - Create driver
- `PUT /api/drivers/{id}` - Update driver
- `DELETE /api/drivers/{id}` - Delete driver

#### Tracking
- `GET /api/tracking/live` - Get live tracking data
- `POST /api/tracking/location` - Report GPS location
- `WS /ws/tracking` - WebSocket for real-time updates

#### Emergency Management
- `GET /emergencies` - Get all emergencies
- `POST /emergencies` - Report emergency

#### AI Predictions
- `GET /predict/eta` - ETA prediction
- `GET /predict/maintenance` - Maintenance prediction
- `GET /predict/fuel` - Fuel consumption
- `GET /predict/delay` - Delay prediction
- `GET /predict/driver` - Driver behavior
- `GET /predict/health` - Vehicle health

#### Other Services
- `GET /api/notifications` - Get notifications
- `GET /api/map/live` - Live map data
- `POST /files/upload` - Upload file
- `GET /files` - Get files
- `GET /digitaltwins` - Digital twins
- `GET /health` - Health check

## Authentication Flow

1. User navigates to `/login`
2. Enter credentials (username & password)
3. System calls `POST /api/auth/login`
4. Backend returns JWT token and user role
5. Token stored in localStorage
6. Redirected to dashboard with role-specific layout
7. All subsequent API calls include Bearer token
8. Automatic logout on 401 response

## Role-Based Access Control (RBAC)

### Route Protection

Routes are protected using `ProtectedRoute` component:

```jsx
<Route
  path="/vehicles"
  element={
    <ProtectedRoute requiredRoles={['OPERATOR', 'ADMIN']}>
      <Layout>
        <Vehicles />
      </Layout>
    </ProtectedRoute>
  }
/>
```

### Menu Items by Role

The sidebar automatically shows different menu items based on user role:

- **DISPATCHER**: Dashboard, My Location, Emergencies
- **OPERATOR**: Dashboard, Vehicles, Drivers, Trips, Tracking, Analytics, Emergencies
- **ADMIN**: All menu items including Users

## Key Components

### AuthContext
Manages authentication state globally:
- `isAuthenticated` - Login status
- `user` - Username
- `role` - User role (DISPATCHER, OPERATOR, ADMIN)
- `login()` - Set authentication
- `logout()` - Clear authentication

### ProtectedRoute
Wraps routes that require authentication:
- Checks if user is authenticated
- Verifies user has required role
- Redirects to login if not authenticated
- Redirects to home if role not allowed

### Layout
Main layout wrapper providing:
- Sidebar navigation
- Top bar with user info
- Responsive design
- Role-aware menu

## Styling System

### CSS Variables

Global design tokens defined in `src/index.css`:

```css
--primary: #3b82f6           /* Blue */
--secondary: #06b6d4         /* Cyan */
--success: #10b981           /* Green */
--warning: #f59e0b           /* Amber */
--danger: #ef4444            /* Red */
--dark: #0f1419              /* Background */
--text-primary: #e0e7ff      /* Primary text */
--text-secondary: #a1a5b4    /* Secondary text */
--text-muted: #6b7280        /* Muted text */
```

### Responsive Design

- Mobile first approach
- Breakpoint: 768px (tablet/desktop)
- CSS Grid & Flexbox for layouts
- Mobile sidebar with hamburger menu

## Development Workflow

### Adding a New Page

1. Create component in `src/pages/PageName.jsx`
2. Add styles in `src/styles/page-name.css`
3. Import in `src/App.jsx`
4. Add route with appropriate role protection
5. Add menu item to Sidebar if needed

### Adding API Integration

1. Add endpoint function to `src/services/api.js`
2. Use in component with error handling
3. Display data or errors to user

### Debugging

Use console.log with `[v0]` prefix:
```javascript
console.log('[v0] Error loading data:', error)
```

## Build & Deployment

### Development Build
```bash
npm run dev
```

### Production Build
```bash
npm run build
```

### Preview Build
```bash
npm run preview
```

### Deploy to Vercel
```bash
npm run build
# Push to GitHub
# Vercel will auto-deploy
```

## Environment Variables

### Available Variables
- `VITE_API_URL` - Backend API Gateway URL (default: `http://localhost:8080`)

### Adding New Variables
1. Add to `.env.example`
2. Add to `.env.local`
3. Access via `import.meta.env.VITE_*`

## Performance Optimizations

- React 18 with concurrent features
- Lazy route loading (React Router v6)
- Axios request caching
- Minimal external dependencies (6 total)
- CSS-based animations (hardware accelerated)
- Responsive images and lazy loading

## Browser Support

- Chrome/Chromium 90+
- Firefox 88+
- Safari 15+
- Edge 90+

## Troubleshooting

### Port Already in Use
```bash
# Kill process on port 5173
lsof -ti :5173 | xargs kill -9
```

### Module Not Found
```bash
npm install --legacy-peer-deps
```

### CORS Issues
Ensure API Gateway CORS is configured:
```
Access-Control-Allow-Origin: *
Access-Control-Allow-Methods: GET, POST, PUT, DELETE
Access-Control-Allow-Headers: Content-Type, Authorization
```

### Login Not Working
1. Check network tab (F12)
2. Verify API Gateway is running
3. Check credentials format
4. Clear localStorage and retry

### Blank Page
1. Check console for errors (F12)
2. Verify API URL in .env.local
3. Clear browser cache
4. Restart dev server

## Testing Credentials

Use any username/password combination. The system will assign roles based on username:
- `admin` → ADMIN role
- `dispatcher` → DISPATCHER role
- `operator` → OPERATOR role (or any other username)

## Performance Monitoring

Check Core Web Vitals in browser DevTools:
- LCP (Largest Contentful Paint) - ~2s
- INP (Interaction to Next Paint) - <100ms
- CLS (Cumulative Layout Shift) - <0.1

## Code Quality

Run linting:
```bash
npm run lint
```

Type checking:
```bash
npm run type-check
```

## Security Notes

- JWT tokens stored in localStorage (production should use httpOnly cookies)
- HTTPS recommended for production
- Implement rate limiting on backend
- Validate all user inputs on backend
- Use environment variables for sensitive data

## Support & Resources

- Documentation: This file
- API Reference: `/API_ENDPOINTS_REFERENCE.md`
- Backend Setup: `/README.md`

## License

MIT License - See LICENSE file

## Release Notes

### v1.0.0 (2026-07-12)
- Initial release
- Role-based access control
- Complete fleet management
- Real-time location tracking
- Emergency management system
- Responsive design
