<template>
  <div id="login">
    <div class="header">登陆</div>
    <div class="register">
      <span>未有账号？</span>
      <router-link to="/register">
        <span>注册</span>
      </router-link>
    </div>
    <div class="main">
      <div class="login_cell">
        <div class="login_title">
          <div>
            <span>用户名</span>
          </div>
        </div>
        <div class="login_value">
          <input type="text" v-model="username" placeholder="请输入用户名/手机号" class="login_input" />
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
          <input type="password" v-model="password" placeholder="请输入密码" class="login_input" />
        </div>
      </div>
      <div class="thirdsoftware">
        <i class="iconfont icon-weibo1"></i>
        <i class="iconfont icon-zhifubao"></i>
        <i class="iconfont icon-weixin"></i>
      </div>
      <div >
        <!-- <router-link to="/shouye/homepage"> -->
        <combtn @click.native="login">登陆</combtn>
      <router-link to="/findpass"> <div class="forget">忘记密码</div></router-link> 
      </div>
    </div>
  </div>
</template>
<script>
import { Button } from "vant";
import { Field } from "vant";
import { mapState, mapGetters, mapActions } from "vuex";
import api from "../router/httpConfig";
import combtn from './commonComponents/comBtn'
import getcode from './commonComponents/getCode'
export default {
  name: "login",
  components:{
    combtn,getcode
  },
  data() {
    return {
      password: "",
      username: "",
      times:9,
    };
  },
  methods: {
    login() {
      console.log('123')
      let reg = /^[0-9]+.?[0-9]*$/;
      let json1 = {
        userPhone: this.username,
        userPassword: this.password
      };
      let json2 = {
        userName: this.username,
        userPassword: this.password
      };
      json1 = JSON.stringify(json1)
      json2 = JSON.stringify(json2)
      if (this.username == " " || this.password == " ") {
      } else if (reg.test(this.username[0])) {
      } else {
      }
    }
  }
};
</script>
<style scoped>

#login {
  background-color: rgb(248, 248, 248);
  height: 100%;
}
.register {
  float: right;
  margin-top: 20px;
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