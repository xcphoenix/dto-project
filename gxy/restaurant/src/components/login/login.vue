<template>
<div>
    <span>用户名</span>
    <input type="text"><br>
    <span>密码</span>
    <input type="text"><br>
    <router-link to="/Restaurant"> <button @click="login">登陆</button></router-link>
</div>
</template>
<script>
    export default{
        methods:{
            login(){
                //请求
                //判断有无店铺
                //有  -->Restaurant
                //没有 -->CreateRestaurant
            }
        }
    }
</script>
<style>
</style>