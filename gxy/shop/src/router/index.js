import React,{Component} from  'react'
import {Switch,Route,Redirect,Link} from 'react-router-dom'
import addshop from '../components/addshop'
import app from '../components/app'
class RouterIndex extends Component{
    render(){
        return(
            <Switch>
            <Route path='/' component={app}/>
            <Route path='/addshop' component={addshop}/>
            </Switch>
        )
    }
}
export default RouterIndex
