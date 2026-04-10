import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { testAPI } from '../api/api';

export default function TestPage() {
  const { user } = useAuth();
  const [state, setState] = useState('idle'); // idle, testing, results
  const [testData, setTestData] = useState(null);
  const [answers, setAnswers] = useState({});
  const [results, setResults] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const startTest = async () => {
    setLoading(true);
    setError('');
    try {
      const { data } = await testAPI.start(user.userId);
      setTestData(data);
      setAnswers({});
      setState('testing');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to start test');
    } finally {
      setLoading(false);
    }
  };

  const selectAnswer = (questionId, answer) => {
    setAnswers({ ...answers, [questionId]: answer });
  };

  const submitTest = async () => {
    if (Object.keys(answers).length < testData.questions.length) {
      setError('Please answer all questions before submitting');
      return;
    }

    setLoading(true);
    setError('');
    try {
      const submission = {
        testId: testData.testId,
        answers: Object.entries(answers).map(([questionId, selectedAnswer]) => ({
          questionId: parseInt(questionId),
          selectedAnswer,
        })),
      };
      const { data } = await testAPI.submit(user.userId, submission);
      setResults(data);
      setState('results');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to submit test');
    } finally {
      setLoading(false);
    }
  };

  const answeredCount = Object.keys(answers).length;

  // Idle State
  if (state === 'idle') {
    return (
      <div>
        <div className="page-header fade-in">
          <h1>Assessment Center</h1>
          <p>Test your knowledge and improve your skills</p>
        </div>
        <div className="test-start fade-in">
          <div style={{ fontSize: '4rem', marginBottom: '24px' }}>🧠</div>
          <h2>Ready to Challenge Yourself?</h2>
          <p>You'll receive 10 randomly selected questions covering various DSA topics. Answer carefully — your performance shapes your personalized learning path.</p>
          {error && <div className="error-message" style={{ maxWidth: '400px', margin: '0 auto 20px' }}>{error}</div>}
          <button className="btn btn-primary" onClick={startTest} disabled={loading} style={{ maxWidth: '300px' }}>
            {loading ? 'Preparing test...' : '🚀 Start Test'}
          </button>
        </div>
      </div>
    );
  }

  // Testing State
  if (state === 'testing') {
    return (
      <div>
        <div className="page-header fade-in">
          <h1>Test in Progress</h1>
          <p>Answer all questions to the best of your ability</p>
        </div>

        <div className="test-progress fade-in">
          <span className="test-progress-text">{answeredCount} / {testData.questions.length} answered</span>
          <div className="test-progress-bar">
            <div className="test-progress-fill" style={{ width: `${(answeredCount / testData.questions.length) * 100}%` }}></div>
          </div>
        </div>

        {error && <div className="error-message">{error}</div>}

        {testData.questions.map((q, index) => (
          <div key={q.questionId} className="question-card fade-in" style={{ animationDelay: `${index * 0.05}s` }}>
            <div className="question-number">Question {index + 1}</div>
            <div className="question-meta">
              <span>{q.topic}</span>
              <span>{q.difficulty}</span>
            </div>
            <div className="question-text">{q.questionText}</div>
            <div className="options-grid">
              {['A', 'B', 'C', 'D'].map((letter) => (
                <button
                  key={letter}
                  className={`option-btn ${answers[q.questionId] === letter ? 'selected' : ''}`}
                  onClick={() => selectAnswer(q.questionId, letter)}
                >
                  <span className="option-letter">{letter}</span>
                  {q[`option${letter}`]}
                </button>
              ))}
            </div>
          </div>
        ))}

        <div className="test-actions">
          <button className="btn btn-primary" onClick={submitTest} disabled={loading} style={{ maxWidth: '300px' }}>
            {loading ? 'Submitting...' : `✓ Submit Test (${answeredCount}/${testData.questions.length})`}
          </button>
        </div>
      </div>
    );
  }

  // Results State
  if (state === 'results' && results) {
    return (
      <div>
        <div className="page-header fade-in">
          <h1>Test Results</h1>
          <p>Here's how you performed</p>
        </div>

        <div className="results-summary">
          <div className="result-card fade-in fade-in-delay-1">
            <div className="result-value" style={{ color: 'var(--primary-light)' }}>{results.score}/{results.totalQuestions}</div>
            <div className="result-label">Score</div>
          </div>
          <div className="result-card fade-in fade-in-delay-2">
            <div className="result-value" style={{ color: results.accuracy >= 60 ? 'var(--success)' : 'var(--danger)' }}>
              {results.accuracy?.toFixed(1)}%
            </div>
            <div className="result-label">Accuracy</div>
          </div>
          <div className="result-card fade-in fade-in-delay-3">
            <div className="result-value" style={{ color: 'var(--success)' }}>
              {results.results?.filter(r => r.isCorrect).length}
            </div>
            <div className="result-label">Correct</div>
          </div>
          <div className="result-card fade-in fade-in-delay-4">
            <div className="result-value" style={{ color: 'var(--danger)' }}>
              {results.results?.filter(r => !r.isCorrect).length}
            </div>
            <div className="result-label">Incorrect</div>
          </div>
        </div>

        {/* Detailed Results */}
        {results.results?.map((r, index) => (
          <div key={index} className="question-card fade-in" style={{ animationDelay: `${index * 0.05}s` }}>
            <div className="question-number">
              {r.isCorrect ? '✅' : '❌'} Question {index + 1}
            </div>
            <div className="question-text">{r.questionText}</div>
            <div style={{ display: 'flex', gap: '16px', flexWrap: 'wrap', fontSize: '0.9rem' }}>
              <div>
                <span style={{ color: 'var(--text-muted)' }}>Your answer: </span>
                <span style={{ color: r.isCorrect ? 'var(--success)' : 'var(--danger)', fontWeight: 600 }}>{r.selectedAnswer}</span>
              </div>
              {!r.isCorrect && (
                <div>
                  <span style={{ color: 'var(--text-muted)' }}>Correct: </span>
                  <span style={{ color: 'var(--success)', fontWeight: 600 }}>{r.correctAnswer}</span>
                </div>
              )}
            </div>
          </div>
        ))}

        <div className="test-actions" style={{ marginBottom: '40px' }}>
          <button className="btn btn-primary" onClick={startTest} style={{ maxWidth: '300px' }}>
            🔄 Take Another Test
          </button>
          <button className="btn btn-secondary" onClick={() => setState('idle')} style={{ maxWidth: '300px' }}>
            Back to Start
          </button>
        </div>
      </div>
    );
  }

  return null;
}
