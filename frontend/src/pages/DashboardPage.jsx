import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { analyticsAPI } from '../api/api';

export default function DashboardPage() {
  const { user } = useAuth();
  const [dashboard, setDashboard] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboard();
  }, []);

  const loadDashboard = async () => {
    try {
      const { data } = await analyticsAPI.getDashboard(user.userId);
      setDashboard(data);
    } catch (err) {
      console.error('Failed to load dashboard:', err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading"><div className="spinner"></div></div>;
  }

  if (!dashboard) {
    return (
      <div className="empty-state">
        <div className="empty-icon">📊</div>
        <h3>Welcome to PlaceMentor!</h3>
        <p>Take your first test to see your dashboard analytics.</p>
      </div>
    );
  }

  const circumference = 2 * Math.PI * 65;
  const readinessOffset = circumference - (dashboard.placementReadinessScore / 100) * circumference;

  const getLevelBadgeClass = (level) => {
    switch (level?.toLowerCase()) {
      case 'beginner': return 'badge-beginner';
      case 'intermediate': return 'badge-intermediate';
      case 'advanced': return 'badge-advanced';
      default: return 'badge-beginner';
    }
  };

  return (
    <div>
      <div className="page-header fade-in">
        <h1>Welcome back, {dashboard.name} 👋</h1>
        <p>Here's your learning progress overview</p>
      </div>

      {/* Stats Grid */}
      <div className="stats-grid">
        <div className="stat-card fade-in fade-in-delay-1">
          <div className="stat-icon">🎯</div>
          <div className="stat-value">{dashboard.overallAccuracy?.toFixed(1)}%</div>
          <div className="stat-label">Overall Accuracy</div>
        </div>
        <div className="stat-card fade-in fade-in-delay-2">
          <div className="stat-icon">📝</div>
          <div className="stat-value">{dashboard.totalTestsTaken}</div>
          <div className="stat-label">Tests Taken</div>
        </div>
        <div className="stat-card fade-in fade-in-delay-3">
          <div className="stat-icon">❓</div>
          <div className="stat-value">{dashboard.totalQuestionsAttempted}</div>
          <div className="stat-label">Questions Attempted</div>
        </div>
        <div className="stat-card fade-in fade-in-delay-4">
          <div className="stat-icon">🏆</div>
          <div className="stat-value" style={{ fontSize: '1.3rem' }}>
            <span className={`badge ${getLevelBadgeClass(dashboard.learningLevel)}`} style={{ padding: '8px 16px', borderRadius: '12px' }}>
              {dashboard.learningLevel}
            </span>
          </div>
          <div className="stat-label" style={{ marginTop: '8px' }}>Learning Level</div>
        </div>
      </div>

      {/* Charts Row */}
      <div className="chart-container">
        {/* Readiness Score */}
        <div className="card fade-in">
          <div className="card-header">
            <h2>Placement Readiness</h2>
            <span className="badge" style={{ background: 'var(--primary-glow)', color: 'var(--primary-light)' }}>
              {dashboard.goal}
            </span>
          </div>
          <div className="readiness-ring">
            <svg width="160" height="160">
              <circle className="ring-bg" cx="80" cy="80" r="65" />
              <circle
                className="ring-fill"
                cx="80" cy="80" r="65"
                stroke={dashboard.placementReadinessScore >= 70 ? 'var(--success)' : dashboard.placementReadinessScore >= 40 ? 'var(--warning)' : 'var(--danger)'}
                strokeDasharray={circumference}
                strokeDashoffset={readinessOffset}
              />
            </svg>
            <div className="readiness-value">
              <div className="score">{dashboard.placementReadinessScore?.toFixed(0)}%</div>
              <div className="label">Ready</div>
            </div>
          </div>
        </div>

        {/* Topic Performance */}
        <div className="card fade-in">
          <div className="card-header">
            <h2>Topic Performance</h2>
          </div>
          {dashboard.topicPerformances?.length > 0 ? (
            dashboard.topicPerformances.map((tp, i) => (
              <div key={i} className="progress-bar-container">
                <div className="progress-bar-header">
                  <span className="topic-name">{tp.topic}</span>
                  <span className="topic-value">{tp.accuracy?.toFixed(1)}% · {tp.level}</span>
                </div>
                <div className="progress-bar">
                  <div
                    className={`progress-bar-fill ${tp.level?.toLowerCase()}`}
                    style={{ width: `${Math.max(tp.accuracy, 3)}%` }}
                  ></div>
                </div>
              </div>
            ))
          ) : (
            <div className="empty-state" style={{ padding: '32px' }}>
              <p>No performance data yet. Take a test!</p>
            </div>
          )}
        </div>
      </div>

      {/* Weak & Strong Topics */}
      <div className="chart-container">
        <div className="card fade-in">
          <div className="card-header">
            <h2>⚠️ Weak Topics</h2>
          </div>
          {dashboard.weakTopics?.length > 0 ? (
            <div className="topic-tags">
              {dashboard.weakTopics.map((t, i) => (
                <span key={i} className="topic-tag weak">{t}</span>
              ))}
            </div>
          ) : (
            <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>No weak topics — great job!</p>
          )}
        </div>
        <div className="card fade-in">
          <div className="card-header">
            <h2>💪 Strong Topics</h2>
          </div>
          {dashboard.strongTopics?.length > 0 ? (
            <div className="topic-tags">
              {dashboard.strongTopics.map((t, i) => (
                <span key={i} className="topic-tag strong">{t}</span>
              ))}
            </div>
          ) : (
            <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Keep practicing to master topics!</p>
          )}
        </div>
      </div>
    </div>
  );
}
