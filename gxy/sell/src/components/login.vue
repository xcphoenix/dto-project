<template>
  <div id="login">
    <div class="header">登陆</div>
    <div class="main">
      <div class="userid">
        <span>账号</span>
        <input type="text" />
        <button>获取验证码</button>
      </div>
      <div class="userpassword">
        <span>密码</span>
        <input type="password" />
      </div>
      <router-link to="/shouye/homepage">
          <span>登陆</span>
        </router-link>
      <button class="loginbtn">登陆</button>
    </div>

    <div class="thirdsoftware">
        <span>微信</span>
        <span>QQ</span>
        <span>微博</span>
      </div>
      <span>未有账号？</span>
        <router-link to="/register">
          <span>注册</span>
        </router-link>
  </div>
 
  
  
</template>
<script>
export default {
  name: "login",
  methods: {
    login() {
      this.$router.push({ path: "/shouye" });
    }
  }
};
</script>
<style scoped>
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
  border: 1px solid black;
  width: 98%;
  margin: 0 auto;
  margin-top: 20%;
}
.userid {
  margin-top: 60px;
  margin-bottom: 80px;
}
.userpassword {
  margin-top: 80px;
  margin-bottom: 80px;
}
.main span {
  font-size: 35px;
  margin-left: 20px;
  margin-right: 15px;
}
.main input {
  border: 1px solid #ccc;
  padding: 10px 0px;
  border-radius: 3px;
  padding-left: 5px;
  width: 65%;
}
input:focus {
  border-color: #66afe9;
  outline: 0;
  -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075),
    0 0 8px rgba(102, 175, 233, 0.6);
  box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075),
    0 0 8px rgba(102, 175, 233, 0.6);
}
.userid button{
  display: inline-block;
  width: 16%;
  font-size: 20px;
  padding-top: 10px;
  padding-bottom:10px;
  padding-left: 5px;
  padding-right: 5px;

}
</style>