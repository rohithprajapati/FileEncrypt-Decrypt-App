// src/components/ThemeToggle.js

import { Switch } from 'antd';
import React from 'react';

const ThemeToggle = ({ darkMode, toggleDarkMode }) => {
  return (
    <div style={{ display: 'flex', alignItems: 'center' }}>
      <span style={{ marginRight: 8 }}>Light</span>
      <Switch checked={darkMode} onChange={toggleDarkMode} />
      <span style={{ marginLeft: 8 }}>Dark</span>
    </div>
  );
};

export default ThemeToggle;
