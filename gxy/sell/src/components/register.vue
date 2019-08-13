<template>
<div id="register">
    <div class="header">注册</div>
     <div class="main">
      <div class="login_cell">
        <div class="login_title">
          <div>
            <span>手机号</span>
          </div>
        </div>
        <div class="login_value">
          <input type="text" v-model="userphone" placeholder="请输入手机号" class="login_input" />
        </div>
        <div class="login_code">
           <getcode :times='times'>
          </getcode>
        </div>
      </div>

      <div class="login_cell">
        <div class="login_title">
          <div>
            <span>密码</span>
          </div>
        </div>
        <div class="login_value">
          <input type="password" v-model="userpassword" placeholder="请输入密码" class="login_input" />
        </div>
      </div>
      <div>
        <!-- <router-link to="/"> -->
           <combtn @click.native="register">注册</combtn>
        <!-- </router-link> -->
      </div>
    </div>
</div>
</template>

<script>
import combtn from './commonComponents/comBtn'
import getcode from './commonComponents/getCode'
import api from '../router/httpConfig'
export default{
components:{
  combtn,
  getcode
},
data(){
    return{
        userphone:'',
        userpassword:'',
        times:20,
    }
},
methods:{
  register(){
      let json1 = {
        userPhone: this.userphone,
        userPassword: this.userpassword
      };
      // json1 = JSON.stringify(json1)
      console.log(json1)
      if (this.username == " " || this.userpassword == " ") {
      }else{
        this.$axios.post(api.register,json1).then(res=>{
          console.log(res.data)
          if(res.data.code == 10002){
            alert('不能重复注册')
          }else{
            this.$router.push({path:'/'})
          }
        },err=>{
          console.log(err)
        })
      }
  }
}
}
</script>

<style>
#register {
  background-color: rgb(248, 248, 248);
  height: 100%;
}
.header {
  width: 100%;
  height: 95px;
  background-color: rgb(149, 190, 94);
  text-align: center;
  font-size: 40px;
  line-height: 95px;
  color: white;
}
.register{
  float: right;
  margin-top:20px;
  margin-right: 5px;
}
.header {
  width: 100%;
  height: 95px;
  background-color: rgb(149, 190, 94);
  text-align: center;
  font-size: 40px;
  line-height: 95px;
  color: white;
}
.main {
  margin: 0 auto;
  margin-top: 30%;
}
span {
  font-size: 30px;
  color: rgb(50, 50, 51);
}
input {
}
.login_cell {
  width: 100%;
  height: 80px;
  display: flex;
  margin-bottom: 20px;
  background-color: rgb(255, 255, 255);
}
.login_title {
  height: 80px;
  line-height: 80px;
  padding-left: 50px;
}
.login_title div {
  width: 110px;
}
.login_input {
  margin-left: 20px;
  vertical-align: middle;
}
.login_value {
  height: 80px;
  line-height: 80px;
}
input {
  font-size: 25px;
}
.login_code {
  height: 80px;
  float: right;
  margin-left: 90px;
}
.login_code button {
  margin-top: 10%;
  width: 180px;
  padding-left: 15px;
  padding-right: 15px;
  padding-top: 12px;
  padding-bottom: 12px;
  vertical-align: middle;
  background-color: rgb(7, 193, 96);
  color: #fff;
  border: 1px solid rgb(7, 193, 96);
  border-radius: 4px;
}

i {
  font-size: 65px;
  margin-right: 60px;
}
.thirdsoftware {
  margin-top: 120px;
  margin-left: 29%;
}
.icon-weibo1 {
  color: rgb(229, 36, 37);
}
.icon-zhifubao {
  color: rgb(6, 180, 253);
}
.icon-weixin {
  color: rgb(76, 191, 0);
}
.forget {
  float: right;
  margin-right: 75px;
  margin-top: 20px;
  color: rgb(54, 214, 183);
  cursor: pointer;
}
</style>