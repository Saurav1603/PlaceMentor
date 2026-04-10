import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { analyticsAPI } from '../api/api';

export default function PerformancePage() {
  const { user } = useAuth();
  const [performance, setPerformance] = useState([]);
  const [skillGaps, setSkillGaps] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [perfRes, gapRes] = await Promise.all([
        analyticsAPI.getPerformance(user.userId),
        analyticsAPI.getSkillGaps(user.userId),
      ]);
      setPerformance(perfRes.data);
      setSkillGaps(gapRes.data);
    } catch (err) {
      console.error('Failed to load performance data:', err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading"><div className="spinner"></div></div>;
  }

  const getLevelBadge = (level) => {
    const cls = `badge ${level === 'WEAK' ? 'badge-weak' : level === 'MEDIUM' ? 'badge-medium' : 'badge-strong'}`;
    return <span className={cls} style={{ padding: '4px 12px', borderRadius: '20px', fontSize: '0.75rem', fontWeight: 600 }}>{level}</span>;
  };

  return (
    <div>
      <div className="page-header fade-in">
        <h1>Performance Analytics</h1>
        <p>Detailed breakdown of your topic-wise performance</p>
      </div>

      {performance.length === 0 ? (
        <div className="empty-state fade-in">
          <div className="empty-icon">📈</div>
          <h3>No performance data yet</h3>
          <p>Take your first test to see your performance analytics here.</p>
        </div>
      ) : (
        <>
          {/* Performance Bars */}
          <div className="card fade-in" style={{ marginBottom: '24px' }}>
            <div className="card-header">
              <h2>Topic Accuracy</h2>
            </div>
            {performance.map((p, i) => (
              <div key={i} className="progress-bar-container">
                <div className="progress-bar-header">
                  <span className="topic-name">{p.topic}</span>
                  <span className="topic-value">
                    {p.accuracy?.toFixed(1)}% · {p.correctCount}/{p.totalAttempted} correct
                  </span>
                </div>
                <div className="progress-bar">
                  <div
                    className={`progress-bar-fill ${p.level?.toLowerCase()}`}
                    style={{ width: `${Math.max(p.accuracy, 3)}%` }}
                  ></div>
                </div>
              </div>
            ))}
          </div>

          {/* Performance Table */}
          <div className="card fade-in">
            <div className="card-header">
              <h2>Detailed Breakdown</h2>
            </div>
            <div style={{ overflowX: 'auto' }}>
              <table className="perf-table">
                <thead>
                  <tr>
                    <th>Topic</th>
                    <th>Attempted</th>
                    <th>Correct</th>
                    <th>Accuracy</th>
                    <th>Level</th>
                  </tr>
                </thead>
                <tbody>
                  {performance.map((p, i) => (
                    <tr key={i}>
                      <td style={{ fontWeight: 600 }}>{p.topic}</td>
                      <td>{p.totalAttempted}</td>
                      <td>{p.correctCount}</td>
                      <td className="accuracy-cell" style={{ 
                        color: p.accuracy >= 80 ? 'var(--success)' : p.accuracy >= 60 ? 'var(--warning)' : 'var(--danger)' 
                      }}>
                        {p.accuracy?.toFixed(1)}%
                      </td>
                      <td>{getLevelBadge(p.level)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          {/* Skill Gaps */}
          {skillGaps?.gaps?.length > 0 && (
            <div className="card fade-in">
              <div className="card-header">
                <h2>Skill Gap Analysis</h2>
              </div>
              <div className="topic-tags" style={{ gap: '12px' }}>
                {skillGaps.gaps
                  .sort((a, b) => a.accuracy - b.accuracy)
                  .map((g, i) => (
                    <div key={i} style={{
                      padding: '16px 20px',
                      background: 'var(--bg-input)',
                      border: '1px solid var(--border)',
                      borderRadius: 'var(--radius)',
                      flex: '1',
                      minWidth: '200px',
                    }}>
                      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
                        <span style={{ fontWeight: 600 }}>{g.topic}</span>
                        {getLevelBadge(g.level)}
                      </div>
                      <div className="progress-bar" style={{ height: '6px' }}>
                        <div
                          className={`progress-bar-fill ${g.level?.toLowerCase()}`}
                          style={{ width: `${Math.max(g.accuracy, 3)}%` }}
                        ></div>
                      </div>
                    </div>
                  ))}
              </div>
            </div>
          )}
        </>
      )}
    </div>
  );
}
