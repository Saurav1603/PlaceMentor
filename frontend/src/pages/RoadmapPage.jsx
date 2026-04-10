import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { roadmapAPI } from '../api/api';

export default function RoadmapPage() {
  const { user } = useAuth();
  const [roadmap, setRoadmap] = useState(null);
  const [loading, setLoading] = useState(true);
  const [generating, setGenerating] = useState(false);

  useEffect(() => {
    loadRoadmap();
  }, []);

  const loadRoadmap = async () => {
    try {
      const { data } = await roadmapAPI.getRoadmap(user.userId);
      setRoadmap(data);
    } catch (err) {
      console.error('Failed to load roadmap:', err);
    } finally {
      setLoading(false);
    }
  };

  const generateRoadmap = async () => {
    setGenerating(true);
    try {
      await roadmapAPI.generate(user.userId);
      await loadRoadmap();
    } catch (err) {
      console.error('Failed to generate roadmap:', err);
    } finally {
      setGenerating(false);
    }
  };

  if (loading) {
    return <div className="loading"><div className="spinner"></div></div>;
  }

  const hasRoadmap = roadmap?.weeks?.length > 0;

  return (
    <div>
      <div className="page-header fade-in">
        <h1>Learning Roadmap</h1>
        <p>Your personalized weekly learning plan</p>
      </div>

      {!hasRoadmap ? (
        <div className="empty-state fade-in">
          <div className="empty-icon">🗺️</div>
          <h3>No roadmap generated yet</h3>
          <p>Take a test first, and your roadmap will be automatically generated based on your performance. Or generate one manually below.</p>
          <button
            className="btn btn-primary"
            onClick={generateRoadmap}
            disabled={generating}
            style={{ maxWidth: '300px', marginTop: '20px' }}
          >
            {generating ? 'Generating...' : '🗺️ Generate Roadmap'}
          </button>
        </div>
      ) : (
        <>
          <div style={{ marginBottom: '24px' }} className="fade-in">
            <button
              className="btn btn-secondary"
              onClick={generateRoadmap}
              disabled={generating}
              style={{ fontSize: '0.85rem' }}
            >
              {generating ? 'Regenerating...' : '🔄 Regenerate Roadmap'}
            </button>
          </div>

          <div className="roadmap-timeline fade-in">
            {roadmap.weeks.map((week, wIndex) => (
              <div key={wIndex} className="week-block" style={{ animationDelay: `${wIndex * 0.1}s` }}>
                <div className="week-header">
                  <h3>Week {week.weekNumber}</h3>
                  <span className="week-topic">{week.topic}</span>
                </div>
                {week.days?.map((day, dIndex) => (
                  <div key={dIndex} className="day-item">
                    <div className="day-number">D{day.dayNumber}</div>
                    <div className="day-activity">{day.activity}</div>
                    <div className="day-status">
                      {day.completed ? '✅' : '⬜'}
                    </div>
                  </div>
                ))}
              </div>
            ))}
          </div>
        </>
      )}
    </div>
  );
}
