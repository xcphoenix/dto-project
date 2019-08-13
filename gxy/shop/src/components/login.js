import React from 'react'
import api from '../httpConfig'
class App extends React.Component{
    constructor(props){
        super(props);
    }
    login=()=>{
        let username = this.refs.username.value
        let password = this.refs.userpassword.value
            console.log(  localStorage)
            let reg = /^[0-9]+.?[0-9]*$/;
            let json1 = {
                userPhone:username,
                userPassword:password
            };
            let json2 = {
              userName: username,
              userPassword: password
            };
            if (username == " " || password == " ") {
            } else if (reg.test(username[0])) {
              console.log(json1)
                this.axios.post(api.login_phone,json1).then(res=>{
                  console.log('000')
                if(res.data.code==200){
                    let token = res.data.data.token
                    localStorage.setItem('token',token)
                    console.log('111')
                    this.props.history.push('/addshop')
                }
      
              },err=>{
                console.log(err)
              })
            } else { 
               console.log(json2)
              this.axios.post(api.login_name,json2).then(res=>{
                console.log(res.data)
                this.props.history.push('/addshop')
              },err=>{
                console.log(err)
              })
            }
          
    }

    render(){
        return(
            <div>
                <h3>登陆</h3>
                <div>
                <div>
                    <span>用户名/手机号</span>
                </div>
               <input type="text" ref='username' ></input>
                </div>
                <div>
                    <div>
                        <span>密码</span>
                    </div>
                    <input type="password" ref='userpassword'></input>
                </div>
                <button onClick={this.login}>登陆</button>
           

            </div>
        )
    }
}

export default App