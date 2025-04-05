import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import './index.css'; // Make sure your CSS file is imported

ReactDOM.createRoot(document.getElementById('app')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
