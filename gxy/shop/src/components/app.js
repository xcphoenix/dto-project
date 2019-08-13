import React from 'react'
import{BrowserRouter as Router,Route,Link,Switch} from 'react-router-dom'
import login from './login'
import addshop from './addshop'
class App extends React.Component{
    constructor(props){
        super(props);
    }
    render(){
      return(
        <Router>
          <div>
            <Route exact path="/" component = {login}></Route>
            <Route path="/addshop" component={addshop}></Route>
          </div>
        </Router>
      )
    }
   
}

export default App