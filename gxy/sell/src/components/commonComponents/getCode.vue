
<template>
<div>
    <button @click="code" v-show="showtimer">获取验证码</button>
    <button style="background-color:#ccc; border:#ccc;"  v-show="!showtimer">还需要{{time}}秒</button>
</div>
</template>
<script>
// 这是一个获取验证码的组件
export default {
  name: "getCode",
  props:['times'],
  data(){
      return{
            showtimer:true,
            time:this.times
      }
  },
  methods:{
    code(){
        this.showtimer = !this.showtimer;
      var timer = setInterval(()=>{
        this.time--;
        if(this.time == 0){
          this.showtimer = !this.showtimer;
          this.time = 9;
          clearInterval(timer)
        }
      },1000)
    },
  }
};
</script>
<style>
button {
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
</style>