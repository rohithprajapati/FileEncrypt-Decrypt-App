import React from 'react';
import ReactDOM from 'react-dom/client';  // For React 18 and onwards
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

const root = ReactDOM.createRoot(document.getElementById('root'));  // Correct method for React 18+
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);


// Optional: Enable Web Vitals reporting (you can keep or remove this part)
reportWebVitals();
