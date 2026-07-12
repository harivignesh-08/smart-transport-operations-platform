import React, { useState, useEffect } from 'react';
import Sidebar from './components/Sidebar';
import TopBar from './components/TopBar';
import LoginPage from './pages/LoginPage';
import Dashboard from './pages/Dashboard';
import FleetTracking from './pages/FleetTracking';
import AiPredictions from './pages/AiPredictions';
import EmergencyConsole from './pages/EmergencyConsole';
import FleetManagement from './pages/FleetManagement';

export default function App() {
  const [loggedIn, setLoggedIn] = useState(false);
  const [currentTab, setCurrentTab] = useState('dashboard');
  const [userRole, setUserRole] = useState(null);
  const [username, setUsername] = useState(null);

  useEffect(() => {
    // Check if token exists on load
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');
    const user = localStorage.getItem('username');
    if (token && role) {
      setLoggedIn(true);
      setUserRole(role);
      setUsername(user);
    }
  }, []);

  if (!loggedIn) {
    return <LoginPage setLoggedIn={setLoggedIn} setUserRole={setUserRole} setUsername={setUsername} />;
  }

  return (
    <div className="app-container">
      {/* Sidebar Navigation */}
      <Sidebar 
        currentTab={currentTab} 
        setCurrentTab={setCurrentTab} 
        setLoggedIn={setLoggedIn}
        userRole={userRole}
        username={username}
      />

      {/* Main Panel */}
      <main className="main-content">
        {/* Header toolbar */}
        <TopBar currentTab={currentTab} userRole={userRole} />

        {/* Tab pages - Role-based rendering */}
        {currentTab === 'dashboard' && <Dashboard setCurrentTab={setCurrentTab} userRole={userRole} />}
        {currentTab === 'tracking' && <FleetTracking userRole={userRole} />}
        {currentTab === 'predictions' && <AiPredictions userRole={userRole} />}
        {currentTab === 'emergencies' && <EmergencyConsole userRole={userRole} />}
        {currentTab === 'fleet' && <FleetManagement userRole={userRole} />}
      </main>
    </div>
  );
}
