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

  useEffect(() => {
    // Check if token exists on load
    const token = localStorage.getItem('token');
    if (token) {
      setLoggedIn(true);
    }
  }, []);

  if (!loggedIn) {
    return <LoginPage setLoggedIn={setLoggedIn} />;
  }

  return (
    <div className="app-container">
      {/* Sidebar Navigation */}
      <Sidebar 
        currentTab={currentTab} 
        setCurrentTab={setCurrentTab} 
        setLoggedIn={setLoggedIn} 
      />

      {/* Main Panel */}
      <main className="main-content">
        {/* Header toolbar */}
        <TopBar currentTab={currentTab} />

        {/* Tab pages */}
        {currentTab === 'dashboard' && <Dashboard setCurrentTab={setCurrentTab} />}
        {currentTab === 'tracking' && <FleetTracking />}
        {currentTab === 'predictions' && <AiPredictions />}
        {currentTab === 'emergencies' && <EmergencyConsole />}
        {currentTab === 'fleet' && <FleetManagement />}
      </main>
    </div>
  );
}
