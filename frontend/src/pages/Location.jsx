import React, { useEffect, useState } from 'react'
import { MapPin, CheckCircle, AlertCircle } from 'lucide-react'
import { trackingAPI } from '../services/api'
import '../styles/location.css'

export default function Location() {
  const [location, setLocation] = useState(null)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const [sharing, setSharing] = useState(false)

  useEffect(() => {
    requestLocation()
  }, [])

  const requestLocation = () => {
    setLoading(true)
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude, accuracy } = position.coords
          setLocation({ latitude, longitude, accuracy })
          setError('')
        },
        (err) => {
          setError('Unable to access location. Please enable location permissions.')
          console.error(err)
        }
      )
    } else {
      setError('Geolocation not supported on this browser')
    }
    setLoading(false)
  }

  const handleShareLocation = async () => {
    if (!location) {
      setError('Location not available')
      return
    }

    setSharing(true)
    try {
      await trackingAPI.reportLocation({
        latitude: location.latitude,
        longitude: location.longitude,
        accuracy: location.accuracy,
        timestamp: new Date().toISOString(),
      })
      setError('')
    } catch (err) {
      setError('Failed to share location: ' + err)
    } finally {
      setSharing(false)
    }
  }

  return (
    <div className="location-container">
      <div className="location-header">
        <h1>My Location</h1>
        <p>Share your current location with dispatch</p>
      </div>

      <div className="location-card">
        <div className="location-status">
          <MapPin size={32} />
          <div>
            <h2>Current Location</h2>
            <p>{location ? 'Location acquired' : 'Waiting for location...'}</p>
          </div>
        </div>

        {error && (
          <div className="alert alert-error">
            <AlertCircle size={16} />
            <span>{error}</span>
          </div>
        )}

        {location && (
          <div className="location-details">
            <div className="detail-row">
              <span>Latitude</span>
              <span className="detail-value">{location.latitude.toFixed(6)}</span>
            </div>
            <div className="detail-row">
              <span>Longitude</span>
              <span className="detail-value">{location.longitude.toFixed(6)}</span>
            </div>
            <div className="detail-row">
              <span>Accuracy</span>
              <span className="detail-value">{Math.round(location.accuracy)} meters</span>
            </div>
            <div className="detail-row">
              <span>Updated</span>
              <span className="detail-value">{new Date().toLocaleTimeString()}</span>
            </div>
          </div>
        )}

        <div className="location-actions">
          <button
            className="btn btn-secondary"
            onClick={requestLocation}
            disabled={loading}
          >
            {loading ? 'Getting location...' : 'Refresh Location'}
          </button>
          <button
            className="btn btn-primary"
            onClick={handleShareLocation}
            disabled={!location || sharing}
          >
            {sharing ? 'Sharing...' : 'Share Location'}
          </button>
        </div>
      </div>

      <div className="location-info">
        <h3>How to Use</h3>
        <ol>
          <li>Click "Refresh Location" to get your current position</li>
          <li>Review the coordinates and accuracy</li>
          <li>Click "Share Location" to send it to dispatch</li>
          <li>Your location updates will be visible to the dispatch team</li>
        </ol>
      </div>
    </div>
  )
}
