import React from 'react';
import ReactDOM from 'react-dom';
import App from './components/app';
import axios from './configaxios'

React.Component.prototype.axios = axios

ReactDOM.render(<App />,document.getElementById('root'));


