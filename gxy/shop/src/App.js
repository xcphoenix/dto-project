import React from 'react';
import './App.css';

function App() {
  return (
    <div className="App">
      <header className="App-header">
      <div className="Login">
        <div className="login">
        <div className="loginform">
          <div className="text">
            <span>用户名</span>
          </div>
          <div>
            <input type="text"></input>
          </div>
        </div>
        <div className="loginform">
          <div className="text">
            <span>密码</span>
          </div>
          <div>
            <input type="text"></input>
          </div>
        </div>
        <button>登陆</button>
      </div>
      </div>
      </header>
    </div>
  );
}

export default App;
