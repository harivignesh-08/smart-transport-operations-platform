# Openeye Frontend - Quick Start

Get the Openeye frontend running in 5 minutes.

## Prerequisites

- Node.js 16+ ([Download](https://nodejs.org/))
- Backend API running on `http://localhost:8080`

## Installation

### 1. Install Dependencies
```bash
cd frontend
npm install --legacy-peer-deps
```

### 2. Configure Environment
```bash
cp .env.example .env.local
```

### 3. Start Development Server
```bash
npm run dev
```

### 4. Open in Browser
Navigate to `http://localhost:5173`

## Login

Use any of these test credentials:

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin` | any |
| Dispatcher | `dispatcher` | any |
| Operator | `operator` | any |

## What Each Role Can Do

### 👮 Dispatcher
- Share GPS location
- Report emergencies
- View alerts
- Access: Dashboard, My Location, Emergencies

### 👨‍💼 Operator
- Manage vehicles & drivers
- View trips
- Real-time tracking
- Analytics
- Access: Dashboard, Vehicles, Drivers, Trips, Tracking, Analytics, Emergencies

### 🛡️ Admin
- Full platform access
- User management
- System monitoring
- All pages

## Quick Commands

```bash
# Development
npm run dev              # Start dev server

# Production
npm run build            # Build for production
npm run preview          # Preview production build

# Code Quality
npm run lint            # Run ESLint
npm run type-check      # TypeScript check
```

## API Gateway Requirements

The backend API Gateway must:

1. Be running on `http://localhost:8080`
2. Have CORS enabled for `http://localhost:5173`
3. Implement JWT authentication
4. Provide the 30+ endpoints (see documentation)

## Troubleshooting

### Port Already in Use
```bash
# Kill process on 5173
lsof -ti :5173 | xargs kill -9
```

### Dependencies Issue
```bash
npm install --legacy-peer-deps
```

### Clear Cache
```bash
npm cache clean --force
npm install
npm run dev
```

## Environment Variables

**Development (.env.local)**
```
VITE_API_URL=http://localhost:8080
```

**Production (.env.production)**
```
VITE_API_URL=https://api.openeye.com
```

## File Structure

```
frontend/
├── src/
│   ├── components/     # React components
│   ├── context/        # Global state
│   ├── pages/          # Page components
│   ├── services/       # API client
│   ├── styles/         # CSS styling
│   ├── App.jsx         # Main app
│   └── main.jsx        # Entry point
│
└── Configuration
    ├── vite.config.js
    ├── package.json
    └── tsconfig.json
```

## API Endpoints

### Authentication
- `POST /api/auth/login` - Login
- `POST /api/auth/register` - Register
- `GET /api/users` - Get users

### Fleet
- `GET/POST/PUT/DELETE /api/vehicles` - Vehicles
- `GET/POST/PUT/DELETE /api/drivers` - Drivers
- `GET/POST/PUT/DELETE /api/trips` - Trips

### Operations
- `GET /api/tracking/live` - Live tracking
- `POST /api/tracking/location` - Report location
- `GET /emergencies` - Get emergencies
- `POST /emergencies` - Report emergency

### AI (When Backend Provides)
- `GET /predict/eta` - ETA prediction
- `GET /predict/maintenance` - Maintenance
- `GET /predict/fuel` - Fuel consumption
- `GET /predict/delay` - Delay prediction
- `GET /predict/driver` - Driver behavior
- `GET /predict/health` - Vehicle health

## Features

✅ Role-based access control
✅ Responsive design (mobile/tablet/desktop)
✅ Dark theme optimized for 24/7
✅ Real-time GPS tracking
✅ Emergency management
✅ Vehicle & driver management
✅ Professional UI
✅ Error handling
✅ Loading states
✅ Form validation

## Browser Support

- Chrome 90+
- Firefox 88+
- Safari 15+
- Edge 90+

## Building for Production

```bash
# Build
npm run build

# Output in dist/
# Deploy dist/ folder to web server
```

## Deployment

### Vercel
```bash
npm run build
git push origin main
# Auto-deploys
```

### Docker
```bash
docker build -t openeye-frontend .
docker run -p 3000:3000 openeye-frontend
```

### Traditional Server
```bash
npm run build
# Upload dist/ to web server
# Configure web server to serve index.html
```

## Development Tips

### Add a New Page
1. Create `src/pages/NewPage.jsx`
2. Add styles in `src/styles/new-page.css`
3. Add route in `src/App.jsx`
4. Add menu item in `src/components/Sidebar.jsx`

### Add API Endpoint
1. Add function to `src/services/api.js`
2. Import and use in component
3. Handle errors with try/catch

### Debug
- Use browser DevTools (F12)
- Check Network tab for API calls
- Check Console for errors
- Use React DevTools extension

## Performance

- Bundle: ~450KB (gzipped)
- Load: <2s on 4G
- Mobile optimized
- Dark theme for reduced power

## Next Steps

1. Start the dev server: `npm run dev`
2. Login with test credentials
3. Explore the interface
4. Check console for any errors
5. Verify API connection
6. Review code structure
7. Start development

## Support

- Documentation: See `FRONTEND_SETUP.md`
- API Reference: See `API_ENDPOINTS_REFERENCE.md`
- Issues: Check browser console (F12)

## License

MIT

---

**Happy coding! 🚀**

Need help? Check the full documentation in `FRONTEND_SETUP.md`
